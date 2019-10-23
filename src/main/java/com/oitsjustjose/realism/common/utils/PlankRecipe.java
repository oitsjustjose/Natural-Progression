/**
 * Special recipe type for chopping logs into planks
 * 
 * @author Credit to Integral for the Enchantment Transpose Recipe as a guide
*/

package com.oitsjustjose.realism.common.utils;

import com.google.gson.JsonObject;
import com.oitsjustjose.realism.Realism;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
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
    private RecipeManager recipes;

    public PlankRecipe(ResourceLocation id, String group, ItemStack output, NonNullList<Ingredient> inputs)
    {
        super(id, group, output, inputs);
        this.recipes = new RecipeManager();
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv)
    {
        ItemStack axe = null;
        ItemStack log = null;

        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack checkedItemStack = inv.getStackInSlot(i);

            if (!checkedItemStack.isEmpty())
            {
                if (checkedItemStack.getToolTypes().contains(ToolType.AXE))
                {
                    if (axe == null)
                    {
                        axe = checkedItemStack.copy();
                    }
                }
                else if (ItemTags.LOGS.contains(checkedItemStack.getItem()))
                {
                    log = checkedItemStack;
                }
            }

            if (axe != null && log != null)
            {
                break;
            }
        }

        if (axe != null && log != null)
        {
            ResourceLocation plankLoc = new ResourceLocation(log.getItem().getRegistryName().getNamespace(),
                    log.getItem().getRegistryName().getPath().replace("log", "planks"));
            Block b = ForgeRegistries.BLOCKS.getValue(plankLoc);
            if (b != null && b != Blocks.AIR)
            {
                return new ItemStack(b, 4);
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean matches(CraftingInventory inv, World world)
    {
        ItemStack axe = null;
        ItemStack log = null;

        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack checkedItemStack = inv.getStackInSlot(i);

            if (!checkedItemStack.isEmpty())
            {
                if (checkedItemStack.getToolTypes().contains(ToolType.AXE))
                {
                    if (axe == null)
                    {
                        axe = checkedItemStack.copy();
                    }
                }
                else if (ItemTags.LOGS.contains(checkedItemStack.getItem()))
                {
                    log = checkedItemStack;
                }
            }

            if (axe != null && log != null)
            {
                break;
            }
        }

        if (axe != null && log != null)
        {
            ResourceLocation plankLoc = new ResourceLocation(log.getItem().getRegistryName().getNamespace(),
                    log.getItem().getRegistryName().getPath().replace("log", "planks"));
            Block b = ForgeRegistries.BLOCKS.getValue(plankLoc);
            if (b != null && b != Blocks.AIR)
            {
                return true;
            }
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
            if (stack.getToolTypes().contains(ToolType.AXE))
            {
                ItemStack savedAxe = stack.copy();

                if (!savedAxe.hasTag())
                {
                    savedAxe.setTag(new CompoundNBT());
                    savedAxe.getTag().putInt("Damage", 0);
                }
                
                int damage = savedAxe.getTag().getInt("Damage");
                if (damage < savedAxe.getMaxDamage())
                {
                    savedAxe.getTag().putInt("Damage", damage + 1);
                    nonnulllist.set(i, savedAxe);
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
        return Realism.PLANK_SLICING;
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