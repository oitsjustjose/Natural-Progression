package com.oitsjustjose.natprog.common.world.feature;

import com.mojang.serialization.Codec;
import com.oitsjustjose.natprog.Constants;
import com.oitsjustjose.natprog.NatProg;
import com.oitsjustjose.natprog.common.Utils;
import com.oitsjustjose.natprog.common.blocks.PebbleBlock;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import javax.annotation.ParametersAreNonnullByDefault;

public class PebbleFeature extends Feature<NoneFeatureConfiguration> {
    public static final TagKey<Block> WONT_SUPPORT_PEBBLE = BlockTags.create(new ResourceLocation(Constants.MOD_ID, "wont_support_pebble"));

    public PebbleFeature(Codec<NoneFeatureConfiguration> p_i231976_1_) {
        super(p_i231976_1_);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> f) {
        if (f.chunkGenerator() instanceof FlatLevelSource) return false;

        var level = f.level();
        var pos = f.origin();

        try {
            for (var i = 0; i < CommonConfig.MAX_PEBBLES_PER_CHUNK.get(); i++) {
                var pebblePos = Utils.getTopLevelPlacePos(level, new ChunkPos(pos));
                if (pebblePos == null || Utils.inNonWaterFluid(level, pebblePos)) continue;
                if (level.getBlockState(pebblePos).is(WONT_SUPPORT_PEBBLE)) continue;

                var pebble = Utils.getPebbleForPos(level, pebblePos);
                var stateToPlace = pebble.defaultBlockState().setValue(PebbleBlock.WATERLOGGED, Utils.isInWater(level, pebblePos));

                if (level.setBlock(pebblePos, stateToPlace, 2 | 16)) {
                    var posAbove = pebblePos.above();
                    var blockAbove = level.getBlockState(posAbove);
                    if (blockAbove.hasProperty(DoublePlantBlock.HALF) && blockAbove.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
                        level.setBlock(posAbove, Utils.isInWater(level, posAbove) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 2 | 16);
                    }
                    Utils.fixSnowyBlock(level, pebblePos);
                }
            }
        } catch (Exception e) {
            NatProg.getInstance().LOGGER.error(e.getMessage());
        }
        return true;
    }
}
