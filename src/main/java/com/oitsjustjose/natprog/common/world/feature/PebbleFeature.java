package com.oitsjustjose.natprog.common.world.feature;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.serialization.Codec;
import com.oitsjustjose.natprog.NatProg;
import com.oitsjustjose.natprog.common.blocks.PebbleBlock;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import com.oitsjustjose.natprog.common.utils.Utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class PebbleFeature extends Feature<NoneFeatureConfiguration> {
    public PebbleFeature(Codec<NoneFeatureConfiguration> p_i231976_1_) {
        super(p_i231976_1_);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> f) {
        if (f.chunkGenerator() instanceof FlatLevelSource) {
            return false;
        }

        WorldGenLevel level = f.level();
        BlockPos pos = f.origin();

        if (!CommonConfig.DIMENSION_WHITELIST.get().contains(Utils.dimensionToString(level))) {
            return false;
        }

        try {
            for (int i = 0; i < CommonConfig.MAX_PEBBLES_PER_CHUNK.get(); i++) {
                BlockPos pebblePos = Utils.getTopLevelPlacePos(level, new ChunkPos(pos));
                if (pebblePos == null || Utils.inNonWaterFluid(level, pebblePos)) {
                    continue;
                }

                if (!canPlaceOnBlock(level, pebblePos)) {
                    continue;
                }

                Block pebble = Utils.getPebbleForPos(level, pebblePos);
                boolean isInWater = Utils.isInWater(level, pebblePos);
                BlockState stateToPlace = isInWater
                        ? pebble.defaultBlockState().setValue(PebbleBlock.WATERLOGGED, Boolean.valueOf(true))
                        : pebble.defaultBlockState();
                if (level.setBlock(pebblePos, stateToPlace, 2 | 16)) {
                    if (Utils.canReplace(level, pebblePos.above())) {
                        level.destroyBlock(pos.above(), false);
                        Utils.fixSnowyBlock(level, pebblePos);
                    }
                }
            }
        } catch (Exception e) {
            NatProg.getInstance().LOGGER.error(e.getMessage());
        }
        return true;
    }

    private boolean canPlaceOnBlock(WorldGenLevel level, BlockPos placePos) {
        String rl = level.getBlockState(placePos.below()).getBlock().getRegistryName().toString();
        return !CommonConfig.PEBBLE_PLACEMENT_BLACKLIST.get().contains(rl);
    }
}
