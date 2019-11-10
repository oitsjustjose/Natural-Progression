package com.oitsjustjose.naturalprogression.common.compat.jei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.oitsjustjose.naturalprogression.common.items.NaturalProgressionItems;
import com.oitsjustjose.naturalprogression.common.utils.Constants;
import com.oitsjustjose.naturalprogression.common.utils.Utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class StrippedLogRecipeMaker
{
    private static HashMap<ItemStack, ItemStack> getPairs()
    {
        HashMap<ItemStack, ItemStack> pairs = new HashMap<>();

        for (Item wood : ItemTags.LOGS.getAllElements())
        {
            if (!Utils.getStrippedLog(wood).isEmpty())
            {
                pairs.put(new ItemStack(wood), Utils.getStrippedLog(wood));
            }
        }

        return pairs;
    }

    public static List<ShapelessRecipe> getRecipes()
    {
        List<ShapelessRecipe> recipes = new ArrayList<>();
        String group = "naturalprogression.log.stripping";

        HashMap<ItemStack, ItemStack> pairs = getPairs();
        ItemStack[] saws = new ItemStack[]
        { new ItemStack(NaturalProgressionItems.basicSaw), new ItemStack(NaturalProgressionItems.improvedSaw) };

        for (Entry<ItemStack, ItemStack> entry : pairs.entrySet())
        {
            for (int k = 0; k < saws.length; k++)
            {
                Ingredient input = Ingredient.fromStacks(entry.getKey());
                Ingredient saw = Ingredient.fromStacks(saws[k]);
                NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY, input, saw);
                ItemStack output = entry.getValue();

                ResourceLocation id = new ResourceLocation(Constants.MODID,
                        "naturalprogression.log.stripping." + output.getTranslationKey());

                ShapelessRecipe recipe = new ShapelessRecipe(id, group, output, inputs);
                recipes.add(recipe);
            }
        }
        return recipes;
    }

}