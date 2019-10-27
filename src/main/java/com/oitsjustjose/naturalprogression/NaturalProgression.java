package com.oitsjustjose.naturalprogression;

import com.oitsjustjose.naturalprogression.common.blocks.NaturalProgressionBlocks;
import com.oitsjustjose.naturalprogression.common.config.CommonConfig;
import com.oitsjustjose.naturalprogression.common.event.LogBreak;
import com.oitsjustjose.naturalprogression.common.event.PlankBreak;
import com.oitsjustjose.naturalprogression.common.items.NaturalProgressionItems;
import com.oitsjustjose.naturalprogression.common.recipes.PlankRecipe;
import com.oitsjustjose.naturalprogression.common.utils.Constants;
import com.oitsjustjose.naturalprogression.common.world.feature.PebbleFeature;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(Constants.MODID)
public class NaturalProgression
{
    private static NaturalProgression instance;
    public Logger LOGGER = LogManager.getLogger();
    public static final IRecipeSerializer<PlankRecipe> PLANK_SLICING = new PlankRecipe.Serializer();

    public NaturalProgression()
    {
        instance = this;

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register the setup method for modloading
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new LogBreak());
        MinecraftForge.EVENT_BUS.register(new PlankBreak());

        this.configSetup();
    }

    public static NaturalProgression getInstance()
    {
        return instance;
    }

    private void configSetup()
    {
        ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.COMMON_CONFIG);
        CommonConfig.loadConfig(CommonConfig.COMMON_CONFIG,
                FMLPaths.CONFIGDIR.get().resolve("natural-progression-common.toml"));
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

    // Handles the sound for when planks are crafted
    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event)
    {

        if (event.getPlayer() != null && !event.getPlayer().world.isRemote)
        {
            // if(ItemTags.PLANKS.contains(event.getCrafting().getItem()){
            //     event.getpla
            // }
            // if (event.getInventory().count(EnigmaticLegacy.enchantmentTransposer) == 1
            //         && event.getCrafting().getItem() == Items.ENCHANTED_BOOK)
            //     event.getPlayer().world.playSound(null, event.getPlayer().getPosition(),
            //             SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0F,
            //             (float) (0.9F + (Math.random() * 0.1F)));
        }

    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
            NaturalProgressionBlocks.registerBlocks(blockRegistryEvent);
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent)
        {
            NaturalProgressionBlocks.registerBlockItems(itemRegistryEvent);
            NaturalProgressionItems.registerItems(itemRegistryEvent);
        }

        @SubscribeEvent
        public static void onRegisterSerializers(final RegistryEvent.Register<IRecipeSerializer<?>> event)
        {
            event.getRegistry()
                    .register(PLANK_SLICING.setRegistryName(new ResourceLocation(Constants.MODID, "plank_sawing")));
        }
    }
}
