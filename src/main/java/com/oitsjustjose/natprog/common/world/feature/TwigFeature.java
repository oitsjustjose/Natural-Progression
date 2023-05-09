package com.oitsjustjose.natprog.common.world.feature;

import com.mojang.serialization.Codec;
import com.oitsjustjose.natprog.Constants;
import com.oitsjustjose.natprog.NatProg;
import com.oitsjustjose.natprog.common.Utils;
import com.oitsjustjose.natprog.common.blocks.TwigBlock;
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

public class TwigFeature extends Feature<NoneFeatureConfiguration> {
    public static final TagKey<Block> WONT_SUPPORT_TWIG = BlockTags.create(new ResourceLocation(Constants.MOD_ID, "wont_support_twig"));

    public TwigFeature(Codec<NoneFeatureConfiguration> p_i231976_1_) {
        super(p_i231976_1_);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> f) {
        if (f.chunkGenerator() instanceof FlatLevelSource) return false;

        var level = f.level();
        var pos = f.origin();

        try {
            for (int i = 0; i < CommonConfig.MAX_TWIGS_PER_CHUNK.get(); i++) {
                var twigPos = Utils.getTopLevelPlacePos(level, new ChunkPos(pos));
                if (twigPos == null || Utils.inNonWaterFluid(level, twigPos)) continue;
                if (level.getBlockState(twigPos).is(WONT_SUPPORT_TWIG)) continue;

                var twig = NatProg.getInstance().REGISTRY.twigBlock.get();
                var stateToPlace = twig.defaultBlockState().setValue(TwigBlock.WATERLOGGED, Utils.isInWater(level, twigPos));

                if (level.setBlock(twigPos, stateToPlace, 2 | 16)) {
                    var posAbove = twigPos.above();
                    var blockAbove = level.getBlockState(posAbove);
                    if (blockAbove.hasProperty(DoublePlantBlock.HALF) && blockAbove.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
                        level.setBlock(posAbove, Utils.isInWater(level, posAbove) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 2 | 16);
                    }
                    Utils.fixSnowyBlock(level, twigPos);
                }
            }
        } catch (Exception e) {
            NatProg.getInstance().LOGGER.error(e.getMessage());
        }
        return true;
    }
}
