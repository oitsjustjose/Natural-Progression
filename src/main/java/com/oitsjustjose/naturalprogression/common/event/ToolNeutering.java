package com.oitsjustjose.naturalprogression.common.event;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ToolNeutering
{
    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed event)
    {
        if (event.getState() == null || event.getPlayer() == null)
        {
            return;
        }

        ItemStack heldItem = event.getPlayer().getHeldItemMainhand();

        if (heldItem.getItem() instanceof ToolItem)
        {
            ToolItem tool = (ToolItem) heldItem.getItem();
            if (tool.getTier() == ItemTier.WOOD || tool.getTier() == ItemTier.STONE)
            {
                event.setCanceled(true);
            }
        }
        else if (heldItem.getItem() instanceof SwordItem)
        {
            SwordItem tool = (SwordItem) heldItem.getItem();
            if (tool.getTier() == ItemTier.WOOD || tool.getTier() == ItemTier.STONE)
            {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void registerEvent(AttackEntityEvent event)
    {
        if (event.getPlayer() == null)
        {
            return;
        }

        ItemStack heldItem = event.getPlayer().getHeldItemMainhand();

        if (heldItem.getItem() instanceof SwordItem)
        {
            SwordItem tool = (SwordItem) heldItem.getItem();
            if (tool.getTier() == ItemTier.WOOD || tool.getTier() == ItemTier.STONE)
            {
                if (event.isCancelable())
                {
                    event.setCanceled(true);
                }
            }
        }
    }
}