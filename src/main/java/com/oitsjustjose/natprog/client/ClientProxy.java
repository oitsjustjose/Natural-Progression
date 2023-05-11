package com.oitsjustjose.natprog.client;

import com.oitsjustjose.natprog.common.CommonProxy;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;

public class ClientProxy extends CommonProxy {
    @Override
    public void doHurtAnimation(Player player) {
        player.animateHurt(0.f);
        player.playSound(SoundEvents.PLAYER_HURT, 0.5F, 1.0F);
    }
}
