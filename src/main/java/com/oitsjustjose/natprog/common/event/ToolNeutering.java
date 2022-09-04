package com.oitsjustjose.natprog.common.event;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ToolNeutering {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onHover(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        if (!(item instanceof TieredItem tiered)) {
            return;
        }

        if ((tiered.getTier() == Tiers.WOOD) && CommonConfig.REMOVE_WOODEN_TOOL_FUNC.get()) {
            try {
                TranslatableContents content = new TranslatableContents("natprog.too.brittle");
                event.getToolTip().add(content.resolve(null, null, 0));
            } catch (CommandSyntaxException ex) { /*NOOP*/ }
            return;
        }
        if ((tiered.getTier() == Tiers.STONE) && CommonConfig.REMOVE_STONE_TOOL_FUNC.get()) {
            try {
                TranslatableContents content = new TranslatableContents("natprog.too.blunt");
                event.getToolTip().add(content.resolve(null, null, 0));
            } catch (CommandSyntaxException ex) { /*NOOP*/ }
            return;
        }
    }

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed event) {
        if (!CommonConfig.TOOL_NEUTERING.get()) return;

        if (event.getState() == null || event.getEntity() == null) {
            return;
        }

        Item heldItem = event.getEntity().getMainHandItem().getItem();
        if (!(heldItem instanceof TieredItem tiered)) {
            return;
        }

        boolean cancelWood = (tiered.getTier() == Tiers.WOOD) && CommonConfig.REMOVE_WOODEN_TOOL_FUNC.get();
        boolean cancelStone = (tiered.getTier() == Tiers.STONE) && CommonConfig.REMOVE_STONE_TOOL_FUNC.get();

        if (cancelWood || cancelStone) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void registerEvent(AttackEntityEvent event) {
        if (event.getEntity() == null) {
            return;
        }

        Item heldItem = event.getEntity().getMainHandItem().getItem();
        if (!(heldItem instanceof TieredItem tiered)) {
            return;
        }

        boolean cancelWood = (tiered.getTier() == Tiers.WOOD) && CommonConfig.REMOVE_WOODEN_TOOL_FUNC.get();
        boolean cancelStone = (tiered.getTier() == Tiers.STONE) && CommonConfig.REMOVE_STONE_TOOL_FUNC.get();

        if (cancelWood || cancelStone) {
            event.setCanceled(true);
        }
    }
}
