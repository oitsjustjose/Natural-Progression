package com.oitsjustjose.natprog.common.items;

import com.oitsjustjose.natprog.common.Registry;
import com.oitsjustjose.natprog.common.utils.NatProgGroup;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;

public class HatchetItem extends AxeItem {
    public HatchetItem() {
        super(Registry.flintTier, 1.8F, 0F,
                new Item.Properties()
                        .tab(NatProgGroup.getInstance()).stacksTo(1)
                        .durability(16));
    }
}
