package com.oitsjustjose.realism.common.items;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;

public class FlintItemTier implements IItemTier
{
    @Override
    public int getMaxUses()
    {
        return 16;
    }

    @Override
    public float getEfficiency()
    {
        return 1.5F;
    }

    @Override
    public float getAttackDamage()
    {
        return 1.0F;
    }

    @Override
    public int getHarvestLevel()
    {
        return 0;
    }

    @Override
    public int getEnchantability()
    {
        return 0;
    }

    @Override
    public Ingredient getRepairMaterial()
    {
        return Ingredient.fromItems(Items.FLINT);
    }
}