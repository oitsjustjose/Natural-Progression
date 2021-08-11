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

    private void doPlace(IWorld world, BlockPos pos) {
        for (int i = 0; i < CommonConfig.MAX_TWIGS_PER_CHUNK.get(); i++) {
            BlockPos twigPos = Utils.getPebblePos(world, new ChunkPos(pos));

            if (twigPos == null || Utils.inNonWaterFluid(world, twigPos)) {
                continue;
            }

            if (!(world.getBlockState(twigPos).getBlock() instanceof TwigBlock)) {
                boolean isInWater = Utils.isInWater(world, twigPos);
                BlockState stateToPlace = isInWater
                        ? NaturalProgressionBlocks.twigs.getDefaultState()
                                .with(TwigBlock.WATERLOGGED, Boolean.TRUE)
                        : NaturalProgressionBlocks.twigs.getDefaultState();

                if (world.setBlockState(twigPos, stateToPlace, 2 | 16)) {
                    if (Utils.canReplace(world.getBlockState(twigPos.up()), world, twigPos.up())) {
                        world.destroyBlock(pos.up(), false);
                    }
                }
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos,
            NoFeatureConfig config) {

        IWorld iworld = reader.getWorld();
        if (!(iworld instanceof ServerWorld)) {
            return false;
        }

        ServerWorld world = (ServerWorld) iworld;
        if (world.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator) {
            return false;
        }

        if (!CommonConfig.DIMENSION_WHITELIST.get().contains(Utils.dimensionToString(world))) {
            return false;
        }

        try {
            return func_207803_a(reader, rand, pos);
        } catch (Exception e) {
            NaturalProgression.getInstance().LOGGER.error(e.getMessage());
        }
        return false;
    }

    protected boolean func_207803_a(IWorld world, Random rand, BlockPos pos) {
        doPlace(world, pos);
        return true;
    }
}
