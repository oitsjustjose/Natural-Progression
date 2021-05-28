package com.oitsjustjose.naturalprogression.common.items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.oitsjustjose.naturalprogression.NaturalProgression;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;

public class DynamicItemTier implements IItemTier {
    private int maxUses;
    private float efficiency;
    private float attackDamage;
    private int harvestLvl;
    private int enchantability;
    private Ingredient repairMat;

    public DynamicItemTier(int maxUses, float eff, float dmg, int harv, int ench, Ingredient repairMat) {
        this.maxUses = maxUses;
        this.efficiency = eff;
        this.attackDamage = dmg;
        this.harvestLvl = harv;
        this.enchantability = ench;
        this.repairMat = repairMat;
    }

    public DynamicItemTier(int maxUses, float eff, float dmg, int harv, int ench, ITag<Item> repairMatTag) {
        this.maxUses = maxUses;
        this.efficiency = eff;
        this.attackDamage = dmg;
        this.harvestLvl = harv;
        this.enchantability = ench;
        this.repairMat = Ingredient.fromTag(repairMatTag);
    }

    public DynamicItemTier() {
        this.maxUses = 0;
        this.efficiency = 0;
        this.attackDamage = 0;
        this.harvestLvl = 0;
        this.enchantability = 0;
        this.repairMat = Ingredient.fromItems(Items.BEDROCK);
    }

    public DynamicItemTier setMaxUses(int maxUses) {
        this.maxUses = maxUses;
        return this;
    }

    public DynamicItemTier setEfficiency(float eff) {
        this.efficiency = eff;
        return this;
    }

    public DynamicItemTier setAttackDamage(float dmg) {
        this.attackDamage = dmg;
        return this;
    }

    public DynamicItemTier setHarvestLvl(int lvl) {
        this.harvestLvl = lvl;
        return this;
    }

    public DynamicItemTier setEnchantability(int ench) {
        this.enchantability = ench;
        return this;
    }

    public DynamicItemTier setRepairMat(Ingredient mat) {
        this.repairMat = mat;
        return this;
    }

    public DynamicItemTier setRepairMat(@Nullable ITag<Item> tag) {
        if (tag == null || tag.getAllElements().isEmpty()) {
            this.repairMat = Ingredient.fromItems(Items.BEDROCK);
            NaturalProgression.getInstance().LOGGER.warn(
                    "Dynamic saw repair material {} could not be found. Defaulting to Bedrock",
                    tag == null ? "" : tag.toString());
        } else {
            this.repairMat = Ingredient.fromTag(tag);
        }
        return this;
    }

    public DynamicItemTier setRepairMats(Item... items) {
        this.repairMat = Ingredient.fromItems(items);
        return this;
    }

    @Override
    public int getMaxUses() {
        return this.maxUses;
    }

    @Override
    public float getEfficiency() {
        return this.efficiency;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public int getHarvestLevel() {
        return this.harvestLvl;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    @Nonnull
    public Ingredient getRepairMaterial() {
        return this.repairMat;
    }
}
