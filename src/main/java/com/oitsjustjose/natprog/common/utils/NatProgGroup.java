package com.oitsjustjose.natprog.common.utils;

import javax.annotation.Nonnull;

import com.oitsjustjose.natprog.common.items.NatProgItems;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

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
        return new ItemStack(NatProgItems.flintSaw);
    }
}
