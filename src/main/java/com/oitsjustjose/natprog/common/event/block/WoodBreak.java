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

public class WoodBreak {

    public static final TagKey<Item> CONSIDERED_AS_AXE = ItemTags.create(new ResourceLocation(Constants.MOD_ID, "considered_as_axe"));
    public static final TagKey<Block> IGNORED_WOOD_BLOCKS = BlockTags.create(new ResourceLocation(Constants.MOD_ID, "ignored_wood_blocks"));
    public static final List<Material> WOODY_MATS = Lists.newArrayList(Material.NETHER_WOOD, Material.WOOD);
    final SplinterSource splinterSource = new SplinterSource();

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed evt) {
        if (evt.getState() == null || evt.getEntity() == null) return;
        if (!WOODY_MATS.contains(evt.getState().getMaterial())) return;
        if (evt.getState().is(IGNORED_WOOD_BLOCKS)) return;

        var heldItem = evt.getEntity().getMainHandItem();
        if (heldItem.is(CONSIDERED_AS_AXE)) return;
        if (heldItem.canPerformAction(ToolActions.AXE_DIG)) return;


        evt.setCanceled(true);

        // Random chance to even perform the hurt anim if the player is empty-handed
        if (evt.getEntity().getMainHandItem().isEmpty() && evt.getEntity().getRandom().nextInt(25) == 1 && CommonConfig.INCORRECT_TOOL_DAMAGE.get()) {
            // And when it's shown, random chance to actually hurt from "splintering"
            if (evt.getEntity().getRandom().nextInt(10) == 1) {
                evt.getEntity().hurt(splinterSource, 1F);
            } else {
                NatProg.proxy.doHurtAnimation(evt.getEntity());
            }
        }

        if (!CommonConfig.SHOW_BREAKING_HELP.get()) return;
        try {
            var contents = new TranslatableContents("natprog.wood.warning");
            var comp = contents.resolve(null, null, 0);
            evt.getEntity().displayClientMessage(comp, true);
        } catch (CommandSyntaxException ex) {/*NOOP*/}
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
            try {
                var contents = new TranslatableContents("natprog.splintered.to.death", entityLivingBaseIn.getDisplayName());
                return contents.resolve(null, null, 0);
            } catch (CommandSyntaxException ex) {
                NatProg.getInstance().LOGGER.info(ex.getMessage());
                return Component.empty().append("Failed to resolve translation");
            }
        }
    }
}
