package com.oitsjustjose.naturalprogression.common.compat.jei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import com.google.common.collect.Lists;
import com.oitsjustjose.naturalprogression.common.config.CommonConfig;
import com.oitsjustjose.naturalprogression.common.items.NaturalProgressionItems;
import com.oitsjustjose.naturalprogression.common.items.SawItem;

import mezz.jei.api.constants.ModIds;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class PlankRecipeMaker
{
    private static ItemStack[] getAxes()
    {
        ArrayList<ItemStack> axes = Lists.newArrayList();

        for (Item item : ForgeRegistries.ITEMS.getValues())
        {
            if (item instanceof SawItem)
            {
                continue;
            }
            else if (item instanceof AxeItem)
            {
                axes.add(new ItemStack(item));
            }
        }

        ItemStack[] ret = new ItemStack[axes.size()];
        for (int i = 0; i < axes.size(); i++)
        {
            ret[i] = axes.get(i);
        }
        return ret;
    }

    private static HashMap<ItemStack, ItemStack> getPairs()
    {
        HashMap<ItemStack, ItemStack> pairs = new HashMap<>();

        for (Item wood : ItemTags.LOGS.getAllElements())
        {
            ResourceLocation plankLoc = new ResourceLocation(
                    Objects.requireNonNull(wood.getItem().getRegistryName()).getNamespace(),
                    wood.getItem().getRegistryName().getPath().replace("stripped_", "").replace("log", "planks"));

            if (!plankLoc.getPath().contains("plank"))
            {
                continue;
            }

            Item plankItem = ForgeRegistries.ITEMS.getValue(plankLoc);
            // This means that this log doesn't have a plank (or at least that won't be calculated..)
            if (plankItem == null)
            {
                continue;
            }
            if (CommonConfig.REQUIRE_STRIPPED_LOG_FOR_PLANKS.get())
            {
                if (wood.getRegistryName().getPath().toLowerCase().contains("stripped"))
                {
                    pairs.put(new ItemStack(wood), new ItemStack(plankItem));
                }
            }
            else
            {
                if (wood.getRegistryName().getPath().endsWith("_log")
                        && !wood.getRegistryName().getPath().toLowerCase().contains("stripped"))
                {
                    pairs.put(new ItemStack(wood), new ItemStack(plankItem));
                }
            }
        }

        return pairs;
    }

    public static List<ShapelessRecipe> getRecipes()
    {
        List<ShapelessRecipe> recipes = new ArrayList<>();
        String group = "naturalprogression.plank.crafting";

        HashMap<ItemStack, ItemStack> pairs = getPairs();
        ItemStack[] axes = getAxes();
        ItemStack[] saws = new ItemStack[]
        { new ItemStack(NaturalProgressionItems.basicSaw), new ItemStack(NaturalProgressionItems.improvedSaw) };

        for (Entry<ItemStack, ItemStack> entry : pairs.entrySet())
        {
            for (int j = 0; j < axes.length; j++)
            {
                Ingredient input = Ingredient.fromStacks(entry.getKey());
                Ingredient axe = Ingredient.fromStacks(axes[j]);
                NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY, input, axe);
                ItemStack output = entry.getValue();

                ResourceLocation id = new ResourceLocation(Constants.MODID,
                        "naturalprogression.plank.axing." + output.getTranslationKey());

                ShapelessRecipe recipe = new ShapelessRecipe(id, group, output, inputs);
                recipes.add(recipe);
            }

            for (int k = 0; k < saws.length; k++)
            {
                Ingredient input = Ingredient.fromStacks(entry.getKey());
                Ingredient saw = Ingredient.fromStacks(saws[k]);
                NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY, input, saw);
                ItemStack output = new ItemStack(entry.getValue().getItem(), 4);

                ResourceLocation id = new ResourceLocation(ModIds.MINECRAFT_ID,
                        "naturalprogression.plank.sawing." + output.getTranslationKey());

                ShapelessRecipe recipe = new ShapelessRecipe(id, group, output, inputs);
                recipes.add(recipe);
            }
        }
        return recipes;
    }

}