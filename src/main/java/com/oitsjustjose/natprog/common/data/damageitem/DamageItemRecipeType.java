package com.oitsjustjose.natprog.common.data.damageitem;

import com.oitsjustjose.natprog.common.utils.Constants;
import net.minecraft.world.item.crafting.RecipeType;

public class DamageItemRecipeType implements RecipeType<DamageItemRecipe> {
    @Override
    public String toString() {
        return Constants.MODID + ":damage_tools";
    }
}
