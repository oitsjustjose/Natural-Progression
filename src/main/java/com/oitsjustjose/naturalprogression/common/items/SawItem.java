package com.oitsjustjose.naturalprogression.common.items;

import com.oitsjustjose.naturalprogression.common.utils.NaturalProgressionGroup;

import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;

public class SawItem extends AxeItem {
    public SawItem(IItemTier tier) {
        super(tier, 1.8F, 0F, new Item.Properties().addToolType(ToolType.AXE, 0).maxStackSize(0)
                .group(NaturalProgressionGroup.getInstance()));
    }
}