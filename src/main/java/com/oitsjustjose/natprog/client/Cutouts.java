package com.oitsjustjose.natprog.client;

import com.oitsjustjose.natprog.common.blocks.NatProgBlocks;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;

public class Cutouts {
    public static void init() {
        ItemBlockRenderTypes.setRenderLayer(NatProgBlocks.twigs, RenderType.cutout());
    }
}
