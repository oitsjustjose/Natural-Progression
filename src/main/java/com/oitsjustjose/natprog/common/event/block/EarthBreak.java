package com.oitsjustjose.natprog.common.event.block;

import com.google.common.collect.Lists;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class EarthBreak {
    public static final List<Material> EARTH_MATS = Lists.newArrayList(Material.TOP_SNOW, Material.SCULK, Material.CLAY, Material.DIRT, Material.GRASS, Material.SAND, Material.SNOW, Material.MOSS, Material.VEGETABLE, Material.POWDER_SNOW);

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed evt) {
        if (!CommonConfig.MAKE_GROUND_BLOCKS_HARDER.get()) return;
        if (!EARTH_MATS.contains(evt.getState().getMaterial())) return;
        if (!evt.getEntity().getMainHandItem().isCorrectToolForDrops(evt.getState())) {
            evt.setNewSpeed(evt.getOriginalSpeed() / 4);
        }
    }
}
