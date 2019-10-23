package com.oitsjustjose.realism;

import com.oitsjustjose.realism.client.ClientProxy;
import com.oitsjustjose.realism.common.CommonProxy;
import com.oitsjustjose.realism.common.blocks.RealismBlocks;
import com.oitsjustjose.realism.common.event.LogBreak;
import com.oitsjustjose.realism.common.event.PlankBreak;
import com.oitsjustjose.realism.common.items.RealismItems;
import com.oitsjustjose.realism.common.utils.Constants;
import com.oitsjustjose.realism.common.utils.PlankRecipe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MODID)
public class Realism
{
    private static Realism instance;
    public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    public Logger LOGGER = LogManager.getLogger();
    public static final IRecipeSerializer<PlankRecipe> PLANK_SLICING = new PlankRecipe.Serializer();

    public Realism()
    {
        instance = this;

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
        // TODO: setup configs
        // ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfig.CLIENT_CONFIG);
        // ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.COMMON_CONFIG);
        // CommonConfig.loadConfig(CommonConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("geolosys-common.toml"));
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
