package com.oitsjustjose.naturalprogression.common.event;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.oitsjustjose.naturalprogression.common.utils.Constants;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
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
    private ArrayList<Item> neuteredTools = Lists.newArrayList(Items.WOODEN_AXE, Items.WOODEN_PICKAXE,
            Items.WOODEN_SHOVEL, Items.WOODEN_HOE, Items.WOODEN_SWORD, Items.STONE_AXE, Items.STONE_PICKAXE,
            Items.STONE_SHOVEL, Items.STONE_HOE, Items.STONE_SWORD);
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
        else if (neuteredTools.contains(event.getItemStack().getItem()))
        {
            event.getToolTip().add(new TranslationTextComponent("natural-progression.wooden.tool.tooltip"));
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