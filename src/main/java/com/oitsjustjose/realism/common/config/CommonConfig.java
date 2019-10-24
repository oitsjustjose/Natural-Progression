package com.oitsjustjose.realism.common.config;

import java.nio.file.Path;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonConfig
{
    public static final ForgeConfigSpec COMMON_CONFIG;
    private static final Builder COMMON_BUILDER = new Builder();

    public static ForgeConfigSpec.IntValue MAX_PEBBLES_PER_CHUNK;
    public static ForgeConfigSpec.BooleanValue GEOLOSYS_GEN_PEBBLES;

    private static String CATEGORY_GENERAL = "general";

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
        COMMON_BUILDER.comment("Miscellaneous").push(CATEGORY_GENERAL);

        MAX_PEBBLES_PER_CHUNK = COMMON_BUILDER.comment("The maximum number of pebbles that can be found in each chunk")
                .defineInRange("maxPebblesPerChunk", 5, 0, 256);

        COMMON_BUILDER.pop();
    }
}