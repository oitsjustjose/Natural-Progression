package com.oitsjustjose.realism.common.blocks;

import java.util.ArrayList;

import com.oitsjustjose.realism.common.utils.Constants;
import com.oitsjustjose.realism.common.utils.RealismGroup;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public class RealismBlocks
{
    private static ArrayList<Block> modBlocks = new ArrayList<>();

    public static Block stonePebble;
    public static Block andesitePebble;
    public static Block dioritePebble;
    public static Block granitePebble;

    public static void registerBlocks(final RegistryEvent.Register<Block> blockRegistryEvent)
    {
        stonePebble = new PebbleBlock().setRegistryName(new ResourceLocation(Constants.MODID, "stone_pebble"));
        blockRegistryEvent.getRegistry().register(stonePebble);
        modBlocks.add(stonePebble);

        andesitePebble = new PebbleBlock().setRegistryName(new ResourceLocation(Constants.MODID, "andesite_pebble"));
        blockRegistryEvent.getRegistry().register(andesitePebble);
        modBlocks.add(andesitePebble);

        dioritePebble = new PebbleBlock().setRegistryName(new ResourceLocation(Constants.MODID, "diorite_pebble"));
        blockRegistryEvent.getRegistry().register(dioritePebble);
        modBlocks.add(dioritePebble);

        granitePebble = new PebbleBlock().setRegistryName(new ResourceLocation(Constants.MODID, "granite_pebble"));
        blockRegistryEvent.getRegistry().register(granitePebble);
        modBlocks.add(granitePebble);

    }

    public static void registerBlockItems(final RegistryEvent.Register<Item> itemRegistryEvent)
    {
        for (Block block : modBlocks)
        {
            Item iBlock = new BlockItem(block, new Item.Properties().group(RealismGroup.getInstance()))
                    .setRegistryName(block.getRegistryName());
            itemRegistryEvent.getRegistry().register(iBlock);
        }
    }

}