package com.oitsjustjose.natprog.common.event.block;

import com.google.common.collect.Lists;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oitsjustjose.natprog.NatProg;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import com.oitsjustjose.natprog.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class StoneBreak {

    public static final TagKey<Item> CONSIDERED_AS_PICKAXE = ItemTags.create(new ResourceLocation(Constants.MOD_ID, "considered_as_pickaxe"));
    public static final TagKey<Block> IGNORED_STONE_BLOCKS = BlockTags.create(new ResourceLocation(Constants.MOD_ID, "ignored_stone_blocks"));
    public static final List<Material> HEAVY_MATS = Lists.newArrayList(Material.HEAVY_METAL, Material.METAL, Material.STONE);
    final BrokenHandSource brokenHandSource = new BrokenHandSource();

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed evt) {
        if (evt.getState() == null || evt.getEntity() == null) return;
        if (!HEAVY_MATS.contains(evt.getState().getMaterial())) return;
        if (evt.getState().is(IGNORED_STONE_BLOCKS)) return;

        var heldItem = evt.getEntity().getMainHandItem();
        if (heldItem.is(CONSIDERED_AS_PICKAXE)) return;
        if (heldItem.canPerformAction(ToolActions.PICKAXE_DIG)) return;

        evt.setCanceled(true);

        // Random chance to even perform the hurt anim if the player is empty-handed
        if (evt.getEntity().getMainHandItem().isEmpty() && evt.getEntity().getRandom().nextInt(25) == 1) {
            // And when it's shown, random chance to actually hurt from breaking bones
            if (evt.getEntity().getRandom().nextInt(2) == 1) {
                evt.getEntity().hurt(brokenHandSource, 1F);
            } else {
                NatProg.proxy.doHurtAnimation(evt.getEntity());
            }
        }

        // Show breaking help, if applicable
        if (!CommonConfig.SHOW_BREAKING_HELP.get()) return;
        try {
            var contents = new TranslatableContents("natprog.stone.warning");
            var comp = contents.resolve(null, null, 0);
            evt.getEntity().displayClientMessage(comp, true);
        } catch (CommandSyntaxException ex) {/*NOOP*/}
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
            try {
                var contents = new TranslatableContents("natprog.broken.bones", entityLivingBaseIn.getDisplayName());
                return contents.resolve(null, null, 0);
            } catch (CommandSyntaxException ex) {
                NatProg.getInstance().LOGGER.info(ex.getMessage());
                return Component.empty().append("Failed to resolve translation");
            }
        }
    }
}
