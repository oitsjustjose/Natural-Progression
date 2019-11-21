package com.oitsjustjose.naturalprogression;

import com.oitsjustjose.naturalprogression.client.ClientProxy;
import com.oitsjustjose.naturalprogression.common.CommonProxy;
import com.oitsjustjose.naturalprogression.common.blocks.NaturalProgressionBlocks;
import com.oitsjustjose.naturalprogression.common.config.CommonConfig;
import com.oitsjustjose.naturalprogression.common.config.CommonConfig.CraftingSounds;
import com.oitsjustjose.naturalprogression.common.event.StoneBreak;
import com.oitsjustjose.naturalprogression.common.event.ToolTips;
import com.oitsjustjose.naturalprogression.common.event.WoodBreak;
import com.oitsjustjose.naturalprogression.common.event.WoodenTools;
import com.oitsjustjose.naturalprogression.common.items.NaturalProgressionItems;
import com.oitsjustjose.naturalprogression.common.recipes.DamageItemRecipe;
import com.oitsjustjose.naturalprogression.common.utils.Constants;
import com.oitsjustjose.naturalprogression.common.utils.Sounds;
import com.oitsjustjose.naturalprogression.common.world.feature.PebbleFeature;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Constants.MODID) public class NaturalProgression
{
	private static NaturalProgression instance;

	public Logger LOGGER = LogManager.getLogger();

	public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

	public static final IRecipeSerializer<DamageItemRecipe> DAMAGE_ITEM_RECIPE = new DamageItemRecipe.Serializer();

	public NaturalProgression()
	{
		instance = this;

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

		// Register the setup method for modloading
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new WoodBreak());
		MinecraftForge.EVENT_BUS.register(new StoneBreak());
		MinecraftForge.EVENT_BUS.register(new ToolTips());
		MinecraftForge.EVENT_BUS.register(new WoodenTools());

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

	// Handles the sound for when planks or items are crafted
	@SubscribeEvent public void onItemCrafted(PlayerEvent.ItemCraftedEvent event)
	{
		if (CommonConfig.CRAFTING_SOUNDS.get() == CraftingSounds.NONE)
		{
			return;
		}

		if (event.getPlayer() != null)
		{
			PlayerEntity player = event.getPlayer();
			float pitch = Math.min(1.0F, 0.5F + event.getPlayer().getRNG().nextFloat());

			if (ItemTags.PLANKS.contains(event.getCrafting().getItem()))
			{
				player.playSound(Sounds.CRAFT_PLANK, 0.85F, pitch);
			}
			else if (CommonConfig.CRAFTING_SOUNDS.get() == CraftingSounds.ALL)
			{
				player.playSound(Sounds.CRAFT_OTHER, 0.85F, pitch);
			}
		}
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD) public static class RegistryEvents
	{
		@SubscribeEvent public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
		{
			NaturalProgressionBlocks.registerBlocks(blockRegistryEvent);
		}

		@SubscribeEvent public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent)
		{
			NaturalProgressionBlocks.registerBlockItems(itemRegistryEvent);
			NaturalProgressionItems.registerItems(itemRegistryEvent);
		}

		@SubscribeEvent public static void onRegisterSerializers(
				final RegistryEvent.Register<IRecipeSerializer<?>> event)
		{
			event.getRegistry().register(
					DAMAGE_ITEM_RECIPE.setRegistryName(new ResourceLocation(Constants.MODID, "damage_tools")));
		}
	}
}
