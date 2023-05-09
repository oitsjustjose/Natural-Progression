package com.oitsjustjose.natprog.common.event;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import com.oitsjustjose.natprog.Constants;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
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
    public static final TagKey<Item> ALLOWED_WOOD_TOOLS = ItemTags.create(new ResourceLocation(Constants.MOD_ID, "allowed_wooden_tools"));
    public static final TagKey<Item> ALLOWED_STONE_TOOLS = ItemTags.create(new ResourceLocation(Constants.MOD_ID, "allowed_stone_tools"));

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onHover(ItemTooltipEvent evt) {
        var stack = evt.getItemStack();
        if (stack.isEmpty()) return;
        if (stack.is(ALLOWED_WOOD_TOOLS) || stack.is(ALLOWED_STONE_TOOLS)) return;

        if (!(stack.getItem() instanceof TieredItem tiered)) return;

        if ((tiered.getTier() != Tiers.WOOD) || !CommonConfig.REMOVE_WOODEN_TOOL_FUNC.get()) return;
        try {
            var content = new TranslatableContents("natprog.too.brittle");
            evt.getToolTip().add(content.resolve(null, null, 0));
        } catch (CommandSyntaxException ex) { /*NOOP*/ }


        if ((tiered.getTier() != Tiers.STONE) || !CommonConfig.REMOVE_STONE_TOOL_FUNC.get()) return;
        try {
            var content = new TranslatableContents("natprog.too.blunt");
            evt.getToolTip().add(content.resolve(null, null, 0));
        } catch (CommandSyntaxException ex) { /*NOOP*/ }
    }

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed evt) {
        if (!CommonConfig.TOOL_NEUTERING.get()) return;
        var stack = evt.getEntity().getMainHandItem();
        if (stack.isEmpty()) return;
        if (stack.is(ALLOWED_WOOD_TOOLS) || stack.is(ALLOWED_STONE_TOOLS)) return;

        if (evt.getState() == null || evt.getEntity() == null) return;
        if (!(stack.getItem() instanceof TieredItem tiered)) return;

        var cancelWood = (tiered.getTier() == Tiers.WOOD) && CommonConfig.REMOVE_WOODEN_TOOL_FUNC.get();
        var cancelStone = (tiered.getTier() == Tiers.STONE) && CommonConfig.REMOVE_STONE_TOOL_FUNC.get();

        if (cancelWood || cancelStone) {
            evt.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void registerEvent(AttackEntityEvent evt) {
        if (evt.getEntity() == null) return;
        var stack = evt.getEntity().getMainHandItem();
        if (stack.isEmpty()) return;
        if (stack.is(ALLOWED_WOOD_TOOLS) || stack.is(ALLOWED_STONE_TOOLS)) return;

        if (!(stack.getItem() instanceof TieredItem tiered)) return;

        var cancelWood = (tiered.getTier() == Tiers.WOOD) && CommonConfig.REMOVE_WOODEN_TOOL_FUNC.get();
        var cancelStone = (tiered.getTier() == Tiers.STONE) && CommonConfig.REMOVE_STONE_TOOL_FUNC.get();

        if (cancelWood || cancelStone) {
            evt.setCanceled(true);
        }
    }
}
