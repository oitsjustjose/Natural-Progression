package com.oitsjustjose.naturalprogression.common.compat.jei;

import com.oitsjustjose.naturalprogression.common.utils.Constants;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JeiCompat implements IModPlugin
{
    @Override
    public ResourceLocation getPluginUid()
    {
        return new ResourceLocation(Constants.MODID, "crafting");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration)
    {
        registration.addRecipes(PlankRecipeMaker.getRecipes(), VanillaRecipeCategoryUid.CRAFTING);
    }
}