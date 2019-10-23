package com.oitsjustjose.realism.common.utils;

import java.util.Objects;

import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.dimension.Dimension;

public class Utils
{
    public static String blockStateToName(BlockState state)
    {
        return I18n.format(state.getBlock().getTranslationKey());
    }

    public static ItemStack blockStateToStack(BlockState state)
    {
        return new ItemStack(state.getBlock().asItem(), 1);
    }

    public static boolean doStatesMatch(BlockState state1, BlockState state2)
    {
        return (state1.getBlock().getRegistryName() == state2.getBlock().getRegistryName());
    }

    public static String dimensionToString(Dimension dim)
    {
        return Objects.requireNonNull(dim.getType().getRegistryName().toString());
    }

    public static BlockPos getTopSolidBlock(IWorld world, BlockPos start)
    {
        BlockPos retPos = new BlockPos(start.getX(), world.getHeight() - 1, start.getZ());
        while (retPos.getY() > 0)
        {
            if (world.getBlockState(retPos).getMaterial().isSolid())
            {
                break;
            }
            retPos = retPos.down();
        }
        return retPos;
    }
}