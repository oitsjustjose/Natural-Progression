package com.oitsjustjose.natprog.common.event;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oitsjustjose.natprog.NatProg;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import com.oitsjustjose.natprog.common.utils.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WoodBreak {
    final SplinterSource splinterSource = new SplinterSource();

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed event) {
        if (event.getState() == null || event.getEntity() == null) {
            return;
        }

        ItemStack heldItem = event.getEntity().getMainHandItem();

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
                TranslatableContents contents = new TranslatableContents("natprog.wood.warning");
                MutableComponent comp;
                try {
                    comp = contents.resolve(null, null, 0);
                } catch (CommandSyntaxException ex) {
                    NatProg.getInstance().LOGGER.info(ex.getMessage());
                    comp = Component.empty().append("Failed to resolve translation");
                }
                event.getEntity().displayClientMessage(comp, true);
            }

            // Random chance to even perform the hurt anim if the player is empty-handed
            if (event.getEntity().getMainHandItem().isEmpty()
                    && event.getEntity().getRandom().nextInt(25) == 1) {
                // And when it's shown, random chance to actually hurt from "splintering"
                if (event.getEntity().getRandom().nextInt(10) == 1) {
                    event.getEntity().hurt(splinterSource, 1F);
                } else {
                    NatProg.proxy.doHurtAnimation(event.getEntity());
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
            TranslatableContents contents = new TranslatableContents("natprog.splintered.to.death", entityLivingBaseIn.getDisplayName());
            try {
                return contents.resolve(null, null, 0);
            } catch (CommandSyntaxException ex) {
                NatProg.getInstance().LOGGER.info(ex.getMessage());
                return Component.empty().append("Failed to resolve translation");
            }
        }
    }
}
