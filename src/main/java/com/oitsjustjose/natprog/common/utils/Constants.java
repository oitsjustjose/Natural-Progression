package com.oitsjustjose.natprog.common.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class Constants {
    public static final String MODID = "natprog";
    public static final String MODNAME = "Natural Progression";

    public static final Tag<Item> OVERRIDE_PICKAXES = ItemTags.getAllTags()
            .getTagOrEmpty(new ResourceLocation(MODID, "override_pickaxes"));
    public static final Tag<Block> IGNORED_STONE_BLOCKS = BlockTags.getAllTags()
            .getTagOrEmpty(new ResourceLocation(MODID, "ignored_stone_blocks"));

    public static final Tag<Item> OVERRIDE_AXES = ItemTags.getAllTags()
            .getTagOrEmpty(new ResourceLocation(MODID, "override_axes"));
    public static final Tag<Block> IGNORED_WOOD_BLOCKS = BlockTags.getAllTags()
            .getTagOrEmpty(new ResourceLocation(MODID, "ignored_wood_blocks"));
    public static final Tag<Block> GROUND = BlockTags.getAllTags()
            .getTagOrEmpty(new ResourceLocation(MODID, "ground"));
}
