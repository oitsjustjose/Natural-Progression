package com.oitsjustjose.natprog.common.event;

import com.google.common.collect.Lists;
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
import java.util.List;

public class StoneBreak {

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed event) {
        final BrokenHandSource brokenHandSource = new BrokenHandSource();
        final List<Material> hardMaterials = Lists.newArrayList(Material.HEAVY_METAL, Material.METAL, Material.STONE);

        if (event.getState() == null || event.getEntity() == null) {
            return;
        }

        ItemStack heldItem = event.getEntity().getMainHandItem();

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
                TranslatableContents contents = new TranslatableContents("natprog.stone.warning");
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
                // And when it's shown, random chance to actually hurt from breaking bones
                if (event.getEntity().getRandom().nextInt(2) == 1) {
                    event.getEntity().hurt(brokenHandSource, 1F);
                } else {
                    NatProg.proxy.doHurtAnimation(event.getEntity());
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
            TranslatableContents contents = new TranslatableContents("natprog.broken.bones", entityLivingBaseIn.getDisplayName());
            try {
                return contents.resolve(null, null, 0);
            } catch (CommandSyntaxException ex) {
                NatProg.getInstance().LOGGER.info(ex.getMessage());
                return Component.empty().append("Failed to resolve translation");
            }
        }
    }
}
