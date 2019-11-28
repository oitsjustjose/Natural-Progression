package com.oitsjustjose.naturalprogression.common.event;

import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GroundBreak
{
    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed event)
    {
        if (event.getState().getMaterial() == Material.EARTH || event.getState().getMaterial() == Material.SAND
                || event.getState().getMaterial() == Material.ORGANIC)
        {
            // If NOT holding a shovel
            if (!event.getPlayer().getHeldItemMainhand().getToolTypes().contains(ToolType.SHOVEL))
            {
                event.setNewSpeed(event.getOriginalSpeed() / 4);
            }
        }
    }
}