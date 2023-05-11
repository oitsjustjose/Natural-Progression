package com.oitsjustjose.natprog.common.event.block;

import com.google.common.collect.Lists;
import com.oitsjustjose.natprog.Constants;
import com.oitsjustjose.natprog.NatProg;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import com.oitsjustjose.natprog.common.event.DamageTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class WoodBreak {

    public static final TagKey<Item> CONSIDERED_AS_AXE = ItemTags.create(new ResourceLocation(Constants.MOD_ID, "considered_as_axe"));
    public static final TagKey<Block> IGNORED_WOOD_BLOCKS = BlockTags.create(new ResourceLocation(Constants.MOD_ID, "ignored_wood_blocks"));
    public static final List<Material> WOODY_MATS = Lists.newArrayList(Material.NETHER_WOOD, Material.WOOD);

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed evt) {
        if (evt.getState() == null || evt.getEntity() == null) return;
        if (!WOODY_MATS.contains(evt.getState().getMaterial())) return;
        if (evt.getState().is(IGNORED_WOOD_BLOCKS)) return;

        var heldItem = evt.getEntity().getMainHandItem();
        if (heldItem.is(CONSIDERED_AS_AXE)) return;
        if (heldItem.canPerformAction(ToolActions.AXE_DIG)) return;

        var level = evt.getEntity().getLevel();
        evt.setCanceled(true);

        // Random chance to even perform the hurt anim if the player is empty-handed
        if (evt.getEntity().getMainHandItem().isEmpty() && evt.getEntity().getRandom().nextInt(25) == 1) {
            // And when it's shown, random chance to actually hurt from "splintering"
            if (evt.getEntity().getRandom().nextInt(10) == 1) {
                evt.getEntity().hurt(DamageTypes.getDamageSource(level, DamageTypes.SPLINTERING), 1F);
            } else {
                NatProg.proxy.doHurtAnimation(evt.getEntity());
            }
        }

        if (!CommonConfig.SHOW_BREAKING_HELP.get()) return;
        evt.getEntity().displayClientMessage(Component.translatable("natprog.wood.warning"), true);
    }
}