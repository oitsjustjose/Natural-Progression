package com.oitsjustjose.naturalprogression.common.blocks;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class NaturalProgressionBlocks
{
    private static ArrayList<Block> modBlocks = new ArrayList<>();

    public static Block stonePebble;
    public static Block andesitePebble;
    public static Block dioritePebble;
    public static Block granitePebble;
    public static Block sandstonePebble;
    public static Block redSandstonePebble;

    public static Block cobbledAndesite;
    public static Block cobbledDiorite;
    public static Block cobbledGranite;
    public static Block cobbledSandstone;
    public static Block cobbledRedSandstone;

    public static HashMap<Block, Block> blocksToPebbles = new HashMap<>();

    public static void registerBlocks(final RegistryEvent.Register<Block> blockRegistryEvent)
    {
        stonePebble = new PebbleBlock().setRegistryName(new ResourceLocation(Constants.MODID, "stone_pebble"));
        blockRegistryEvent.getRegistry().register(stonePebble);
        modBlocks.add(stonePebble);

        andesitePebble = new PebbleBlock().setRegistryName(new ResourceLocation(Constants.MODID, "andesite_pebble"));
        blockRegistryEvent.getRegistry().register(andesitePebble);
        modBlocks.add(andesitePebble);
        blocksToPebbles.put(Blocks.ANDESITE, andesitePebble);

        dioritePebble = new PebbleBlock().setRegistryName(new ResourceLocation(Constants.MODID, "diorite_pebble"));
        blockRegistryEvent.getRegistry().register(dioritePebble);
        modBlocks.add(dioritePebble);
        blocksToPebbles.put(Blocks.DIORITE, dioritePebble);

        granitePebble = new PebbleBlock().setRegistryName(new ResourceLocation(Constants.MODID, "granite_pebble"));
        blockRegistryEvent.getRegistry().register(granitePebble);
        modBlocks.add(granitePebble);
        blocksToPebbles.put(Blocks.GRANITE, granitePebble);

        sandstonePebble = new PebbleBlock().setRegistryName(new ResourceLocation(Constants.MODID, "sandstone_pebble"));
        blockRegistryEvent.getRegistry().register(sandstonePebble);
        modBlocks.add(sandstonePebble);
        blocksToPebbles.put(Blocks.SANDSTONE, sandstonePebble);
        blocksToPebbles.put(Blocks.SAND, sandstonePebble);

        redSandstonePebble = new PebbleBlock()
                .setRegistryName(new ResourceLocation(Constants.MODID, "red_sandstone_pebble"));
        blockRegistryEvent.getRegistry().register(redSandstonePebble);
        modBlocks.add(redSandstonePebble);
        blocksToPebbles.put(Blocks.RED_SANDSTONE, redSandstonePebble);
        blocksToPebbles.put(Blocks.RED_SAND, redSandstonePebble);

        Block.Properties cobbleProps = Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 6.0F);

        cobbledAndesite = new Block(cobbleProps)
                .setRegistryName(new ResourceLocation(Constants.MODID, "cobbled_andesite"));
        blockRegistryEvent.getRegistry().register(cobbledAndesite);
        modBlocks.add(cobbledAndesite);

        cobbledDiorite = new Block(cobbleProps)
                .setRegistryName(new ResourceLocation(Constants.MODID, "cobbled_diorite"));
        blockRegistryEvent.getRegistry().register(cobbledDiorite);
        modBlocks.add(cobbledDiorite);

        cobbledGranite = new Block(cobbleProps)
                .setRegistryName(new ResourceLocation(Constants.MODID, "cobbled_granite"));
        blockRegistryEvent.getRegistry().register(cobbledGranite);
        modBlocks.add(cobbledGranite);

        cobbledSandstone = new Block(cobbleProps)
                .setRegistryName(new ResourceLocation(Constants.MODID, "cobbled_sandstone"));
        blockRegistryEvent.getRegistry().register(cobbledSandstone);
        modBlocks.add(cobbledSandstone);

        cobbledRedSandstone = new Block(cobbleProps)
                .setRegistryName(new ResourceLocation(Constants.MODID, "cobbled_red_sandstone"));
        blockRegistryEvent.getRegistry().register(cobbledRedSandstone);
        modBlocks.add(cobbledRedSandstone);
    }

    public static void registerBlockItems(final RegistryEvent.Register<Item> itemRegistryEvent)
    {
        for (Block block : modBlocks)
        {
            // Ignore pebble blocks - pebble item will represent them
            if (block instanceof PebbleBlock)
            {
                Item iBlock = new PebbleItem(block).setRegistryName(Objects.requireNonNull(block.getRegistryName()));
                itemRegistryEvent.getRegistry().register(iBlock);
            }
            else
            {
                Item iBlock = new BlockItem(block, new Item.Properties().group(NaturalProgressionGroup.getInstance()))
                        .setRegistryName(Objects.requireNonNull(block.getRegistryName()));
                itemRegistryEvent.getRegistry().register(iBlock);
            }
        }
    }

}