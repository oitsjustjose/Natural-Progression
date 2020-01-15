package com.oitsjustjose.naturalprogression.common.event;

import com.oitsjustjose.naturalprogression.common.config.CommonConfig;
import com.oitsjustjose.naturalprogression.common.utils.Constants;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ToolTips
{
    private Tag<Item> axes = ItemTags.getCollection().getOrCreate(new ResourceLocation(Constants.MODID, "axe"));
    private Tag<Item> saws = ItemTags.getCollection().getOrCreate(new ResourceLocation(Constants.MODID, "saw"));

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onItemHover(ItemTooltipEvent event)
    {
        if (axes.contains(event.getItemStack().getItem()))
        {
            event.getToolTip().add(new TranslationTextComponent("natural-progression.axe.tooltip"));
        }
        else if (saws.contains(event.getItemStack().getItem()))
        {
            event.getToolTip().add(new TranslationTextComponent("natural-progression.saw.tooltip"));
        }
        else if (event.getItemStack().getItem() instanceof ToolItem)
        {
            IItemTier toolTier = ((ToolItem) event.getItemStack().getItem()).getTier();
            if (toolTier == ItemTier.WOOD && CommonConfig.REMOVE_WOODEN_TOOL_RECIPES.get())
            {
                event.getToolTip().add(new TranslationTextComponent("natural-progression.disabled.tool.tooltip"));
            }
            else if (toolTier == ItemTier.STONE && CommonConfig.REMOVE_STONE_TOOL_RECIPES.get())
            {
                event.getToolTip().add(new TranslationTextComponent("natural-progression.disabled.tool.tooltip"));
            }
        }
        else if (event.getItemStack().getItem() == Items.BONE)
        {
            if (Screen.hasShiftDown())
            {
                if (Screen.hasAltDown())
                {
                    event.getToolTip().add(new TranslationTextComponent("natural-progression.bone.help.1"));
                    event.getToolTip().add(new TranslationTextComponent("natural-progression.bone.help.2"));
                }
                else
                {
                    event.getToolTip().add(new TranslationTextComponent("natural-progression.bone.desc.1"));
                    event.getToolTip().add(new TranslationTextComponent("natural-progression.bone.desc.2"));
                }
            }
            else
            {
                event.getToolTip().add(new TranslationTextComponent("natural-progression.bone.shift"));
            }
        }
    }
}