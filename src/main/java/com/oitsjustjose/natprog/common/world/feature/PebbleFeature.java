package com.oitsjustjose.natprog.common.world.feature;

import com.mojang.serialization.Codec;
import com.oitsjustjose.natprog.NatProg;
import com.oitsjustjose.natprog.common.blocks.PebbleBlock;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import com.oitsjustjose.natprog.common.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

public class PebbleFeature extends Feature<NoneFeatureConfiguration> {
    public PebbleFeature(Codec<NoneFeatureConfiguration> p_i231976_1_) {
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
            for (int i = 0; i < CommonConfig.MAX_PEBBLES_PER_CHUNK.get(); i++) {
                BlockPos pebblePos = Utils.getTopLevelPlacePos(level, new ChunkPos(pos));
                if (pebblePos == null || Utils.inNonWaterFluid(level, pebblePos)) {
                    continue;
                }

                if (!canPlaceOnBlock(level, pebblePos)) {
                    continue;
                }

                Block pebble = Utils.getPebbleForPos(level, pebblePos);
                BlockState stateToPlace = pebble.defaultBlockState()
                        .setValue(PebbleBlock.WATERLOGGED, Utils.isInWater(level, pebblePos));

                if (level.setBlock(pebblePos, stateToPlace, 2 | 16)) {
                    BlockPos abovePos = pebblePos.above();
                    BlockState aboveBlock = level.getBlockState(abovePos);
                    if (aboveBlock.hasProperty(DoublePlantBlock.HALF)
                            && aboveBlock.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
                        level.setBlock(abovePos, Utils.isInWater(level, abovePos) ? Blocks.WATER.defaultBlockState()
                                : Blocks.AIR.defaultBlockState(), 2 | 16);
                    }
                    Utils.fixSnowyBlock(level, pebblePos);
                }
            }
        } catch (Exception e) {
            NatProg.getInstance().LOGGER.error(e.getMessage());
        }
        return true;
    }

    private boolean canPlaceOnBlock(WorldGenLevel level, BlockPos placePos) {
        ResourceLocation rl = ForgeRegistries.BLOCKS.getKey(level.getBlockState(placePos.below()).getBlock());
        return !CommonConfig.PEBBLE_PLACEMENT_BLACKLIST.get().contains(Objects.requireNonNull(rl).toString());
    }
}
