package com.oitsjustjose.naturalprogression.common.world.feature;

import java.util.Random;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.serialization.Codec;
import com.oitsjustjose.naturalprogression.NaturalProgression;
import com.oitsjustjose.naturalprogression.common.blocks.NaturalProgressionBlocks;
import com.oitsjustjose.naturalprogression.common.blocks.TwigBlock;
import com.oitsjustjose.naturalprogression.common.config.CommonConfig;
import com.oitsjustjose.naturalprogression.common.utils.Utils;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.server.ServerWorld;

public class TwigFeature extends Feature<NoFeatureConfig> {
    public TwigFeature(Codec<NoFeatureConfig> p_i231976_1_) {
        super(p_i231976_1_);
    }

    // private void doPlace(IWorld world, BlockPos pos) {
    // }

    @Override
    @ParametersAreNonnullByDefault
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos,
            NoFeatureConfig config) {
        if (generator instanceof FlatChunkGenerator) {
            return false;
        }

        if (!CommonConfig.DIMENSION_WHITELIST.get().contains(Utils.dimensionToString(reader))) {
            return false;
        }

        try {
            for (int i = 0; i < CommonConfig.MAX_TWIGS_PER_CHUNK.get(); i++) {
                BlockPos twigPos = Utils.getPebblePos(reader, new ChunkPos(pos));

                if (twigPos == null || Utils.inNonWaterFluid(reader, twigPos)) {
                    continue;
                }

                if (!(reader.getBlockState(twigPos).getBlock() instanceof TwigBlock)) {
                    boolean isInWater = Utils.isInWater(reader, twigPos);
                    BlockState stateToPlace = isInWater
                            ? NaturalProgressionBlocks.twigs.getDefaultState()
                                    .with(TwigBlock.WATERLOGGED, Boolean.TRUE)
                            : NaturalProgressionBlocks.twigs.getDefaultState();

                    if (reader.setBlockState(twigPos, stateToPlace, 2 | 16)) {
                        if (Utils.canReplace(reader.getBlockState(twigPos.up()), reader,
                                twigPos.up())) {
                            reader.destroyBlock(pos.up(), false);
                            Utils.fixSnowyBlock(reader, twigPos);
                        }
                    }
                }
            }


        } catch (Exception e) {
            NaturalProgression.getInstance().LOGGER.error(e.getMessage());
        }
        return false;
    }
}
