package com.oitsjustjose.naturalprogression.common.items;

import com.oitsjustjose.naturalprogression.common.utils.NaturalProgressionGroup;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;

public class HatchetItem extends AxeItem {
    public HatchetItem() {
        // super(new FlintItemTier(), 1.8F, 0F, new
        // Item.Properties().addToolType(ToolType.AXE, 0)
        // .group(NaturalProgressionGroup.getInstance()).maxStackSize(1).maxDamage(16));
        super(new FlintItemTier(), 1.8F, 0F, new Item.Properties().addToolType(ToolType.AXE, 1)
                .group(NaturalProgressionGroup.getInstance()).maxStackSize(1).maxDamage(16));
    }
}