/**
 * Special recipe type for chopping logs into planks
 * 
 * @author Credit to Integral for the Enchantment Transpose Recipe as a guide
*/

package com.oitsjustjose.naturalprogression.common.utils;

import com.google.gson.JsonObject;
import com.oitsjustjose.naturalprogression.NaturalProgression;
import com.oitsjustjose.naturalprogression.common.items.SawItem;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ToolItem;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class PlankRecipe extends ShapelessRecipe
{
    public PlankRecipe(ResourceLocation id, String group, ItemStack output, NonNullList<Ingredient> inputs)
    {
        super(id, group, output, inputs);
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv)
    {
        ItemStack saw = null;
        ItemStack axe = null;
        ItemStack log = null;

        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack checkedItemStack = inv.getStackInSlot(i);

            if (!checkedItemStack.isEmpty())
            {
                if (checkedItemStack.getItem() instanceof SawItem)
                {
                    if (saw == null)
                    {
                        saw = checkedItemStack.copy();
                    }
                }
                else if (checkedItemStack.getToolTypes().contains(ToolType.AXE))
                {
                    axe = checkedItemStack.copy();
                }
                else if (ItemTags.LOGS.contains(checkedItemStack.getItem()))
                {
                    log = checkedItemStack;
                }
            }
        }

        // If you have *both* in there, then we don't want a match.
        if (axe != null && saw != null)
        {
            return ItemStack.EMPTY;
        }
        if (saw != null && log != null)
        {
            ResourceLocation plankLoc = new ResourceLocation(log.getItem().getRegistryName().getNamespace(),
                    log.getItem().getRegistryName().getPath().replace("stripped_", "").replace("log", "planks"));
            Block b = ForgeRegistries.BLOCKS.getValue(plankLoc);
            if (b != null && b != Blocks.AIR)
            {
                return new ItemStack(b, 4);
            }
        }
        else if (axe != null && log != null)
        {
            ResourceLocation plankLoc = new ResourceLocation(log.getItem().getRegistryName().getNamespace(),
                    log.getItem().getRegistryName().getPath().replace("log", "planks"));
            Block b = ForgeRegistries.BLOCKS.getValue(plankLoc);
            if (b != null && b != Blocks.AIR)
            {
                return new ItemStack(b, 1);
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean matches(CraftingInventory inv, World world)
    {
        ItemStack saw = null;
        ItemStack axe = null;
        ItemStack log = null;

        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack checkedItemStack = inv.getStackInSlot(i);

            if (!checkedItemStack.isEmpty())
            {
                if (checkedItemStack.getItem() instanceof SawItem)
                {
                    if (saw == null)
                    {
                        saw = checkedItemStack.copy();
                    }
                }
                else if (checkedItemStack.getToolTypes().contains(ToolType.AXE))
                {
                    axe = checkedItemStack.copy();
                }
                else if (ItemTags.LOGS.contains(checkedItemStack.getItem()))
                {
                    log = checkedItemStack;
                }
            }
        }

        // If you have *both* in there, then we don't want a match.
        if (axe != null && saw != null)
        {
            return false;
        }
        if (saw != null && log != null)
        {
            return true;
        }
        if (axe != null && log != null)
        {
            return true;
        }
        // Return true that this recipe matches anyways because we want to cancel out the recipe
        if (log != null)
        {
            return true;
        }

        return false;
    }

    @Override
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

                if (!savedStack.hasTag())
                {
                    savedStack.setTag(new CompoundNBT());
                    savedStack.getTag().putInt("Damage", 0);
                }

                int damage = savedStack.getTag().getInt("Damage");
                if (damage < savedStack.getMaxDamage())
                {
                    savedStack.getTag().putInt("Damage", damage + 1);
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
    public IRecipeSerializer<?> getSerializer()
    {
        return NaturalProgression.PLANK_SLICING;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<PlankRecipe>
    {
        @Override
        public PlankRecipe read(ResourceLocation recipeId, JsonObject json)
        {
            return new PlankRecipe(recipeId, "", ItemStack.EMPTY, NonNullList.create());
        }

        @Override
        public PlankRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
        {
            return new PlankRecipe(recipeId, "", ItemStack.EMPTY, NonNullList.create());
        }

        @Override
        public void write(PacketBuffer buffer, PlankRecipe recipe)
        {

        }
    }
}