package com.oitsjustjose.naturalprogression.common.world.feature;

import java.util.Random;
import java.util.function.Function;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.datafixers.Dynamic;
import com.oitsjustjose.naturalprogression.NaturalProgression;
import com.oitsjustjose.naturalprogression.common.blocks.NaturalProgressionBlocks;
import com.oitsjustjose.naturalprogression.common.blocks.TwigBlock;
import com.oitsjustjose.naturalprogression.common.config.CommonConfig;
import com.oitsjustjose.naturalprogression.common.utils.Utils;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class TwigFeature extends Feature<NoFeatureConfig>
{
    public TwigFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
    {
        super(configFactoryIn);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand,
            BlockPos pos, NoFeatureConfig config)
    {
        if (!CommonConfig.DIMENSION_WHITELIST.get().contains(Utils.dimensionToString(world.getDimension())))
        {
            return false;
        }

        boolean placed = false;

        try
        {
            if (world.getWorld().getWorldType() != WorldType.FLAT)
            {
                for (int i = 0; i < CommonConfig.MAX_TWIGS_PER_CHUNK.get(); i++)
                {
                    BlockPos twigPos = Utils.getPebblePos(world, new ChunkPos(pos));

                    if (twigPos == null || Utils.inNonWaterFluid(world, twigPos))
                    {
                        continue;
                    }

                    if (!(world.getBlockState(twigPos).getBlock() instanceof TwigBlock))
                    {
                        boolean isInWater = Utils.isInWater(world, twigPos);
                        BlockState stateToPlace = isInWater
                                ? NaturalProgressionBlocks.twigs.getDefaultState().with(TwigBlock.WATERLOGGED,
                                        Boolean.TRUE)
                                : NaturalProgressionBlocks.twigs.getDefaultState();

                        if (world.setBlockState(twigPos, stateToPlace, 2 | 16))
                        {
                            if (Utils.canReplace(world.getBlockState(twigPos.up()), world, twigPos.up()))
                            {
                                world.destroyBlock(pos.up(), false);
                            }
                            placed = true;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            NaturalProgression.getInstance().LOGGER.error(e.getMessage());
            return false;
        }

        return placed;
    }
}