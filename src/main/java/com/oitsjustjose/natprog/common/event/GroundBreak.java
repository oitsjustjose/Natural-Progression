package com.oitsjustjose.natprog.common.event;

import com.oitsjustjose.natprog.common.config.CommonConfig;

import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GroundBreak {
    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed event) {
        if (event.getState().getMaterial() == Material.DIRT
                || event.getState().getMaterial() == Material.SAND
                || event.getState().getMaterial() == Material.GRASS) {
            if (!CommonConfig.MAKE_GROUND_BLOCKS_HARDER.get()) {
                return;
            }
            // If NOT holding a shovel
            if (!event.getEntity().getMainHandItem().isCorrectToolForDrops(event.getState())) {
                event.setNewSpeed(event.getOriginalSpeed() / 4);
            }
        }
    }
}
