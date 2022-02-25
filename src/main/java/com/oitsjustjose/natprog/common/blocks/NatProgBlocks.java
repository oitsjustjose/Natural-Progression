package com.oitsjustjose.natprog.common.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import com.oitsjustjose.natprog.NatProg;
import com.oitsjustjose.natprog.common.items.PebbleItem;
import com.oitsjustjose.natprog.common.utils.Constants;
import com.oitsjustjose.natprog.common.utils.NatProgGroup;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class NatProgBlocks {
    public static HashMap<Block, Block> blocksToPebbles = new HashMap<>();
    private static final ArrayList<Block> modBlocks = new ArrayList<>();

    public static Block twigs;

    private static void registerPebble(String modid, String path) {
        ResourceLocation rl = new ResourceLocation(modid, path);
        ResourceLocation pebbleRl = new ResourceLocation(Constants.MODID,
                (modid.equalsIgnoreCase("minecraft") ? "" : (modid + "_")) + path + "_pebble");
        Block originBlock = ForgeRegistries.BLOCKS.getValue(rl);
        Block pebble = new PebbleBlock().setRegistryName(pebbleRl);
        modBlocks.add(pebble);

        if (originBlock != null && originBlock != Blocks.AIR) {
            blocksToPebbles.put(originBlock, pebble);
        } else {
            NatProg.getInstance().LOGGER
                    .warn("{}:{} could not be found. No pebble will be created", modid, path);
        }
    }

    private static void registerCobble(String modid, String path) {
        ResourceLocation rl = new ResourceLocation(modid, path);
        // Make it such that if there are two limestones, both can be registered.
        ResourceLocation cobbleRl = new ResourceLocation(Constants.MODID,
                (modid.equalsIgnoreCase("minecraft") ? "" : (modid + "_")) + "cobbled_" + path);
        Block b = ForgeRegistries.BLOCKS.getValue(rl);

        Block.Properties cobbleProps = Block.Properties.of(Material.STONE)
                .strength(2.0F, 6.0F).sound(b.defaultBlockState().getSoundType());

        if (b != null && b != Blocks.AIR) {
            Block cobble = new Block(cobbleProps).setRegistryName(cobbleRl);
            modBlocks.add(cobble);
        } else {
            NatProg.getInstance().LOGGER
                    .warn("{}:{} could not be found. No cobble will be created", modid, path);
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

        registerPebble("minecraft", "tuff");
        registerPebble("minecraft", "deepslate");
        registerPebble("minecraft", "dripstone_block");

        registerPebble("minecraft", "netherrack");
        registerPebble("minecraft", "end_stone");

        /* Quark Stones (NO cobble needed) */
        registerPebble("quark", "marble");
        registerPebble("quark", "limestone");
        registerPebble("quark", "jasper");
        registerPebble("quark", "slate");
        registerPebble("quark", "basalt");

        /* Create Stones (NO cobble needed) */
        registerPebble("create", "asurine");
        registerPebble("create", "crimsite");
        registerPebble("create", "limestone");
        registerPebble("create", "ochrum");
        registerPebble("create", "scorchia");
        registerPebble("create", "scoria");
        registerPebble("create", "veridium");


        /* ------------ COBBLES ------------ */
        registerCobble("minecraft", "andesite");
        registerCobble("minecraft", "diorite");
        registerCobble("minecraft", "granite");
        registerCobble("minecraft", "sandstone");
        registerCobble("minecraft", "red_sandstone");

        registerCobble("minecraft", "tuff");
        registerCobble("minecraft", "dripstone_block");

        registerCobble("minecraft", "netherrack");
        registerCobble("minecraft", "end_stone");

        // Don't add to mod blocks -- we don't want a BlockItem for this
        twigs = new TwigBlock().setRegistryName(new ResourceLocation(Constants.MODID, "twigs"));
        blockRegistryEvent.getRegistry().register(twigs);

        for (Block b : modBlocks) {
            blockRegistryEvent.getRegistry().register(b);
        }
    }

    public static void registerBlockItems(final RegistryEvent.Register<Item> itemRegistryEvent) {
        for (Block block : modBlocks) {
            // Ignore pebble blocks - pebble item will represent them
            Item iBlock;
            if (block instanceof PebbleBlock) {
                iBlock = new PebbleItem(block)
                        .setRegistryName(Objects.requireNonNull(block.getRegistryName()));
            } else {
                iBlock = new BlockItem(block, new Item.Properties().tab(NatProgGroup.getInstance()))
                        .setRegistryName(Objects.requireNonNull(block.getRegistryName()));
            }
            itemRegistryEvent.getRegistry().register(iBlock);
        }
    }
}
