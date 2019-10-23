package com.oitsjustjose.realism.common.items;

import java.util.ArrayList;

import com.oitsjustjose.realism.common.utils.Constants;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public class RealismItems
{
    private static ArrayList<Item> modItems = new ArrayList<>();
    public static Item flintHatchet;

    public static void registerItems(final RegistryEvent.Register<Item> itemRegistryEvent)
    {
        flintHatchet = new HatchetFlint();
        flintHatchet.setRegistryName(new ResourceLocation(Constants.MODID, "flint_hatchet"));
        modItems.add(flintHatchet);

        for (Item item : modItems)
        {
            itemRegistryEvent.getRegistry().register(item);
        }
    }

}