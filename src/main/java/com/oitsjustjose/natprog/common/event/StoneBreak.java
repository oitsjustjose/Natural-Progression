package com.oitsjustjose.natprog.common.event;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
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

public class StoneBreak {

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed event) {
        final BrokenHandSource brokenHandSource = new BrokenHandSource();
        final List<Material> hardMaterials = Lists.newArrayList(Material.HEAVY_METAL, Material.METAL, Material.STONE);

        if (event.getState() == null || event.getPlayer() == null) {
            return;
        }

        ItemStack heldItem = event.getPlayer().getMainHandItem();

        if (!hardMaterials.contains(event.getState().getMaterial())) {
            return;
        }

        if (event.getState().is(Constants.IGNORED_STONE_BLOCKS)) {
            return;
        }

        if (heldItem.is(Constants.OVERRIDE_PICKAXES)) {
            return;
        }

        if (!heldItem.canPerformAction(ToolActions.PICKAXE_DIG)) {
            event.setCanceled(true);

            if (CommonConfig.SHOW_BREAKING_HELP.get()) {
                event.getPlayer().displayClientMessage(
                        new TranslatableComponent("natprog.stone.warning"), true);
            }
            // Random chance to even perform the hurt anim if the player is empty-handed
            if (event.getPlayer().getMainHandItem().isEmpty()
                    && event.getPlayer().getRandom().nextInt(25) == 1) {
                // And when it's shown, random chance to actually hurt from breaking bones
                if (event.getPlayer().getRandom().nextInt(2) == 1) {
                    event.getPlayer().hurt(brokenHandSource, 1F);
                } else {
                    NatProg.proxy.doHurtAnimation(event.getPlayer());
                }
            }
        }
    }

    public static class BrokenHandSource extends DamageSource {
        BrokenHandSource() {
            super("broken hand");
        }

        @Override
        @Nullable
        public Entity getDirectEntity() {
            return null;
        }

        @Override
        @Nonnull
        public Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
            return new TranslatableComponent("natural-progression.broken.bones",
                    entityLivingBaseIn.getDisplayName());
        }
    }
}
