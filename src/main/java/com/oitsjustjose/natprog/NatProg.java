package com.oitsjustjose.natprog;

import com.oitsjustjose.natprog.client.ClientProxy;
import com.oitsjustjose.natprog.common.CommonProxy;
import com.oitsjustjose.natprog.common.Registry;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import com.oitsjustjose.natprog.common.event.*;
import com.oitsjustjose.natprog.common.event.block.EarthBreak;
import com.oitsjustjose.natprog.common.event.block.StoneBreak;
import com.oitsjustjose.natprog.common.event.block.WoodBreak;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Constants.MOD_ID)
public class NatProg {
    private static NatProg instance;
    public Logger LOGGER = LogManager.getLogger();
    public static CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    public final Registry REGISTRY;

    public NatProg() {
        instance = this;

        REGISTRY = new Registry();
        REGISTRY.RegisterAll(FMLJavaModLoadingContext.get());

        MinecraftForge.EVENT_BUS.register(new WoodBreak());
        MinecraftForge.EVENT_BUS.register(new StoneBreak());
        MinecraftForge.EVENT_BUS.register(new EarthBreak());
        MinecraftForge.EVENT_BUS.register(new ToolNeutering());
        MinecraftForge.EVENT_BUS.register(new BoneEvent());
        MinecraftForge.EVENT_BUS.register(new TwigPlacement());

        this.configSetup();
    }

    public static NatProg getInstance() {
        return instance;
    }

    private void configSetup() {
        ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.COMMON_CONFIG);
        CommonConfig.loadConfig(CommonConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(Constants.MOD_ID + "-common.toml"));
    }
}
