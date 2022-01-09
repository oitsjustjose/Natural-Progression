package com.oitsjustjose.naturalprogression.common.world.feature;

import java.util.Random;
import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.serialization.Codec;
import com.oitsjustjose.naturalprogression.NaturalProgression;
import com.oitsjustjose.naturalprogression.common.blocks.NaturalProgressionBlocks;
import com.oitsjustjose.naturalprogression.common.blocks.PebbleBlock;
import com.oitsjustjose.naturalprogression.common.blocks.TwigBlock;
import com.oitsjustjose.naturalprogression.common.config.CommonConfig;
import com.oitsjustjose.naturalprogression.common.utils.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class TwigFeature extends Feature<NoFeatureConfig> {
    public TwigFeature(Codec<NoFeatureConfig> p_i231976_1_) {
        super(p_i231976_1_);
    }

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

                if (!canPlaceOnBlock(reader, twigPos)) {
                    continue;
                }

                if (!(reader.getBlockState(twigPos).getBlock() instanceof TwigBlock)) {
                    BlockState stateToPlace = NaturalProgressionBlocks.twigs.getDefaultState()
                            .with(PebbleBlock.WATERLOGGED, Utils.isInWater(reader, twigPos));

                    if (reader.setBlockState(twigPos, stateToPlace, 2 | 16)) {
                        BlockState aboveBlock = reader.getBlockState(twigPos.up());
                        if (aboveBlock.hasProperty(BlockStateProperties.HALF) && aboveBlock.get(BlockStateProperties.HALF) == Half.TOP) {
                            reader.destroyBlock(twigPos.up(), false);
                        }
                        Utils.fixSnowyBlock(reader, twigPos);
                    }
                }
            }
        } catch (Exception e) {
            NaturalProgression.getInstance().LOGGER.error(e.getMessage());
        }
        return true;
    }

    private boolean canPlaceOnBlock(ISeedReader reader, BlockPos placePos) {
        String rl = reader.getBlockState(placePos.down()).getBlock().getRegistryName().toString();
        return !CommonConfig.TWIG_PLACEMENT_BLACKLIST.get().contains(rl);
    }
}
