package com.oitsjustjose.naturalprogression.common.items;

import java.util.ArrayList;

import com.oitsjustjose.naturalprogression.common.utils.Constants;
import com.oitsjustjose.naturalprogression.common.utils.NaturalProgressionGroup;

import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public class NaturalProgressionItems
{
    private static ArrayList<Item> modItems = new ArrayList<>();
    private static BoneItemTier boneTier = new BoneItemTier();

    public static Item flintHatchet;
    public static Item bonePickaxe;
    public static Item boneKnife;
    public static Item boneShard;
    public static Item basicSaw;
    public static Item improvedSaw;


    public static void registerItems(final RegistryEvent.Register<Item> itemRegistryEvent)
    {
        flintHatchet = new HatchetItem();
        flintHatchet.setRegistryName(new ResourceLocation(Constants.MODID, "flint_hatchet"));
        modItems.add(flintHatchet);

        bonePickaxe = new PickaxeItem(boneTier, 1, -2.8F,
                new Item.Properties().group(NaturalProgressionGroup.getInstance()).maxStackSize(1).maxDamage(40));
        bonePickaxe.setRegistryName(new ResourceLocation(Constants.MODID, "bone_pickaxe"));
        modItems.add(bonePickaxe);

        boneKnife = new SwordItem(boneTier, 1, -1.4F,
                new Item.Properties().group(NaturalProgressionGroup.getInstance()).maxStackSize(1).maxDamage(40));
        boneKnife.setRegistryName(new ResourceLocation(Constants.MODID, "bone_knife"));
        modItems.add(boneKnife);

        boneShard = new Item(new Item.Properties().group(NaturalProgressionGroup.getInstance()));
        boneShard.setRegistryName(new ResourceLocation(Constants.MODID, "bone_shard"));
        modItems.add(boneShard);

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