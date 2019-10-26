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

public class PlankRecipe extends ShapelessRecipe
{
    public PlankRecipe(ResourceLocation id, String group, ItemStack output, NonNullList<Ingredient> inputs)
    {
        super(id, group, output, inputs);
    }

    @Override
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
            ResourceLocation plankLoc = new ResourceLocation(log.getItem().getRegistryName().getNamespace(),
                    log.getItem().getRegistryName().getPath().replace("stripped_", "").replace("log", "planks"));
            Block b = ForgeRegistries.BLOCKS.getValue(plankLoc);
            if (b != null && b != Blocks.AIR)
            {
                return new ItemStack(b, count);
            }
        }
        // Return true that this recipe matches anyways because we want to cancel out the recipe
        // if (!log.isEmpty())
        // {
        //     return ItemStack.EMPTY;
        // }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean matches(CraftingInventory inv, World world)
    {
        ItemStack saw = ItemStack.EMPTY;
        ItemStack axe = ItemStack.EMPTY;
        ItemStack log = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack checkedItemStack = inv.getStackInSlot(i);

            if (!checkedItemStack.isEmpty())
            {

                NaturalProgression.getInstance().LOGGER.info(checkedItemStack);

                if (Utils.isLog(checkedItemStack))
                {
                    if (log.isEmpty())
                    {
                        log = checkedItemStack;
                    }
                    else
                    {
                        return false;
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
                        return false;
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
                        return false;
                    }
                }
            }
        }

        if (!saw.isEmpty() && !log.isEmpty())
        {
            return true;
        }
        if (!axe.isEmpty() && !log.isEmpty())
        {
            return true;
        }
        if (!log.isEmpty())
        {
            return true;
        }

        // Return true that this recipe matches anyways because we want to cancel out the recipe
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