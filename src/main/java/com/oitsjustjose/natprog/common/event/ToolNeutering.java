package com.oitsjustjose.natprog.common.event;

import com.oitsjustjose.natprog.common.config.CommonConfig;

import net.minecraft.network.chat.TextComponent;
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
        if (!(item instanceof TieredItem)) {
            return;
        }

        TieredItem tiered = (TieredItem) item;

        if ((tiered.getTier() == Tiers.WOOD) && CommonConfig.REMOVE_WOODEN_TOOL_FUNC.get()) {
            event.getToolTip().add(new TextComponent(
                    "\u00A74" + "This tool is too brittle to use." + "\u00A7r"));
            return;
        }
        if ((tiered.getTier() == Tiers.STONE) && CommonConfig.REMOVE_STONE_TOOL_FUNC.get()) {
            event.getToolTip().add(new TextComponent(
                    "\u00A74" + "This tool is too blunt to use" + "\u00A7r"));
            return;
        }
    }

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed event) {
        if (event.getState() == null || event.getPlayer() == null) {
            return;
        }

        Item heldItem = event.getPlayer().getMainHandItem().getItem();
        if (!(heldItem instanceof TieredItem)) {
            return;
        }

        TieredItem tiered = (TieredItem) heldItem;
        boolean cancelWood = (tiered.getTier() == Tiers.WOOD) && CommonConfig.REMOVE_WOODEN_TOOL_FUNC.get();
        boolean cancelStone = (tiered.getTier() == Tiers.STONE) && CommonConfig.REMOVE_STONE_TOOL_FUNC.get();

        if (cancelWood || cancelStone) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void registerEvent(AttackEntityEvent event) {
        if (event.getPlayer() == null) {
            return;
        }

        Item heldItem = event.getPlayer().getMainHandItem().getItem();
        if (!(heldItem instanceof TieredItem)) {
            return;
        }

        TieredItem tiered = (TieredItem) heldItem;
        boolean cancelWood = (tiered.getTier() == Tiers.WOOD) && CommonConfig.REMOVE_WOODEN_TOOL_FUNC.get();
        boolean cancelStone = (tiered.getTier() == Tiers.STONE) && CommonConfig.REMOVE_STONE_TOOL_FUNC.get();

        if (cancelWood || cancelStone) {
            event.setCanceled(true);
        }
    }
}
