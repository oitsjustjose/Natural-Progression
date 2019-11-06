package com.oitsjustjose.naturalprogression.common.world.feature;

import java.util.Random;
import java.util.function.Function;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.datafixers.Dynamic;
import com.oitsjustjose.naturalprogression.common.blocks.PebbleBlock;
import com.oitsjustjose.naturalprogression.common.config.CommonConfig;
import com.oitsjustjose.naturalprogression.common.utils.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class PebbleFeature extends Feature<NoFeatureConfig>
{
    public PebbleFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
    {
        super(configFactoryIn, true);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand,
            BlockPos pos, NoFeatureConfig config)
    {
        boolean placed = false;

        if (CommonConfig.DIMENSION_BLACKLIST.get().contains(Utils.dimensionToString(world.getDimension())))
        {
            return false;
        }

        if (world.getWorld().getWorldType() != WorldType.FLAT)
        {
            for (int i = 0; i < CommonConfig.MAX_PEBBLES_PER_CHUNK.get(); i++)
            {
                BlockPos pebblePos = Utils.getSamplePosition(world, new ChunkPos(pos));

                if (pebblePos == null || Utils.inNonWaterFluid(world, pebblePos))
                {
                    continue;
                }

                Block pebble = Utils.getPebbleForPos(world, pebblePos);
                
                if (world.getBlockState(pebblePos).getBlock() != pebble)
                {
                    boolean isInWater = Utils.isInWater(world, pebblePos);
                    BlockState stateToPlace = isInWater
                            ? pebble.getDefaultState().with(PebbleBlock.WATERLOGGED, Boolean.TRUE)
                            : pebble.getDefaultState();

                    if (world.setBlockState(pebblePos, stateToPlace, 2 | 16))
                    {
                        // Clean up the blocks *around* the pebble
                        stateToPlace.updateNeighbors(world, pebblePos, 0);
                        stateToPlace.updateNeighbors(world, pebblePos.up(), 0);
                        placed = true;
                    }
                }
            }
        }

        return placed;
    }
}