package com.oitsjustjose.natprog.common.items;

import java.util.ArrayList;

import javax.annotation.Nullable;

import com.oitsjustjose.natprog.common.utils.Constants;
import com.oitsjustjose.natprog.common.utils.NatProgGroup;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.event.RegistryEvent;

public class NatProgItems {
    private static ArrayList<Item> modItems = new ArrayList<>();

    public static Tier flintTier = new DynamicItemTier().setMaxUses(16).setEfficiency(1.5F)
            .setAttackDamage(1.0F).setHarvestLvl(0).setEnchantability(0).setRepairMats(Items.FLINT);
    public static Tier boneTier = new DynamicItemTier().setMaxUses(128).setEfficiency(2.0F)
            .setAttackDamage(2.0F).setHarvestLvl(1).setEnchantability(0).setRepairMats(Items.BONE);
    public static Tier copperTier = new DynamicItemTier().setMaxUses(192).setEfficiency(1.65F)
            .setAttackDamage(1.5F).setHarvestLvl(0).setEnchantability(0)
            .setRepairMat(getTagOrNull("forge:ingots/copper"));
    public static Tier bronzeTier = new DynamicItemTier().setMaxUses(442).setEfficiency(2.5F)
            .setAttackDamage(2.5F).setHarvestLvl(2).setEnchantability(0)
            .setRepairMat(getTagOrNull("forge:ingots/bronze"));
    public static Tier steelTier = new DynamicItemTier().setMaxUses(914).setEfficiency(3.5F)
            .setAttackDamage(3.5F).setHarvestLvl(3).setEnchantability(0)
            .setRepairMat(getTagOrNull("forge:ingots/steel"));

    public static Item flintHatchet;
    public static Item bonePickaxe;
    public static Item boneKnife;
    public static Item boneShard;

    public static SawItem flintSaw;
    public static SawItem copperSaw;
    public static SawItem ironSaw;
    public static SawItem goldSaw;
    public static SawItem diamondSaw;
    public static SawItem netheriteSaw;
    public static SawItem bronzeSaw;
    public static SawItem steelSaw;

    public static void registerItems(final RegistryEvent.Register<Item> itemRegistryEvent) {
        flintHatchet = new HatchetItem();
        flintHatchet.setRegistryName(new ResourceLocation(Constants.MODID, "flint_hatchet"));
        modItems.add(flintHatchet);

        bonePickaxe = new PickaxeItem(boneTier, 1, -2.8F,
                new Item.Properties().tab(NatProgGroup.getInstance()).stacksTo(1));
        bonePickaxe.setRegistryName(new ResourceLocation(Constants.MODID, "bone_pickaxe"));
        modItems.add(bonePickaxe);

        boneKnife = new SwordItem(boneTier, 1, -1.4F,
                new Item.Properties().tab(NatProgGroup.getInstance()).stacksTo(1));
        boneKnife.setRegistryName(new ResourceLocation(Constants.MODID, "bone_knife"));
        modItems.add(boneKnife);

        boneShard = new Item(new Item.Properties().tab(NatProgGroup.getInstance()));
        boneShard.setRegistryName(new ResourceLocation(Constants.MODID, "bone_shard"));
        modItems.add(boneShard);

        flintSaw = new SawItem(flintTier);
        flintSaw.setRegistryName(new ResourceLocation(Constants.MODID, "basic_saw"));
        modItems.add(flintSaw);

        ironSaw = new SawItem(Tiers.IRON);
        ironSaw.setRegistryName(new ResourceLocation(Constants.MODID, "improved_saw"));
        modItems.add(ironSaw);

        goldSaw = new SawItem(Tiers.GOLD);
        goldSaw.setRegistryName(new ResourceLocation(Constants.MODID, "golden_saw"));
        modItems.add(goldSaw);

        diamondSaw = new SawItem(Tiers.DIAMOND);
        diamondSaw.setRegistryName(new ResourceLocation(Constants.MODID, "diamond_saw"));
        modItems.add(diamondSaw);

        netheriteSaw = new SawItem(Tiers.NETHERITE, true);
        netheriteSaw.setRegistryName(new ResourceLocation(Constants.MODID, "netherite_saw"));
        modItems.add(netheriteSaw);

        copperSaw = new SawItem(copperTier);
        copperSaw.setRegistryName(new ResourceLocation(Constants.MODID, "copper_saw"));
        modItems.add(copperSaw);

        bronzeSaw = new SawItem(bronzeTier);
        bronzeSaw.setRegistryName(new ResourceLocation(Constants.MODID, "bronze_saw"));
        modItems.add(bronzeSaw);

        steelSaw = new SawItem(steelTier);
        steelSaw.setRegistryName(new ResourceLocation(Constants.MODID, "steel_saw"));
        modItems.add(steelSaw);

        for (Item item : modItems) {
            itemRegistryEvent.getRegistry().register(item);
        }
    }

    @Nullable
    public static Tag<Item> getTagOrNull(String tagName) {
        ResourceLocation resLoc = new ResourceLocation(tagName);
        if (ItemTags.getAllTags().getAvailableTags().contains(resLoc)) {
            return ItemTags.getAllTags().getTag(resLoc);
        }
        return null;
    }
}
