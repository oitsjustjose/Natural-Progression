package com.oitsjustjose.natprog.common.items;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

public class SawItem extends AxeItem {
    public SawItem(Tier tier) {
        super(tier, 1.8F, 0F, new Item.Properties().stacksTo(0));
    }

    public SawItem(Tier tier, boolean ignoredIsImmuneToFire) {
        super(tier, 1.8F, 0F, new Item.Properties().stacksTo(0).fireResistant());
    }
}
