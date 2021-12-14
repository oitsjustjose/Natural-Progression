package com.oitsjustjose.natprog.common.world;

import com.oitsjustjose.natprog.common.utils.Constants;
import com.oitsjustjose.natprog.common.world.feature.PebbleFeature;
import com.oitsjustjose.natprog.common.world.feature.TwigFeature;

import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class NatProgFeatures {
    private static final Feature<NoneFeatureConfiguration> PEBBLE_FEATURE = new PebbleFeature(
            NoneFeatureConfiguration.CODEC);
    private static final Feature<NoneFeatureConfiguration> TWIG_FEATURE = new TwigFeature(
            NoneFeatureConfiguration.CODEC);

    public static final ConfiguredFeature<?, ?> PEBBLES_ALL = PEBBLE_FEATURE
            .configured(NoneFeatureConfiguration.NONE);
    public static final ConfiguredFeature<?, ?> TWIGS_ALL = TWIG_FEATURE
            .configured(NoneFeatureConfiguration.NONE);

    public static PlacedFeature PEBBLES_ALL_PLACED = PEBBLES_ALL.placed(
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64),
                    VerticalAnchor.absolute(320)));
    public static PlacedFeature TWIGS_ALL_PLACED = TWIGS_ALL.placed(
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64),
                    VerticalAnchor.absolute(320)));

    public static DeferredRegister<Feature<?>> createRegistry() {
        DeferredRegister<Feature<?>> registry = DeferredRegister.create(ForgeRegistries.FEATURES, Constants.MODID);
        registry.register("pebbles", () -> PEBBLE_FEATURE);
        registry.register("twigs", () -> TWIG_FEATURE);
        return registry;
    }
}
