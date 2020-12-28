package com.oitsjustjose.naturalprogression.client;

import com.oitsjustjose.naturalprogression.common.CommonProxy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;

public class ClientProxy extends CommonProxy {
    @Override
    public void doHurtAnimation(PlayerEntity player) {
        player.performHurtAnimation();
        player.playSound(SoundEvents.ENTITY_PLAYER_HURT, 0.5F, 1.0F);
    }
}