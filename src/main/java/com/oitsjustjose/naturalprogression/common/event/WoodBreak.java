package com.oitsjustjose.naturalprogression.common.event;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.block.material.Material;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WoodBreak
{
    private HashMap<UUID, Long> lastUpdate = new HashMap<>();

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed event)
    {
        if (event.getState() == null || event.getPlayer() == null)
        {
            return;
        }

        if (event.getState().getMaterial() == Material.WOOD)
        {
            event.setNewSpeed(0F);

            UUID uuid = event.getPlayer().getUniqueID();
            long time = System.currentTimeMillis();

            if (!lastUpdate.containsKey(uuid) || time - lastUpdate.get(uuid) >= 10000)
            {
                event.getPlayer().sendStatusMessage(new TranslationTextComponent("natural-progression.wood.warning"),
                        true);
                lastUpdate.put(uuid, time);
            }
        }

        // Item asItem = event.getState().getBlock().asItem();
        // event.getPlayer().getUniqueID()

        // if (BlockTags.LOGS.contains(event.getState().getBlock()) || ItemTags.LOGS.contains(asItem))
        // {
        // // If the player **isn't** using an axe on a log, don't let them break it
        // if (!event.getPlayer().getHeldItemMainhand().getToolTypes().contains(ToolType.AXE))
        // {
        // event.setNewSpeed(0F);
        //
        // true);
        // }
        // }
    }
}