package com.oitsjustjose.naturalprogression.common.world.feature;

import java.util.Random;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.serialization.Codec;
import com.oitsjustjose.naturalprogression.NaturalProgression;
import com.oitsjustjose.naturalprogression.common.blocks.PebbleBlock;
import com.oitsjustjose.naturalprogression.common.config.CommonConfig;
import com.oitsjustjose.naturalprogression.common.utils.Utils;

import net.minecraft.block.Block;
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

public class PebbleFeature extends Feature<NoFeatureConfig> {
    public PebbleFeature(Codec<NoFeatureConfig> p_i231976_1_) {
        super(p_i231976_1_);
    }

    private void doPlace(IWorld world, BlockPos pos) {
        for (int i = 0; i < CommonConfig.MAX_PEBBLES_PER_CHUNK.get(); i++) {
            BlockPos pebblePos = Utils.getPebblePos(world, new ChunkPos(pos));

            if (pebblePos == null || Utils.inNonWaterFluid(world, pebblePos)) {
                continue;
            }

            Block pebble = Utils.getPebbleForPos(world, pebblePos);

            if (!(world.getBlockState(pebblePos).getBlock() instanceof PebbleBlock)) {
                boolean isInWater = Utils.isInWater(world, pebblePos);
                BlockState stateToPlace = isInWater
                        ? pebble.getDefaultState().with(PebbleBlock.WATERLOGGED, Boolean.TRUE)
                        : pebble.getDefaultState();

                if (world.setBlockState(pebblePos, stateToPlace, 2 | 16)) {
                    if (Utils.canReplace(world.getBlockState(pebblePos.up()), world,
                            pebblePos.up())) {
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
