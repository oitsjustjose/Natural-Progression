package com.oitsjustjose.realism.common.utils;

import java.util.Random;

import com.oitsjustjose.realism.common.blocks.RealismBlocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class RealismGroup extends ItemGroup
{
    private static RealismGroup instance;

    private Block[] possibleBlocks = new Block[]
    { RealismBlocks.andesitePebble, RealismBlocks.dioritePebble, RealismBlocks.granitePebble,
            RealismBlocks.stonePebble };
    private long lastSwitchTime = System.currentTimeMillis();
    private ItemStack cached;

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
        if (System.currentTimeMillis() - lastSwitchTime > 1000)
        {
            Random rand = new Random();
            cached = new ItemStack(possibleBlocks[rand.nextInt(possibleBlocks.length)]);
            lastSwitchTime = System.currentTimeMillis();
        }

        return cached;
    }
}