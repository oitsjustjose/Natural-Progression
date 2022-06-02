package com.oitsjustjose.natprog.common.utils;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class Constants {
    public static final String MODID = "natprog";
    public static final String MODNAME = "Natural Progression";

    public static final TagKey<Item> OVERRIDE_PICKAXES = ItemTags
            .create(new ResourceLocation(MODID, "override_pickaxes"));
    public static final TagKey<Block> IGNORED_STONE_BLOCKS = BlockTags
            .create(new ResourceLocation(MODID, "ignored_stone_blocks"));

    public static final TagKey<Item> OVERRIDE_AXES = ItemTags.create(new ResourceLocation(MODID, "override_axes"));
    public static final TagKey<Block> IGNORED_WOOD_BLOCKS = BlockTags
            .create(new ResourceLocation(MODID, "ignored_wood_blocks"));
    public static final TagKey<Block> GROUND = BlockTags.create(new ResourceLocation(MODID, "ground"));

    // The below tagkey is instantiated differently because it *has* to be - the
    // usual method like above isn't made public so EntityTypeTags.create(String s)
    // is private, and there is no constructor w/ ResourceLocation as param
    public static final TagKey<EntityType<?>> BONE_DROP = TagKey.create(Registry.ENTITY_TYPE_REGISTRY,
            new ResourceLocation(MODID, "drops_bone"));
}
