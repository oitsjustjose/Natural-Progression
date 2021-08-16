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
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class PebbleFeature extends Feature<NoFeatureConfig> {
    public PebbleFeature(Codec<NoFeatureConfig> p_i231976_1_) {
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
            for (int i = 0; i < CommonConfig.MAX_PEBBLES_PER_CHUNK.get(); i++) {
                BlockPos pebblePos = Utils.getPebblePos(reader, new ChunkPos(pos));
                if (pebblePos == null || Utils.inNonWaterFluid(reader, pebblePos)) {
                    continue;
                }

                if (!canPlaceOnBlock(reader, pebblePos)) {
                    continue;
                }

                Block pebble = Utils.getPebbleForPos(reader, pebblePos);
                boolean isInWater = Utils.isInWater(reader, pebblePos);
                BlockState stateToPlace = isInWater
                        ? pebble.getDefaultState().with(PebbleBlock.WATERLOGGED, Boolean.valueOf(true))
                        : pebble.getDefaultState();
                if (reader.setBlockState(pebblePos, stateToPlace, 2 | 16)) {
                    if (Utils.canReplace(reader, pebblePos.up())) {
                        reader.destroyBlock(pos.up(), false);
                        Utils.fixSnowyBlock(reader, pebblePos);
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
        return !CommonConfig.PEBBLE_PLACEMENT_BLACKLIST.get().contains(rl);
    }
}
