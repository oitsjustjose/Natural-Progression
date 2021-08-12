package com.oitsjustjose.naturalprogression.common.event;

import com.oitsjustjose.naturalprogression.common.config.CommonConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.TieredItem;
import net.minecraft.util.text.StringTextComponent;
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

        if ((tiered.getTier() == ItemTier.WOOD) && CommonConfig.REMOVE_WOODEN_TOOL_FUNC.get()) {
            event.getToolTip().add(new StringTextComponent(
                    "\u00A74" + "This tool is too brittle to use." + "\u00A7r"));
            return;
        }
        if ((tiered.getTier() == ItemTier.STONE) && CommonConfig.REMOVE_STONE_TOOL_FUNC.get()) {
            event.getToolTip().add(new StringTextComponent(
                    "\u00A74" + "This tool is too blunt to use" + "\u00A7r"));
            return;
        }
    }

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed event) {
        if (event.getState() == null || event.getPlayer() == null) {
            return;
        }

        Item heldItem = event.getPlayer().getHeldItemMainhand().getItem();
        if (!(heldItem instanceof TieredItem)) {
            return;
        }

        TieredItem tiered = (TieredItem) heldItem;
        boolean cancelWood =
                (tiered.getTier() == ItemTier.WOOD) && CommonConfig.REMOVE_WOODEN_TOOL_FUNC.get();
        boolean cancelStone =
                (tiered.getTier() == ItemTier.STONE) && CommonConfig.REMOVE_STONE_TOOL_FUNC.get();

        if (cancelWood || cancelStone) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void registerEvent(AttackEntityEvent event) {
        if (event.getPlayer() == null) {
            return;
        }

        Item heldItem = event.getPlayer().getHeldItemMainhand().getItem();
        if (!(heldItem instanceof TieredItem)) {
            return;
        }

        TieredItem tiered = (TieredItem) heldItem;
        boolean cancelWood =
                (tiered.getTier() == ItemTier.WOOD) && CommonConfig.REMOVE_WOODEN_TOOL_FUNC.get();
        boolean cancelStone =
                (tiered.getTier() == ItemTier.STONE) && CommonConfig.REMOVE_STONE_TOOL_FUNC.get();

        if (cancelWood || cancelStone) {
            event.setCanceled(true);
        }
    }
}
