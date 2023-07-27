package com.oitsjustjose.natprog.common.event.block;

import com.oitsjustjose.natprog.common.config.CommonConfig;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.oitsjustjose.natprog.common.Utils.GROUND;

public class EarthBreak {
    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed evt) {
        if (!CommonConfig.MAKE_GROUND_BLOCKS_HARDER.get()) return;
        if (!evt.getState().is(GROUND)) return;
        if (!evt.getEntity().getMainHandItem().isCorrectToolForDrops(evt.getState())) {
            evt.setNewSpeed(evt.getOriginalSpeed() / 4);
        }
    }
}
