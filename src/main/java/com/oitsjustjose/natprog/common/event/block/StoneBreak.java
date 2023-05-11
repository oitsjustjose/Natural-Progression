package com.oitsjustjose.natprog.common.event.block;

import com.google.common.collect.Lists;
import com.oitsjustjose.natprog.Constants;
import com.oitsjustjose.natprog.NatProg;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import com.oitsjustjose.natprog.common.event.DamageTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class StoneBreak {

    public static final TagKey<Item> CONSIDERED_AS_PICKAXE = ItemTags.create(new ResourceLocation(Constants.MOD_ID, "considered_as_pickaxe"));
    public static final TagKey<Block> IGNORED_STONE_BLOCKS = BlockTags.create(new ResourceLocation(Constants.MOD_ID, "ignored_stone_blocks"));
    public static final List<Material> HEAVY_MATS = Lists.newArrayList(Material.HEAVY_METAL, Material.METAL, Material.STONE);

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed evt) {
        if (evt.getState() == null || evt.getEntity() == null) return;
        if (!HEAVY_MATS.contains(evt.getState().getMaterial())) return;
        if (evt.getState().is(IGNORED_STONE_BLOCKS)) return;

        var heldItem = evt.getEntity().getMainHandItem();
        if (heldItem.is(CONSIDERED_AS_PICKAXE)) return;
        if (heldItem.canPerformAction(ToolActions.PICKAXE_DIG)) return;

        var level = evt.getEntity().getLevel();
        evt.setCanceled(true);

        // Random chance to even perform the hurt anim if the player is empty-handed
        if (evt.getEntity().getMainHandItem().isEmpty() && evt.getEntity().getRandom().nextInt(25) == 1) {
            // And when it's shown, random chance to actually hurt from breaking bones
            if (evt.getEntity().getRandom().nextInt(2) == 1) {
                evt.getEntity().hurt(DamageTypes.getDamageSource(level, DamageTypes.CRUSHING), 1F);
            } else {
                NatProg.proxy.doHurtAnimation(evt.getEntity());
            }
        }

        // Show breaking help, if applicable
        if (!CommonConfig.SHOW_BREAKING_HELP.get()) return;
        evt.getEntity().displayClientMessage(Component.translatable("natprog.stone.warning"), true);
    }
}
