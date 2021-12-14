package com.oitsjustjose.natprog.common.recipes;

import java.util.Random;

import javax.annotation.Nonnull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.oitsjustjose.natprog.NatProg;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * Special shapeless recipe that allows use of Tools, and damages the tool
 * properly
 *
 * @author Credit to Integral for the Enchantment Transpose Recipe as a guide
 */

public class DamageItemRecipe extends ShapelessRecipe {
    private final NonNullList<Ingredient> inputs;
    private final ItemStack output;

    private DamageItemRecipe(ResourceLocation id, String group, ItemStack output,
            NonNullList<Ingredient> inputs) {
        super(id, group, output, inputs);
        this.inputs = inputs;
        this.output = output;
    }

    @Override
    @Nonnull
    public ItemStack assemble(CraftingContainer p_44260_) {
        return this.output.copy();
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        StackedContents stackedcontents = new StackedContents();
        int i = 0;

        for (int j = 0; j < inv.getContainerSize(); ++j) {
            ItemStack itemstack = inv.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                stackedcontents.accountStack(itemstack, 1);
            }
        }

        return i == this.inputs.size() && stackedcontents.canCraft(this, null);
    }

    @Override
    @Nonnull
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.getItem().isDamageable(stack)) {
                ItemStack savedStack = stack.copy();
                boolean shouldAttemptDmg = true;
                Random random = new Random();
                int unbreakingLvl = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, savedStack);

                if (unbreakingLvl > 0) {
                    shouldAttemptDmg = (1 + random.nextInt(5)) <= unbreakingLvl;
                }

                if (savedStack.getDamageValue() < savedStack.getMaxDamage()) {
                    if (shouldAttemptDmg) {
                        savedStack.setDamageValue(savedStack.getDamageValue() + 1);
                    }
                    nonnulllist.set(i, savedStack);
                } else {
                    nonnulllist.set(i, ItemStack.EMPTY);
                }
            }
        }

        return nonnulllist;
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return NatProg.DAMAGE_ITEM_RECIPE;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>
            implements RecipeSerializer<DamageItemRecipe> {
        @Override
        @Nonnull
        public DamageItemRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String s = GsonHelper.getAsString(json, "group");
            NonNullList<Ingredient> nonnulllist = readIngredients(
                    GsonHelper.getAsJsonArray(json, "ingredients"));

            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for natprog:damage_tools");
            }

            ItemStack stack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            return new DamageItemRecipe(recipeId, s, stack, nonnulllist);
        }

        private static NonNullList<Ingredient> readIngredients(JsonArray jsonArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for (int i = 0; i < jsonArray.size(); i++) {
                Ingredient ingr = Ingredient.fromJson(jsonArray.get(i));
                if (!ingr.isEmpty()) {
                    nonnulllist.add(ingr);
                }
            }
            return nonnulllist;
        }

        @Override
        public DamageItemRecipe fromNetwork(ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
            String s = buffer.readUtf(32767);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

            for (int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, Ingredient.fromNetwork(buffer));
            }

            ItemStack stack = buffer.readItem();
            return new DamageItemRecipe(recipeId, s, stack, nonnulllist);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, DamageItemRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            buffer.writeVarInt(recipe.inputs.size());

            for (Ingredient ingredient : recipe.inputs) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItemStack(recipe.output, false);
        }
    }
}
