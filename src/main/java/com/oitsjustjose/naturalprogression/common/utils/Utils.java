package com.oitsjustjose.naturalprogression.common.utils;

import java.util.Objects;
import java.util.Random;

import javax.annotation.Nullable;

import com.oitsjustjose.naturalprogression.common.blocks.NaturalProgressionBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.dimension.Dimension;

public class Utils
{

    public static Block getPebbleForPos(IWorld world, BlockPos pos)
    {
        BlockPos search = new BlockPos(pos.getX(), 0, pos.getZ());
        for (int y = 1; y < getTopSolidBlock(world, pos).getY(); y++)
        {
            if (world.getBlockState(search.up(y)).getBlock() == Blocks.GRANITE)
            {
                return NaturalProgressionBlocks.granitePebble;
            }
            if (world.getBlockState(search.up(y)).getBlock() == Blocks.DIORITE)
            {
                return NaturalProgressionBlocks.dioritePebble;

            }
            if (world.getBlockState(search.up(y)).getBlock() == Blocks.ANDESITE)
            {
                return NaturalProgressionBlocks.andesitePebble;

            }
            if (world.getBlockState(search.up(y)).getBlock() == Blocks.SAND
                    || world.getBlockState(search.up(y)).getBlock() == Blocks.SANDSTONE)
            {
                return NaturalProgressionBlocks.sandstonePebble;
            }
        }
        return NaturalProgressionBlocks.stonePebble;
    }

    /**
     * @param world an IWorld instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block at pos is replaceable
     */
    public static boolean canReplace(IWorld world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);
        Material mat = state.getMaterial();
        return state.isFoliage(world, pos) || mat.isReplaceable();
    }

    /**
     * Determines if the sample can be placed on this block
     * 
     * @param world: an IWorld instance
     * @param pos:   The current searching position that will be used to confirm
     * @return true if the block below is solid on top AND isn't in the blacklist
     */
    public static boolean canPlaceOn(IWorld world, BlockPos pos)
    {
        return Block.func_220055_a(world, pos.down(), Direction.UP);
    }

    /**
     * @param world an IWorld instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block is water (since we can waterlog)
     */
    public static boolean isInWater(IWorld world, BlockPos pos)
    {
        if (world.getBlockState(pos).getBlock() == Blocks.WATER)
        {
            return true;
        }
        if (world.getBlockState(pos) instanceof ILiquidContainer)
        {
            return true;
        }

        return false;
    }

    /**
     * @param world an IWorld instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block is in a non-water fluid
     */
    public static boolean inNonWaterFluid(IWorld world, BlockPos pos)
    {
        return world.getBlockState(pos).getMaterial().isLiquid() && !isInWater(world, pos);
    }

    /**
     * @param posA
     * @param posB
     * @param range An integer representing how far is acceptable to be considered in range
     * @return true if within range
     */
    public static boolean isWithinRange(int posA, int posB, int range)
    {
        return (Math.abs(posA - posB) <= range);
    }

    @Nullable
    public static BlockPos getSamplePosition(IWorld world, ChunkPos chunkPos)
    {
        Random random = new Random();
        int blockPosX = (chunkPos.x << 4) + random.nextInt(16);
        int blockPosZ = (chunkPos.z << 4) + random.nextInt(16);
        BlockPos searchPos = new BlockPos(blockPosX, 0, blockPosZ);

        while (searchPos.getY() < world.getHeight())
        {
            world.getBlockState(searchPos.down()).getBlock();
            if (Block.func_220055_a(world, searchPos.down(), Direction.UP))
            {
                // If the current block is air
                if (canReplace(world, searchPos))
                {
                    // If the block above this state is air,
                    if (canReplace(world, searchPos.up()))
                    {
                        // If the block we're going to place on top of is blacklisted
                        if (canPlaceOn(world, searchPos))
                        {
                            // If it's above sea level it's fine
                            if (searchPos.getY() > world.getSeaLevel())
                            {
                                return searchPos;
                            }
                            // If not, it's gotta be at least 12 blocks away from it (i.e. below it) but at least above the deposit
                            else if (isWithinRange(world.getSeaLevel(), searchPos.getY(), 12))
                            {

                                return searchPos;
                            }
                        }
                    }
                }
            }
            searchPos = searchPos.up();
        }
        return null;
    }

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

    public static boolean isLog(ItemStack stack)
    {
        if (stack.getItem() instanceof BlockItem)
        {
            BlockItem asBlockItem = (BlockItem) stack.getItem();
            if (BlockTags.LOGS.contains(asBlockItem.getBlock()))
            {
                return true;
            }
        }

        return ItemTags.LOGS.contains(stack.getItem());
    }

}