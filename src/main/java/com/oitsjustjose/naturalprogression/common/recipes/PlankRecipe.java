
package com.oitsjustjose.naturalprogression.common.recipes;

import java.util.Objects;
import java.util.Random;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.oitsjustjose.naturalprogression.NaturalProgression;
import com.oitsjustjose.naturalprogression.common.config.CommonConfig;
import com.oitsjustjose.naturalprogression.common.items.SawItem;
import com.oitsjustjose.naturalprogression.common.utils.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * Special recipe type for chopping logs into planks
 *
 * @author Credit to Integral for the Enchantment Transpose Recipe as a guide
 */

public class PlankRecipe extends ShapelessRecipe
{
    private PlankRecipe(ResourceLocation id, String group, ItemStack output, NonNullList<Ingredient> inputs)
    {
        super(id, group, output, inputs);
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(CraftingInventory inv)
    {
        ItemStack saw = ItemStack.EMPTY;
        ItemStack axe = ItemStack.EMPTY;
        ItemStack log = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack checkedItemStack = inv.getStackInSlot(i);

            if (!checkedItemStack.isEmpty())
            {
                if (Utils.isLog(checkedItemStack))
                {
                    if (log.isEmpty())
                    {
                        log = checkedItemStack;
                    }
                    else
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (checkedItemStack.getItem() instanceof SawItem)
                {
                    if (saw.isEmpty())
                    {
                        saw = checkedItemStack.copy();
                    }
                    else
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (checkedItemStack.getToolTypes().contains(ToolType.AXE))
                {
                    if (axe.isEmpty())
                    {

                        axe = checkedItemStack.copy();
                    }
                    else
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        if (!saw.isEmpty() && !axe.isEmpty())
        {
            return ItemStack.EMPTY;
        }

        if ((!saw.isEmpty() && !log.isEmpty()) || (!axe.isEmpty() && !log.isEmpty()))
        {
            int count = axe.isEmpty() ? 4 : 1;

            ResourceLocation plankLoc = new ResourceLocation(
                    Objects.requireNonNull(log.getItem().getRegistryName()).getNamespace(),
                    log.getItem().getRegistryName().getPath().replace("stripped_", "").replace("log", "planks")
                            .replace("wood", "planks"));

            // Prevent the recipe from crafting itself for dupe issues
            if (!plankLoc.getPath().toLowerCase().contains("plank"))
            {
                return ItemStack.EMPTY;
            }

            // Require only stripped logs if the config is enabled
            if (CommonConfig.REQUIRE_STRIPPED_LOG_FOR_PLANKS.get()
                    && !log.getItem().getRegistryName().getPath().contains("stripped"))
            {
                return ItemStack.EMPTY;
            }

            Block b = ForgeRegistries.BLOCKS.getValue(plankLoc);
            if (b != null && b != Blocks.AIR)
            {
                return new ItemStack(b, count);
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean matches(CraftingInventory inv, World world)
    {
        return !getCraftingResult(inv).isEmpty();
    }

    @Override
    @Nonnull
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv)
    {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i)
        {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty())
            {
                continue;
            }
            if (stack.getItem() instanceof SawItem || stack.getToolTypes().contains(ToolType.AXE))
            {
                ItemStack savedStack = stack.copy();
                boolean shouldAttemptDmg = true;
                Random random = new Random();
                int unbreakingLvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, savedStack);

                if (unbreakingLvl > 0)
                {
                    shouldAttemptDmg = (1 + random.nextInt(5)) <= unbreakingLvl;
                }

                if (!savedStack.hasTag())
                {
                    savedStack.setTag(new CompoundNBT());
                    assert savedStack.getTag() != null;
                    savedStack.getTag().putInt("Damage", 0);
                }

                assert savedStack.getTag() != null;
                int damage = savedStack.getTag().getInt("Damage");

                if (damage < savedStack.getMaxDamage())
                {
                    if (shouldAttemptDmg)
                    {
                        savedStack.getTag().putInt("Damage", damage + 1);
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

    @Override
    @Nonnull
    public ItemStack getRecipeOutput()
    {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isDynamic()
    {
        return true;
    }

    @Override
    @Nonnull
    public IRecipeSerializer<?> getSerializer()
    {
        return NaturalProgression.PLANK_SLICING;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<PlankRecipe>
    {
        @Override
        @Nonnull
        public PlankRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json)
        {
            return new PlankRecipe(recipeId, "", ItemStack.EMPTY, NonNullList.create());
        }

        @Override
        public PlankRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer)
        {
            return new PlankRecipe(recipeId, "", ItemStack.EMPTY, NonNullList.create());
        }

        @Override
        public void write(@Nonnull PacketBuffer buffer, @Nonnull PlankRecipe recipe)
        {

        }
    }
}