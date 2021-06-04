package com.oitsjustjose.naturalprogression.common.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import com.oitsjustjose.naturalprogression.NaturalProgression;
import com.oitsjustjose.naturalprogression.common.items.PebbleItem;
import com.oitsjustjose.naturalprogression.common.utils.Constants;
import com.oitsjustjose.naturalprogression.common.utils.NaturalProgressionGroup;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class NaturalProgressionBlocks {
    public static HashMap<Block, Block> blocksToPebbles = new HashMap<>();
    private static ArrayList<Block> modBlocks = new ArrayList<>();

    public static Block twigs;

    private static void registerPebble(String modid, String path) {
        ResourceLocation rl = new ResourceLocation(modid, path);
        ResourceLocation pebbleRl = new ResourceLocation(Constants.MODID,
                (modid.toLowerCase() == "minecraft" ? "" : (modid + "_")) + path + "_pebble");
        Block b = ForgeRegistries.BLOCKS.getValue(rl);

        if (b != null && b != Blocks.AIR) {
            Block pebble = new PebbleBlock().setRegistryName(pebbleRl);
            modBlocks.add(pebble);
            blocksToPebbles.put(b, pebble);
        } else {
            NaturalProgression.getInstance().LOGGER.warn("{}:{} could not be found. No pebble will be created", modid,
                    path);
        }
    }

    private static void registerCobble(String modid, String path) {
        ResourceLocation rl = new ResourceLocation(modid, path);
        // Make it such that if there are two limestones, both can be registered.
        ResourceLocation cobbleRl = new ResourceLocation(Constants.MODID,
                (modid.toLowerCase() == "minecraft" ? "" : (modid + "_")) + "cobbled_" + path);
        Block b = ForgeRegistries.BLOCKS.getValue(rl);

        Block.Properties cobbleProps = Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 6.0F)
                .sound(b.getDefaultState().getSoundType());

        if (b != null && b != Blocks.AIR) {
            Block cobble = new Block(cobbleProps).setRegistryName(cobbleRl);
            modBlocks.add(cobble);
        } else {
            NaturalProgression.getInstance().LOGGER.warn("{}:{} could not be found. No cobble will be created", modid,
                    path);
        }
    }

    public static void registerBlocks(final RegistryEvent.Register<Block> blockRegistryEvent) {
        /* ------------ PEBBLES ------------ */
        registerPebble("minecraft", "stone");
        registerPebble("minecraft", "andesite");
        registerPebble("minecraft", "diorite");
        registerPebble("minecraft", "granite");
        registerPebble("minecraft", "sandstone");
        registerPebble("minecraft", "red_sandstone");

        registerPebble("minecraft", "netherrack");
        registerPebble("minecraft", "end_stone");

        /* Quark Stones (NO cobble needed) */
        registerPebble("quark", "marble");
        registerPebble("quark", "limestone");
        registerPebble("quark", "jasper");
        registerPebble("quark", "slate");
        registerPebble("quark", "basalt");

        /* Create Stones (NO cobble needed) */
        registerPebble("create", "limestone");
        registerPebble("create", "weathered_limestone");
        registerPebble("create", "dolomite");
        registerPebble("create", "gabbro");
        registerPebble("create", "scoria");
        registerPebble("create", "dark_scoria");

        /* ------------ COBBLES ------------ */
        registerCobble("minecraft", "andesite");
        registerCobble("minecraft", "diorite");
        registerCobble("minecraft", "granite");
        registerCobble("minecraft", "sandstone");
        registerCobble("minecraft", "red_sandstone");
        registerCobble("minecraft", "netherrack");
        registerCobble("minecraft", "end_stone");

        // Don't add to mod blocks -- we don't want a BlockItem for this, we just want
        // sticks.
        twigs = new TwigBlock().setRegistryName(new ResourceLocation(Constants.MODID, "twigs"));
        blockRegistryEvent.getRegistry().register(twigs);

        for (Block b : modBlocks) {
            blockRegistryEvent.getRegistry().register(b);
        }
    }

    public static void registerBlockItems(final RegistryEvent.Register<Item> itemRegistryEvent) {
        for (Block block : modBlocks) {
            // Ignore pebble blocks - pebble item will represent them
            if (block instanceof PebbleBlock) {
                Item iBlock = new PebbleItem(block).setRegistryName(Objects.requireNonNull(block.getRegistryName()));
                itemRegistryEvent.getRegistry().register(iBlock);
            } else {
                Item iBlock = new BlockItem(block, new Item.Properties().group(NaturalProgressionGroup.getInstance()))
                        .setRegistryName(Objects.requireNonNull(block.getRegistryName()));
                itemRegistryEvent.getRegistry().register(iBlock);
            }
        }
    }
}