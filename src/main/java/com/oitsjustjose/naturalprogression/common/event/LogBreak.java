package com.oitsjustjose.naturalprogression.common.event;

import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LogBreak
{
    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed event)
    {
        if (event.getState() == null || event.getPlayer() == null)
        {
            return;
        }

        Item asItem = event.getState().getBlock().asItem();

        if (BlockTags.LOGS.contains(event.getState().getBlock()) || ItemTags.LOGS.contains(asItem))
        {
            // If the player **isn't** using an axe on a log, don't let them break it
            if (!event.getPlayer().getHeldItemMainhand().getToolTypes().contains(ToolType.AXE))
            {
                event.setNewSpeed(0F);
                event.getPlayer().sendStatusMessage(new TranslationTextComponent("natural-progression.logs.warning"),
                        true);
            }
        }
    }
}