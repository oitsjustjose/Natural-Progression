package com.oitsjustjose.naturalprogression.common.compat.jei;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.oitsjustjose.naturalprogression.common.blocks.NaturalProgressionBlocks;
import com.oitsjustjose.naturalprogression.common.utils.Constants;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.ItemStack;
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

        registration.addRecipes(StrippedLogRecipeMaker.getRecipes(), VanillaRecipeCategoryUid.CRAFTING);

        ArrayList<ItemStack> pebbles = Lists.newArrayList();

        pebbles.add(new ItemStack(NaturalProgressionBlocks.andesitePebble));
        pebbles.add(new ItemStack(NaturalProgressionBlocks.dioritePebble));
        pebbles.add(new ItemStack(NaturalProgressionBlocks.granitePebble));
        pebbles.add(new ItemStack(NaturalProgressionBlocks.stonePebble));
        pebbles.add(new ItemStack(NaturalProgressionBlocks.sandstonePebble));

        registration.addIngredientInfo(pebbles, VanillaTypes.ITEM, "natural-progression.pebble.jei.info");
    }
}