package com.oitsjustjose.natprog;

import com.oitsjustjose.natprog.client.ClientProxy;
import com.oitsjustjose.natprog.client.Cutouts;
import com.oitsjustjose.natprog.common.CommonProxy;
import com.oitsjustjose.natprog.common.blocks.NatProgBlocks;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import com.oitsjustjose.natprog.common.event.BoneEvent;
import com.oitsjustjose.natprog.common.event.GroundBreak;
import com.oitsjustjose.natprog.common.event.StoneBreak;
import com.oitsjustjose.natprog.common.event.ToolNeutering;
import com.oitsjustjose.natprog.common.event.WoodBreak;
import com.oitsjustjose.natprog.common.items.NatProgItems;
import com.oitsjustjose.natprog.common.recipes.DamageItemRecipe;
import com.oitsjustjose.natprog.common.utils.Constants;
import com.oitsjustjose.natprog.common.world.NatProgFeatures;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Constants.MODID)
public class NatProg {
    private static NatProg instance;

    public Logger LOGGER = LogManager.getLogger();

    public static CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static final RecipeSerializer<DamageItemRecipe> DAMAGE_ITEM_RECIPE = new DamageItemRecipe.Serializer();

    public NatProg() {
        instance = this;

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new WoodBreak());
        MinecraftForge.EVENT_BUS.register(new StoneBreak());
        MinecraftForge.EVENT_BUS.register(new GroundBreak());
        MinecraftForge.EVENT_BUS.register(new ToolNeutering());
        MinecraftForge.EVENT_BUS.register(new BoneEvent());

        this.configSetup();
    }

    public static NatProg getInstance() {
        return instance;
    }

    private void configSetup() {
        ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.COMMON_CONFIG);
        CommonConfig.loadConfig(CommonConfig.COMMON_CONFIG,
                FMLPaths.CONFIGDIR.get().resolve(Constants.MODID + "-common.toml"));
    }

    @SubscribeEvent
    public void onBiomesLoaded(BiomeLoadingEvent evt) {
        BiomeGenerationSettingsBuilder gen = evt.getGeneration();
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, NatProgFeatures.PEBBLES_PLACED);
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, NatProgFeatures.TWIGS_PLACED);
    }

    public void setupClient(final FMLClientSetupEvent event) {
        Cutouts.init();
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onFeaturesRegistry(final RegistryEvent.Register<Feature<?>> featureRegistryEvent) {
            NatProgFeatures.register(featureRegistryEvent);
        }

        @SubscribeEvent
        public static void onBlocksRegistry(
                final RegistryEvent.Register<Block> blockRegistryEvent) {
            NatProgBlocks.registerBlocks(blockRegistryEvent);
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            NatProgBlocks.registerBlockItems(itemRegistryEvent);
            NatProgItems.registerItems(itemRegistryEvent);
        }

        @SubscribeEvent
        public static void onRegisterSerializers(
                final RegistryEvent.Register<RecipeSerializer<?>> event) {
            event.getRegistry().register(DAMAGE_ITEM_RECIPE
                    .setRegistryName(new ResourceLocation(Constants.MODID, "damage_tools")));
        }
    }
}
