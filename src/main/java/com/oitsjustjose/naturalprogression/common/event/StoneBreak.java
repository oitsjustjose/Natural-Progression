package com.oitsjustjose.naturalprogression.common.event;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.oitsjustjose.naturalprogression.NaturalProgression;
import com.oitsjustjose.naturalprogression.common.config.CommonConfig;
import com.oitsjustjose.naturalprogression.common.utils.Constants;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class StoneBreak {
    final ResourceLocation OVERRIDE_RL = new ResourceLocation(Constants.MODID, "override_pickaxes");
    final ResourceLocation IGNORE_BLOCKS_RL =
            new ResourceLocation(Constants.MODID, "ignored_stone_blocks");

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed event) {
        final BrokenHandSource brokenHandSource = new BrokenHandSource();
        final List<Material> hardMaterials =
                Lists.asList(Material.ROCK, new Material[] {Material.IRON, Material.ANVIL});

        if (event.getState() == null || event.getPlayer() == null) {
            return;
        }

        ItemStack heldItem = event.getPlayer().getHeldItemMainhand();

        if (!hardMaterials.contains(event.getState().getMaterial())) {
            return;
        }

        if (ItemTags.getCollection().get(OVERRIDE_RL) != null
                && ItemTags.getCollection().get(OVERRIDE_RL).contains(heldItem.getItem())) {
            return;
        }

        if (BlockTags.getCollection().get(IGNORE_BLOCKS_RL) != null && BlockTags.getCollection()
                .get(IGNORE_BLOCKS_RL).contains(event.getState().getBlock())) {
            return;
        }

        if (!heldItem.getToolTypes().contains(ToolType.PICKAXE)) {
            event.setCanceled(true);

            if (CommonConfig.SHOW_BREAKING_HELP.get()) {
                event.getPlayer().sendStatusMessage(
                        new TranslationTextComponent("natural-progression.stone.warning"), true);
            }
            // Random chance to even perform the hurt anim if the player is empty-handed
            if (event.getPlayer().getHeldItemMainhand().isEmpty()
                    && event.getPlayer().getRNG().nextInt(25) == 1) {
                // And when it's shown, random chance to actually hurt from breaking bones
                if (event.getPlayer().getRNG().nextInt(2) == 1) {
                    event.getPlayer().attackEntityFrom(brokenHandSource, 1F);
                } else {
                    NaturalProgression.proxy.doHurtAnimation(event.getPlayer());
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
        public Entity getTrueSource() {
            return null;
        }

        @Override
        @Nonnull
        public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
            return new TranslationTextComponent("natural-progression.broken.bones",
                    entityLivingBaseIn.getDisplayName());
        }
    }
}
