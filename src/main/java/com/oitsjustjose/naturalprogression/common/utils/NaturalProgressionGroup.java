package com.oitsjustjose.naturalprogression.common.utils;

import com.oitsjustjose.naturalprogression.common.blocks.NaturalProgressionBlocks;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class NaturalProgressionGroup extends ItemGroup
{
    private static NaturalProgressionGroup instance;

    private NaturalProgressionGroup()
    {
        super("natural-progression.name");
    }

    public static NaturalProgressionGroup getInstance()
    {
        if (instance == null)
        {
            instance = new NaturalProgressionGroup();
        }
        return instance;
    }

    @Override
    @Nonnull
    public ItemStack createIcon()
    {
        return new ItemStack(NaturalProgressionBlocks.stonePebble, 1);
    }
}