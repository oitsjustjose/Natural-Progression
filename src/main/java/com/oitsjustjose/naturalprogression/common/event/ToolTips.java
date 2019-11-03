package com.oitsjustjose.naturalprogression.common.event;

import com.oitsjustjose.naturalprogression.common.items.SawItem;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ToolTips
{
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onItemHover(ItemTooltipEvent event)
    {
        if (!event.getItemStack().getToolTypes().contains(ToolType.AXE))
        {
            return;
        }

        TranslationTextComponent tooltip;
        if (event.getItemStack().getItem() instanceof SawItem)
        {
            tooltip = new TranslationTextComponent("natural-progression.saw.tooltip");
        }
        else
        {
            tooltip = new TranslationTextComponent("natural-progression.axe.tooltip");
        }

        event.getToolTip().add(tooltip);
    }
}