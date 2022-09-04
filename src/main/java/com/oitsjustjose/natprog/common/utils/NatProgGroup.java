package com.oitsjustjose.natprog.common.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class NatProgGroup extends CreativeModeTab {
    private static NatProgGroup instance;

    private NatProgGroup() {
        super(Constants.MODID + ".name");
    }

    public static NatProgGroup getInstance() {
        if (instance == null) {
            instance = new NatProgGroup();
        }
        return instance;
    }

    @Override
    @Nonnull
    public ItemStack makeIcon() {
        Item i = ForgeRegistries.ITEMS.getValue(new ResourceLocation(Constants.MODID, "iron_saw"));
        return i == null ? ItemStack.EMPTY : new ItemStack(i);
    }
}
