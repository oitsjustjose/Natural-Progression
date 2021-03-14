package com.oitsjustjose.naturalprogression.common.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.oitsjustjose.naturalprogression.NaturalProgression;
import com.oitsjustjose.naturalprogression.common.config.CommonConfig;
import com.oitsjustjose.naturalprogression.common.utils.Constants;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WoodBreak {
    final SplinterSource splinterSource = new SplinterSource();
    final ResourceLocation OVERRIDE_RL = new ResourceLocation(Constants.MODID, "override_axes");

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed event) {
        if (event.getState() == null || event.getPlayer() == null) {
            return;
        }

        ItemStack heldItem = event.getPlayer().getHeldItemMainhand();

        if (event.getState().getMaterial() == Material.WOOD || event.getState().getMaterial() == Material.NETHER_WOOD) {
            if (ItemTags.getCollection().get(OVERRIDE_RL).contains(heldItem.getItem())) {
                return;
            }
            if (!heldItem.getToolTypes().contains(ToolType.AXE)) {
                event.setCanceled(true);

                if (CommonConfig.SHOW_BREAKING_HELP.get()) {
                    event.getPlayer()
                            .sendStatusMessage(new TranslationTextComponent("natural-progression.wood.warning"), true);
                }

                // Random chance to even perform the hurt anim if the player is empty-handed
                if (event.getPlayer().getHeldItemMainhand().isEmpty() && event.getPlayer().getRNG().nextInt(25) == 1) {
                    // And when it's shown, random chance to actually hurt from "splintering"
                    if (event.getPlayer().getRNG().nextInt(10) == 1) {
                        event.getPlayer().attackEntityFrom(splinterSource, 1F);
                    } else {
                        NaturalProgression.proxy.doHurtAnimation(event.getPlayer());
                    }
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
        public Entity getTrueSource() {
            return null;
        }

        @Override
        @Nonnull
        public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
            return new TranslationTextComponent("natural-progression.splintered.to.death",
                    entityLivingBaseIn.getDisplayName());
        }
    }
}