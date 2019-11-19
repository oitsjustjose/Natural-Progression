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
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.dimension.Dimension;

public class Utils
{
    public static Block getPebbleForPos(IWorld world, BlockPos pos)
    {
        BlockPos search = new BlockPos(pos.getX(), world.getHeight(), pos.getZ());
        for (int y = 0; y < search.getY(); y++)
        {
            if (world.getBlockState(search.down(y)).getMaterial() == Material.AIR)
            {
                continue;
            }

            if (NaturalProgressionBlocks.blocksToPebbles.containsKey(world.getBlockState(search.down(y)).getBlock()))
            {
                return NaturalProgressionBlocks.blocksToPebbles.get(world.getBlockState(search.down(y)).getBlock());
            }
        }
        return NaturalProgressionBlocks.stonePebble;
    }

    /**
     * @param world an IWorld instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block at pos is replaceable
     */
    private static boolean canReplace(BlockState stateAtPos, IWorld world, BlockPos searchPos)
    {
        if (stateAtPos.getMaterial().isReplaceable() || stateAtPos.getMaterial() == Material.AIR
                || stateAtPos.isFoliage(world, searchPos))
        {
            return true;
        }

        if (stateAtPos.getMaterial().isLiquid() && stateAtPos.getBlock() == Blocks.WATER)
        {
            return true;
        }

        if (stateAtPos.has(BlockStateProperties.WATERLOGGED))

        {
            return true;
        }
        return false;
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
        return world.getBlockState(pos) instanceof ILiquidContainer;
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
     * 
     * @param world    an IWorld instance
     * @param chunkPos The chunkPos to place within
     * @return A random BlockPos within the chunkPos that is valid. Can return null if no valid location is found.
     */
    @Nullable
    public static BlockPos getPebblePos(IWorld world, ChunkPos chunkPos)
    {
        Random random = new Random();
        int blockPosX = (chunkPos.x << 4) + random.nextInt(16);
        int blockPosZ = (chunkPos.z << 4) + random.nextInt(16);
        BlockPos searchPos = new BlockPos(blockPosX, world.getHeight(), blockPosZ);

        for (int i = 0; i < searchPos.getY(); i++)
        {
            BlockState stateAtPos = world.getBlockState(searchPos.down(i));
            BlockState stateBelow = world.getBlockState(searchPos.down(i + 1));

            if (canReplace(stateAtPos, world, searchPos))
            {
                if (stateBelow.isSolid() && stateBelow.getMaterial().blocksMovement())
                {
                    return searchPos.down(i);
                }
            }
        }

        return null;
    }

    public static String dimensionToString(Dimension dim)
    {
        return Objects.requireNonNull(Objects.requireNonNull(dim.getType().getRegistryName()).toString());
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

    public static ItemStack getStrippedLog(Item wood)
    {
        for (Item log : ItemTags.LOGS.getAllElements())
        {
            if (wood.getRegistryName() == log.getRegistryName())
            {
                continue;
            }
            if (log.getRegistryName().getPath().replace("stripped_", "")
                    .equalsIgnoreCase(wood.getRegistryName().getPath()))
            {
                return new ItemStack(log);
            }
        }
        return ItemStack.EMPTY;
    }

}