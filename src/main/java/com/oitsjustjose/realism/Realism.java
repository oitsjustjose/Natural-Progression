package com.oitsjustjose.realism;

import com.oitsjustjose.realism.common.blocks.RealismBlocks;
import com.oitsjustjose.realism.common.config.CommonConfig;
import com.oitsjustjose.realism.common.event.LogBreak;
import com.oitsjustjose.realism.common.event.PlankBreak;
import com.oitsjustjose.realism.common.items.RealismItems;
import com.oitsjustjose.realism.common.utils.Constants;
import com.oitsjustjose.realism.common.utils.PlankRecipe;
import com.oitsjustjose.realism.common.world.feature.PebbleFeature;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(Constants.MODID)
public class Realism
{
    private static Realism instance;
    public Logger LOGGER = LogManager.getLogger();
    public static final IRecipeSerializer<PlankRecipe> PLANK_SLICING = new PlankRecipe.Serializer();

    public Realism()
    {
        instance = this;

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register the setup method for modloading
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new LogBreak());
        MinecraftForge.EVENT_BUS.register(new PlankBreak());

        this.configSetup();
    }

    public static Realism getInstance()
    {
        return instance;
    }

    private void configSetup()
    {
        ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.COMMON_CONFIG);
        CommonConfig.loadConfig(CommonConfig.COMMON_CONFIG,
                FMLPaths.CONFIGDIR.get().resolve("geolosys-realism-common.toml"));
    }

    public void setup(final FMLCommonSetupEvent event)
    {
        for (Biome biome : ForgeRegistries.BIOMES.getValues())
        {
            biome.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION,
                    Biome.createDecoratedFeature(new PebbleFeature(NoFeatureConfig::deserialize), new NoFeatureConfig(),
                            Placement.NOPE, new NoPlacementConfig()));
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
            RealismBlocks.registerBlocks(blockRegistryEvent);
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent)
        {
            RealismBlocks.registerBlockItems(itemRegistryEvent);
            RealismItems.registerItems(itemRegistryEvent);
        }

        @SubscribeEvent
        public static void onRegisterSerializers(final RegistryEvent.Register<IRecipeSerializer<?>> event)
        {
            event.getRegistry()
                    .register(PLANK_SLICING.setRegistryName(new ResourceLocation(Constants.MODID, "plank_sawing")));
        }
    }
}
