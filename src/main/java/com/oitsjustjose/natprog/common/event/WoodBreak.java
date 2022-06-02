package com.oitsjustjose.natprog.common.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.oitsjustjose.natprog.NatProg;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import com.oitsjustjose.natprog.common.utils.Constants;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WoodBreak {
    final SplinterSource splinterSource = new SplinterSource();

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed event) {
        if (event.getState() == null || event.getPlayer() == null) {
            return;
        }

        ItemStack heldItem = event.getPlayer().getMainHandItem();

        if (!(event.getState().getMaterial() == Material.WOOD
                || event.getState().getMaterial() == Material.NETHER_WOOD)) {
            return;
        }

        if (event.getState().is(Constants.IGNORED_WOOD_BLOCKS)) {
            return;
        }

        if (heldItem.is(Constants.OVERRIDE_AXES)) {
            return;
        }

        if (!heldItem.canPerformAction(ToolActions.AXE_DIG)) {
            event.setCanceled(true);

            if (CommonConfig.SHOW_BREAKING_HELP.get()) {
                event.getPlayer().displayClientMessage(
                        new TranslatableComponent("natprog.wood.warning"), true);
            }

            // Random chance to even perform the hurt anim if the player is empty-handed
            if (event.getPlayer().getMainHandItem().isEmpty()
                    && event.getPlayer().getRandom().nextInt(25) == 1) {
                // And when it's shown, random chance to actually hurt from "splintering"
                if (event.getPlayer().getRandom().nextInt(10) == 1) {
                    event.getPlayer().hurt(splinterSource, 1F);
                } else {
                    NatProg.proxy.doHurtAnimation(event.getPlayer());
                }
            }
        }
    }

    public static class SplinterSource extends DamageSource {
        SplinterSource() {
            super("splintering");
        }

        @Override
        @Nullable
        public Entity getDirectEntity() {
            return null;
        }

        @Override
        @Nonnull
        public Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
            return new TranslatableComponent("natprog.splintered.to.death",
                    entityLivingBaseIn.getDisplayName());
        }
    }
}
