package com.oitsjustjose.natprog;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NatProgGroup extends CreativeModeTab {
    private static NatProgGroup instance;

    private NatProgGroup() {
        super(Constants.MOD_ID + ".name");
    }

    public static NatProgGroup getInstance() {
        if (instance == null) {
            instance = new NatProgGroup();
        }
        return instance;
    }

    @Override
    public @NotNull ItemStack makeIcon() {
        return new ItemStack(NatProg.getInstance().REGISTRY.ironSaw.get());
    }
}
