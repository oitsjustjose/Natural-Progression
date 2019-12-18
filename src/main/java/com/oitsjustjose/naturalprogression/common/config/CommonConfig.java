package com.oitsjustjose.naturalprogression.common.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;
import java.util.List;

@Mod.EventBusSubscriber
public class CommonConfig
{
    public static final ForgeConfigSpec COMMON_CONFIG;
    private static final Builder COMMON_BUILDER = new Builder();

    public static ForgeConfigSpec.IntValue MAX_PEBBLES_PER_CHUNK;
    public static ForgeConfigSpec.BooleanValue REMOVE_PLANK_RECIPES;
    public static ForgeConfigSpec.BooleanValue REMOVE_WOODEN_TOOL_RECIPES;
    public static ForgeConfigSpec.BooleanValue REMOVE_STONE_TOOL_RECIPES;
    public static ForgeConfigSpec.EnumValue<CraftingSounds> CRAFTING_SOUNDS;
    public static ForgeConfigSpec.BooleanValue MAKE_GROUND_BLOCKS_HARDER;
    public static ForgeConfigSpec.BooleanValue ARE_PEBBLES_REPLACEABLE;
    public static ForgeConfigSpec.IntValue FLINT_CHANCE;
    public static ForgeConfigSpec.IntValue BONE_SHARD_CHANCE;
    public static ForgeConfigSpec.IntValue BONE_DROP_CHANCE;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BONE_DROP_MOBS;
    public static ForgeConfigSpec.BooleanValue SHOW_BREAKING_HELP;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> DIMENSION_WHITELIST;

    static
    {
        init();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path)
    {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave()
                .writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }

    private static void init()
    {
        String CATEGORY_GENERAL = "general";
        COMMON_BUILDER.comment("Miscellaneous").push(CATEGORY_GENERAL);

        MAX_PEBBLES_PER_CHUNK = COMMON_BUILDER.comment("The maximum number of pebbles that can be found in each chunk")
                .defineInRange("maxPebblesPerChunk", 5, 0, 256);
        REMOVE_PLANK_RECIPES = COMMON_BUILDER.comment(
                "Setting this to true makes it so that plank recipes (including most mods) will not work without a saw or axe.")
                .define("removePlankRecipes", true);
        REMOVE_WOODEN_TOOL_RECIPES = COMMON_BUILDER.comment(
                "Setting this to true prevents the ability to craft wooden tools. This is totally unrealistic anyways.")
                .define("removeWoodenToolRecipes", true);
        REMOVE_STONE_TOOL_RECIPES = COMMON_BUILDER.comment(
                "Setting this to true prevents the ability to craft stone tools. This is totally unrealistic anyways.")
                .define("removeWoodenToolRecipes", true);
        CRAFTING_SOUNDS = COMMON_BUILDER.comment(
                "When to play sounds when crafting -- PLANKS will make it only play slicing sounds when crafting planks")
                .defineEnum("craftingSounds", CraftingSounds.ALL);
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
                .defineInRange("boneDropFromMobsChance", 15, 0, 100);
        BONE_DROP_MOBS = COMMON_BUILDER
                .comment("A set of names in form of <modid:loot_table_path> that you wish to add loot drops for.\n"
                        + "These aren't necessarily the paths to the mobs themselves, but *to their loot tables*")
                .defineList("boneDropMobs", Lists.newArrayList(new String[]
                { "minecraft:entities/bat", "minecraft:entities/cat", "minecraft:entities/chicken",
                        "minecraft:entities/cow", "minecraft:entities/donkey", "minecraft:entities/fox",
                        "minecraft:entities/horse", "minecraft:entities/llama", "minecraft:entities/mooshroom",
                        "minecraft:entities/mule", "minecraft:entities/ocelot", "minecraft:entities/panda",
                        "minecraft:entities/parrot", "minecraft:entities/pig", "minecraft:entities/polar_bear",
                        "minecraft:entities/rabbit", "minecraft:entities/sheep", "minecraft:entities/trader_llama",
                        "minecraft:entities/wolf" }), (itemRaw) -> itemRaw instanceof String);
        SHOW_BREAKING_HELP = COMMON_BUILDER.comment(
                "Setting this to true will let players know that they can't break certain blocks without a certain tool")
                .define("showToolHelp", true);
        DIMENSION_WHITELIST = COMMON_BUILDER
                .comment("A string of dimensions in which pebbles should spawn. See the defaults for the format.")
                .defineList("dimensionWhitelist", Lists.newArrayList("minecraft:overworld"),
                        (itemRaw) -> itemRaw instanceof String);
        COMMON_BUILDER.pop();
    }

    public enum CraftingSounds
    {
        ALL, PLANKS, NONE
    }
}