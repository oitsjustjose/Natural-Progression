package com.oitsjustjose.realism.client;

import com.oitsjustjose.realism.common.CommonProxy;

import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    public void init()
    {
        // TODO: write up the manual screen before init'ing this
        // ManualScreen.initPages();

    }

    @Override
    public void registerClientSubscribeEvent(Object o)
    {
        MinecraftForge.EVENT_BUS.register(o);
    }
}