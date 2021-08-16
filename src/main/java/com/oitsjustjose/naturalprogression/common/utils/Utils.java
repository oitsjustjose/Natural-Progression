package com.oitsjustjose.naturalprogression.common.utils;

import java.util.Random;

import javax.annotation.Nullable;

import com.oitsjustjose.naturalprogression.common.blocks.NaturalProgressionBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ISeedReader;

public class Utils {
    public static Block getPebbleForPos(ISeedReader reader, BlockPos pos) {
        BlockPos search = new BlockPos(pos.getX(), reader.getHeight(), pos.getZ());
        for (int y = 0; y < search.getY(); y++) {
            if (reader.getBlockState(search.down(y)).getMaterial() == Material.AIR) {
                continue;
            }

            if (NaturalProgressionBlocks.blocksToPebbles.containsKey(reader.getBlockState(search.down(y)).getBlock())) {
                return NaturalProgressionBlocks.blocksToPebbles.get(reader.getBlockState(search.down(y)).getBlock());
            }
        }
        return NaturalProgressionBlocks.blocksToPebbles.get(Blocks.STONE);
    }

    /**
     * @param reader an ISeedReader instance
     * @param pos    A BlockPos to check in and around
     * @return true if the block is water (since we can waterlog)
     */
    public static boolean isInWater(ISeedReader reader, BlockPos pos) {
        return reader.getBlockState(pos).getBlock() == Blocks.WATER;
    }

    /**
     * @param reader an ISeedReader instance
     * @param pos    A BlockPos to check in and around
     * @return true if the block is in a non-water fluid
     */
    public static boolean inNonWaterFluid(ISeedReader reader, BlockPos pos) {
        return reader.getBlockState(pos).getMaterial().isLiquid() && !isInWater(reader, pos);
    }

    /**
     * @param reader   an ISeedReader instance
     * @param chunkPos The chunkPos to place within
     * @return A random BlockPos within the chunkPos that is valid. Can return null
     *         if no valid location is found.
     */
    @Nullable
    public static BlockPos getPebblePos(ISeedReader reader, ChunkPos chunkPos) {
        Random random = new Random();
        int blockPosX = (chunkPos.x << 4) + random.nextInt(16);
        int blockPosZ = (chunkPos.z << 4) + random.nextInt(16);

        BlockPos searchPosUp = new BlockPos(blockPosX, reader.getSeaLevel(), blockPosZ);
        BlockPos searchPosDown = new BlockPos(blockPosX, reader.getSeaLevel(), blockPosZ);

        // Try to get something _above_ sea level first
        while (searchPosUp.getY() < reader.getHeight()) {
            if (Block.hasEnoughSolidSide(reader, searchPosUp.down(), Direction.UP)) {
                if (canReplace(reader, searchPosUp) && canReplace(reader, searchPosUp.up())
                        && canPlaceOn(reader, searchPosUp)) {
                    return searchPosUp;
                }
            }
            searchPosUp = searchPosUp.up();
        }

        // If all else fails try something below sea level..
        while (searchPosDown.getY() > 0) {
            if (Block.hasEnoughSolidSide(reader, searchPosDown.down(), Direction.UP)) {
                if (canReplace(reader, searchPosDown) && canReplace(reader, searchPosDown.up())
                        && canPlaceOn(reader, searchPosDown)) {
                    return searchPosDown;
                }
            }
            searchPosDown = searchPosDown.down();
        }

        return null;
    }

    /**
     * Determines if the sample can be placed on this block
     * 
     * @param reader: an ISeedReader instance
     * @param pos:    The current searching position that will be used to confirm
     * @return true if the block below is solid on top AND isn't in the blacklist
     */
    public static boolean canPlaceOn(ISeedReader reader, BlockPos pos) {
        return Block.hasEnoughSolidSide(reader, pos.down(), Direction.UP);
    }

    /**
     * @param reader an ISeedReader instance
     * @param pos    A BlockPos to check in and around
     * @return true if the block at pos is replaceable
     */
    public static boolean canReplace(ISeedReader reader, BlockPos pos) {
        BlockState state = reader.getBlockState(pos);
        Material mat = state.getMaterial();
        return mat.isLiquid() || mat == Material.AIR || BlockTags.LEAVES.contains(state.getBlock())
                || mat.isReplaceable();
    }

    public static String dimensionToString(ISeedReader reader) {
        return reader.getWorld().getDimensionKey().getLocation().toString();
    }

    public static void fixSnowyBlock(ISeedReader reader, BlockPos posPlaced) {
        BlockState below = reader.getBlockState(posPlaced.down());
        if (below.hasProperty(BlockStateProperties.SNOWY)) {
            reader.setBlockState(posPlaced.down(), below.with(BlockStateProperties.SNOWY, Boolean.valueOf(false)),
                    2 | 16);
        }
    }
}
