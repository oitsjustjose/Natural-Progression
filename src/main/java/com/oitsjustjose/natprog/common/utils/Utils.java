package com.oitsjustjose.natprog.common.utils;

import com.oitsjustjose.natprog.NatProg;
import com.oitsjustjose.natprog.common.blocks.PebbleBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;

public class Utils {

    public static Block getPebbleForPos(WorldGenLevel level, BlockPos pos) {
        BlockPos search = new BlockPos(pos.getX(), level.getHeight(), pos.getZ());
        for (int y = 0; y < search.getY(); y++) {
            if (level.getBlockState(search.below(y)).getMaterial() == Material.AIR) {
                continue;
            }

            for (var rl : NatProg.getInstance().REGISTRY.PebbleMaterials) {
                String generated = (rl.getNamespace().equals("minecraft") ? "" : rl.getNamespace() + "_") + rl.getPath() + "_pebble";

                PebbleBlock b = (PebbleBlock) ForgeRegistries.BLOCKS.getValue(new ResourceLocation(Constants.MODID, generated));
                Block parent = b.getParentBlock();
                if (parent != null && parent == level.getBlockState(search.below(y)).getBlock()) {
                    return b;
                }
            }
        }

        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(Constants.MODID, "stone_pebble"));
    }

    /**
     * @param level an ISeedReader instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block is water (since we can waterlog)
     */
    public static boolean isInWater(WorldGenLevel level, BlockPos pos) {
        return level.getBlockState(pos).getBlock() == Blocks.WATER;
    }

    /**
     * @param level an ISeedReader instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block is in a non-water fluid
     */
    public static boolean inNonWaterFluid(WorldGenLevel level, BlockPos pos) {
        return level.getBlockState(pos).getMaterial().isLiquid() && !isInWater(level, pos);
    }

    @Nullable
    public static BlockPos getTopLevelPlacePos(WorldGenLevel level, ChunkPos chunkPos) {
        return getTopLevelPlacePos(level, chunkPos, -1);
    }

    @Nullable
    public static BlockPos getTopLevelPlacePos(WorldGenLevel level, ChunkPos chunkPos, int spread) {

        if (!(level instanceof WorldGenRegion world)) {
            return null;
        }

        int usedSpread = Math.max(8, spread);
        int xCenter = (chunkPos.getMinBlockX() + chunkPos.getMaxBlockX()) / 2;
        int zCenter = (chunkPos.getMinBlockZ() + chunkPos.getMaxBlockZ()) / 2;

        // Only put things in the negative X|Z if the spread is provided.
        int blockPosX = xCenter
                + (level.getRandom().nextInt(usedSpread) * ((level.getRandom().nextBoolean()) ? 1 : -1));
        int blockPosZ = zCenter
                + (level.getRandom().nextInt(usedSpread) * ((level.getRandom().nextBoolean()) ? 1 : -1));

        if (!world.hasChunk(chunkPos.x, chunkPos.z)) {
            return null;
        }

        BlockPos searchPos = new BlockPos(blockPosX, world.getHeight(), blockPosZ);

        // With worlds being so much deeper,
        // it makes most sense to take a top-down approach
        while (searchPos.getY() > world.getMinBuildHeight()) {
            // BlockState blockToPlaceOn = world.getBlockState(searchPos);
            // Check if the location itself is solid
            if (canPlaceOn(level, searchPos)) {
                // Then check if the block above it is either air, or replacable
                BlockPos actualPlacePos = searchPos.above();
                if (canReplace(world, actualPlacePos)) {
                    return actualPlacePos;
                }
            }
            searchPos = searchPos.below();
        }

        return null;
    }

    /**
     * Determines if the sample can be placed on this block
     *
     * @param level: A WorldGenLevel instance
     * @param pos:   The current searching position that will be used to confirm
     * @return true if the block below is solid on top AND isn't in the blacklist
     */
    public static boolean canPlaceOn(WorldGenLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return Block.isShapeFullBlock(state.getShape(level, pos)) && state.is(Constants.GROUND);
    }

    /**
     * @param level an ISeedReader instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block at pos is replaceable
     */
    public static boolean canReplace(WorldGenLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        Material mat = state.getMaterial();
        return mat.isLiquid() || mat == Material.AIR || state.is(BlockTags.LEAVES)
                || mat.isReplaceable();
    }

    public static String dimensionToString(WorldGenLevel level) {
        return level.getLevel().dimension().location().toString();
    }

    public static void fixSnowyBlock(WorldGenLevel level, BlockPos posPlaced) {
        BlockState below = level.getBlockState(posPlaced.below());
        if (below.hasProperty(BlockStateProperties.SNOWY)) {
            level.setBlock(posPlaced.below(), below.setValue(BlockStateProperties.SNOWY, Boolean.FALSE),
                    2 | 16);
        }
    }
}
