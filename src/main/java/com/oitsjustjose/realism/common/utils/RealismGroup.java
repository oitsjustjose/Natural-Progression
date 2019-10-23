package com.oitsjustjose.realism.common.utils;

import com.oitsjustjose.realism.common.blocks.RealismBlocks;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class RealismGroup extends ItemGroup
{
    private static RealismGroup instance;

    private RealismGroup()
    {
        super("geolosys-realism.name");
    }

    public static RealismGroup getInstance()
    {
        if (instance == null)
        {
            instance = new RealismGroup();
        }
        return instance;
    }

    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(RealismBlocks.stonePebble, 1);
    }
}