package com.oitsjustjose.naturalprogression.common.utils;

import javax.annotation.Nonnull;

import com.oitsjustjose.naturalprogression.common.items.NaturalProgressionItems;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class NaturalProgressionGroup extends ItemGroup {
    private static NaturalProgressionGroup instance;

    private NaturalProgressionGroup() {
        super("natural-progression.name");
    }

    public static NaturalProgressionGroup getInstance() {
        if (instance == null) {
            instance = new NaturalProgressionGroup();
        }
        return instance;
    }

    @Override
    @Nonnull
    public ItemStack createIcon() {
        return new ItemStack(NaturalProgressionItems.flintSaw);
    }
}