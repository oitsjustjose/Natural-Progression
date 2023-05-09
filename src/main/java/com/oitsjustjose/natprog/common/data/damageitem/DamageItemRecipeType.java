package com.oitsjustjose.natprog.common.data.damageitem;

import com.oitsjustjose.natprog.Constants;
import net.minecraft.world.item.crafting.RecipeType;

public class DamageItemRecipeType implements RecipeType<DamageItemRecipe> {
    @Override
    public String toString() {
        return Constants.MOD_ID + ":damage_tools";
    }
}
