package com.oitsjustjose.naturalprogression.common.items;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nonnull;

public class BoneItemTier implements IItemTier {
    @Override
    public int getMaxUses() {
        return 128;
    }

    @Override
    public float getEfficiency() {
        return 2.0F;
    }

    @Override
    public float getAttackDamage() {
        return 2.0F;
    }

    @Override
    public int getHarvestLevel() {
        return 1;
    }

    @Override
    public int getEnchantability() {
        return 0;
    }

    @Override
    @Nonnull
    public Ingredient getRepairMaterial() {
        return Ingredient.fromItems(Items.BONE);
    }
}