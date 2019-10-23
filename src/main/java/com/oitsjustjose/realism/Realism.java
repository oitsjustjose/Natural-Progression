package com.oitsjustjose.realism;

import com.oitsjustjose.realism.client.ClientProxy;
import com.oitsjustjose.realism.common.CommonProxy;
import com.oitsjustjose.realism.common.blocks.RealismBlocks;
import com.oitsjustjose.realism.common.utils.Constants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
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

    public Realism()
    {
        instance = this;

        // Register the setup method for modloading
        MinecraftForge.EVENT_BUS.register(this);

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
        }
    }
}
