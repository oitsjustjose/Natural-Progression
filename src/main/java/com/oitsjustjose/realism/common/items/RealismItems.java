package com.oitsjustjose.realism.common.items;

import java.util.ArrayList;

import com.oitsjustjose.realism.common.utils.Constants;

import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public class RealismItems
{
    private static ArrayList<Item> modItems = new ArrayList<>();
    public static Item flintHatchet;
    public static Item basicSaw;
    public static Item improvedSaw;

    public static void registerItems(final RegistryEvent.Register<Item> itemRegistryEvent)
    {
        flintHatchet = new HatchetItem();
        flintHatchet.setRegistryName(new ResourceLocation(Constants.MODID, "flint_hatchet"));
        modItems.add(flintHatchet);

        basicSaw = new SawItem(new FlintItemTier());
        basicSaw.setRegistryName(new ResourceLocation(Constants.MODID, "basic_saw"));
        modItems.add(basicSaw);

        improvedSaw = new SawItem(ItemTier.IRON);
        improvedSaw.setRegistryName(new ResourceLocation(Constants.MODID, "improved_saw"));
        modItems.add(improvedSaw);

        for (Item item : modItems)
        {
            itemRegistryEvent.getRegistry().register(item);
        }
    }

}