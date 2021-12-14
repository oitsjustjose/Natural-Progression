package com.oitsjustjose.natprog.common.world.feature;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.serialization.Codec;
import com.oitsjustjose.natprog.NatProg;
import com.oitsjustjose.natprog.common.blocks.NatProgBlocks;
import com.oitsjustjose.natprog.common.blocks.TwigBlock;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import com.oitsjustjose.natprog.common.utils.Utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class TwigFeature extends Feature<NoneFeatureConfiguration> {
    public TwigFeature(Codec<NoneFeatureConfiguration> p_i231976_1_) {
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
            for (int i = 0; i < CommonConfig.MAX_TWIGS_PER_CHUNK.get(); i++) {
                BlockPos twigPos = Utils.getTopLevelPlacePos(level, new ChunkPos(pos));
                if (twigPos == null || Utils.inNonWaterFluid(level, twigPos)) {
                    continue;
                }

                if (!canPlaceOnBlock(level, twigPos)) {
                    continue;
                }

                if (!(level.getBlockState(twigPos).getBlock() instanceof TwigBlock)) {
                    boolean isInWater = Utils.isInWater(level, twigPos);
                    BlockState stateToPlace = isInWater
                            ? NatProgBlocks.twigs.defaultBlockState().setValue(TwigBlock.WATERLOGGED,
                                    Boolean.TRUE)
                            : NatProgBlocks.twigs.defaultBlockState();

                    if (level.setBlock(twigPos, stateToPlace, 2 | 16)) {
                        if (Utils.canReplace(level, twigPos.above())) {
                            level.destroyBlock(pos.above(), false);
                            Utils.fixSnowyBlock(level, twigPos);
                        }
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
        return !CommonConfig.TWIG_PLACEMENT_BLACKLIST.get().contains(rl);
    }
}
