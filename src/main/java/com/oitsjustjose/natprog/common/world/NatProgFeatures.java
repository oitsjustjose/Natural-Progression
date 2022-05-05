package com.oitsjustjose.natprog.common.world;

import com.oitsjustjose.natprog.common.world.feature.PebbleFeature;
import com.oitsjustjose.natprog.common.world.feature.TwigFeature;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.RegistryEvent;

public class NatProgFeatures {
    private static final Feature<NoneFeatureConfiguration> PEBBLE_FEATURE = new PebbleFeature(
            NoneFeatureConfiguration.CODEC).withRegistryName("pebbles");
    private static final Feature<NoneFeatureConfiguration> TWIG_FEATURE = new TwigFeature(
            NoneFeatureConfiguration.CODEC).withRegistryName("twigs");

    public static Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> PEBBLES_ALL = FeatureUtils
            .register(PEBBLE_FEATURE.getRegistryName().toString(), PEBBLE_FEATURE);
    public static Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> TWIGS_ALL = FeatureUtils
            .register(TWIG_FEATURE.getRegistryName().toString(), TWIG_FEATURE);

    public static Holder<PlacedFeature> PEBBLES_PLACED = PlacementUtils.register(
            PEBBLE_FEATURE.getRegistryName().toString(), PEBBLES_ALL,
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(320)));
    public static Holder<PlacedFeature> TWIGS_PLACED = PlacementUtils.register(
            TWIG_FEATURE.getRegistryName().toString(), TWIGS_ALL,
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(320)));

    public static void register(final RegistryEvent.Register<Feature<?>> featureRegistryEvent) {
        featureRegistryEvent.getRegistry().register(PEBBLE_FEATURE);
        featureRegistryEvent.getRegistry().register(TWIG_FEATURE);
    }
}
