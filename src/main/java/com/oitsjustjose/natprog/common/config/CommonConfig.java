package com.oitsjustjose.natprog.common.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;
import java.util.List;

@Mod.EventBusSubscriber
public class CommonConfig {
    public static final ForgeConfigSpec COMMON_CONFIG;
    private static final Builder COMMON_BUILDER = new Builder();

    public static ForgeConfigSpec.IntValue MAX_PEBBLES_PER_CHUNK;
    public static ForgeConfigSpec.IntValue MAX_TWIGS_PER_CHUNK;
    public static ForgeConfigSpec.BooleanValue TOOL_NEUTERING;
    public static ForgeConfigSpec.BooleanValue REMOVE_WOODEN_TOOL_FUNC;
    public static ForgeConfigSpec.BooleanValue REMOVE_STONE_TOOL_FUNC;
    public static ForgeConfigSpec.BooleanValue MAKE_GROUND_BLOCKS_HARDER;
    public static ForgeConfigSpec.BooleanValue ARE_PEBBLES_REPLACEABLE;
    public static ForgeConfigSpec.IntValue FLINT_CHANCE;
    public static ForgeConfigSpec.IntValue BONE_SHARD_CHANCE;
    public static ForgeConfigSpec.IntValue BONE_DROP_CHANCE;
    public static ForgeConfigSpec.BooleanValue ALL_ENTITIES_DROP_BONES;
    public static ForgeConfigSpec.BooleanValue SHOW_BREAKING_HELP;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> PEBBLE_PLACEMENT_BLACKLIST;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> TWIG_PLACEMENT_BLACKLIST;

    static {
        init();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave()
                .writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }

    private static void init() {
        String CATEGORY_GENERAL = "general";
        COMMON_BUILDER.comment("Miscellaneous").push(CATEGORY_GENERAL);

        MAX_PEBBLES_PER_CHUNK = COMMON_BUILDER
                .comment("The maximum number of pebbles that can be found in each chunk")
                .defineInRange("maxPebblesPerChunk", 5, 0, 256);
        MAX_TWIGS_PER_CHUNK = COMMON_BUILDER
                .comment("The maximum number of twigs that can be found in each chunk")
                .defineInRange("maxTwigsPerChunk", 3, 0, 256);
        TOOL_NEUTERING = COMMON_BUILDER
                .comment("Make disabled tools completely useless - can't even break grass.")
                .define("toolNeutering", false);
        REMOVE_WOODEN_TOOL_FUNC = COMMON_BUILDER.comment(
                "Setting this to true prevents the ability to use wooden tools, though you can still craft them for compatibility.")
                .define("removeWoodenToolFunctionality", true);
        REMOVE_STONE_TOOL_FUNC = COMMON_BUILDER.comment(
                "Setting this to true prevents the ability to use stone tools, though you can still craft them for compatibility.")
                .define("removeStoneToolFunctionality", true);
        MAKE_GROUND_BLOCKS_HARDER = COMMON_BUILDER.comment(
                "Setting this to true will make ground blocks (e.g. sand, dirt, gravel) harder to break without the correct tool.")
                .define("makeGroundBlocksHarder", true);
        ARE_PEBBLES_REPLACEABLE = COMMON_BUILDER.comment(
                "Setting this to true will allow you to replace pebbles like tall grass (more convenient for building, but loses the block forever")
                .define("arePebblesReplaceable", true);
        FLINT_CHANCE = COMMON_BUILDER
                .comment("The chance (out of 100) for flint to be created via knapping.\n"
                        + "e.g.: Setting to 75 means there is a 75% chance knapping will provide flint.")
                .defineInRange("flintKnappingChance", 75, 1, 100);
        BONE_SHARD_CHANCE = COMMON_BUILDER
                .comment("The chance (out of 100) for bone to be created via knapping.\n"
                        + "e.g.: Setting to 75 means there is a 75% chance knapping will provide a bone shard.")
                .defineInRange("boneShardKnappingChance", 75, 1, 100);
        BONE_DROP_CHANCE = COMMON_BUILDER.comment(
                "The chance (out of 100) that a bone can drop from the entities in 'boneDropMobs'.\nSetting this to 0 disables this feature")
                .defineInRange("boneDropFromMobsChance", 50, 0, 100);
        ALL_ENTITIES_DROP_BONES = COMMON_BUILDER
                .comment("Enabling this causes all entities to drop additional bones when killed")
                .define("allEntitiesDropBones", false);
        SHOW_BREAKING_HELP = COMMON_BUILDER.comment(
                "Setting this to true will let players know that they can't break certain blocks without a certain tool")
                .define("showToolHelp", true);
        PEBBLE_PLACEMENT_BLACKLIST = COMMON_BUILDER.comment(
                "A list of 'modid:block' that represent blocks that pebbles can be placed on.")
                .defineList(
                        "pebblePlacementBlacklist", Lists.newArrayList("minecraft:ice",
                                "minecraft:packed_ice", "minecraft:bedrock"),
                        (itemRaw) -> itemRaw instanceof String);
        TWIG_PLACEMENT_BLACKLIST = COMMON_BUILDER.comment(
                "A list of 'modid:block' that represent blocks that twigs can be placed on.")
                .defineList(
                        "twigPlacementBlacklist", Lists.newArrayList("minecraft:ice",
                                "minecraft:packed_ice", "minecraft:bedrock"),
                        (itemRaw) -> itemRaw instanceof String);
        COMMON_BUILDER.pop();
    }
}
