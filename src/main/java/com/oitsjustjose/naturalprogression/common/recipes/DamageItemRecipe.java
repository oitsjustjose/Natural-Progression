package com.oitsjustjose.naturalprogression.common.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.oitsjustjose.naturalprogression.NaturalProgression;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Special shapeless recipe that allows use of Tools, and damages the tool properly
 *
 * @author Credit to Integral for the Enchantment Transpose Recipe as a guide
 */

public class DamageItemRecipe extends ShapelessRecipe
{
	private final NonNullList<Ingredient> inputs;
	private final ItemStack output;

	private DamageItemRecipe(ResourceLocation id, String group, ItemStack output, NonNullList<Ingredient> inputs)
	{
		super(id, group, output, inputs);
		this.inputs = inputs;
		this.output = output;
	}

	@Override @Nonnull public ItemStack getCraftingResult(CraftingInventory inv)
	{
		return this.output.copy();
	}

	@Override public boolean matches(CraftingInventory inv, World world)
	{
		RecipeItemHelper recipeitemhelper = new RecipeItemHelper();
		int i = 0;

		for (int j = 0; j < inv.getSizeInventory(); ++j)
		{
			ItemStack itemstack = inv.getStackInSlot(j);
			if (!itemstack.isEmpty())
			{
				++i;
				recipeitemhelper.func_221264_a(itemstack, 1);
			}
		}

		return i == this.inputs.size() && recipeitemhelper.canCraft(this, null);
	}

	@Override @Nonnull public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv)
	{
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

		for (int i = 0; i < nonnulllist.size(); ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.isEmpty())
			{
				continue;
			}
			if (stack.getItem().isDamageable())
			{
				ItemStack savedStack = stack.copy();
				boolean shouldAttemptDmg = true;
				Random random = new Random();
				int unbreakingLvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, savedStack);

				if (unbreakingLvl > 0)
				{
					shouldAttemptDmg = (1 + random.nextInt(5)) <= unbreakingLvl;
				}

				if (savedStack.getDamage() < savedStack.getMaxDamage())
				{
					if (shouldAttemptDmg)
					{
						savedStack.setDamage(savedStack.getDamage() + 1);
					}
					nonnulllist.set(i, savedStack);
				}
				else
				{
					nonnulllist.set(i, ItemStack.EMPTY);
				}
			}
		}

		return nonnulllist;
	}

	@Override @Nonnull public IRecipeSerializer<?> getSerializer()
	{
		return NaturalProgression.DAMAGE_ITEM_RECIPE;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<DamageItemRecipe>
	{
		@Override @Nonnull public DamageItemRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json)
		{
			String s = JSONUtils.getString(json, "group", "");
			NonNullList<Ingredient> nonnulllist = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));

			if (nonnulllist.isEmpty())
			{
				throw new JsonParseException("No ingredients for natural-progression:damage_tools");
			}

			ItemStack stack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
			return new DamageItemRecipe(recipeId, s, stack, nonnulllist);
		}

		private static NonNullList<Ingredient> readIngredients(JsonArray jsonArray)
		{
			NonNullList<Ingredient> nonnulllist = NonNullList.create();

			for (int i = 0; i < jsonArray.size(); i++)
			{
				Ingredient ingr = Ingredient.deserialize(jsonArray.get(i));
				if (!ingr.hasNoMatchingItems())
				{
					nonnulllist.add(ingr);
				}
			}
			return nonnulllist;
		}

		@Override public DamageItemRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer)
		{
			String s = buffer.readString(32767);
			int i = buffer.readVarInt();
			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < nonnulllist.size(); ++j)
			{
				nonnulllist.set(j, Ingredient.read(buffer));
			}

			ItemStack stack = buffer.readItemStack();
			return new DamageItemRecipe(recipeId, s, stack, nonnulllist);
		}

		@Override public void write(PacketBuffer buffer, DamageItemRecipe recipe)
		{
			buffer.writeString(recipe.getGroup());
			buffer.writeVarInt(recipe.inputs.size());

			for (Ingredient ingredient : recipe.inputs)
			{
				ingredient.write(buffer);
			}

			buffer.writeItemStack(recipe.output);
		}
	}
}