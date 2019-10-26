package com.oitsjustjose.naturalprogression.common.recipes;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableMap;
import com.oitsjustjose.naturalprogression.NaturalProgression;
import com.oitsjustjose.naturalprogression.common.utils.Constants;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

/**
 * This code is written as a result to PlankRecipe#match() not catching plank crafting consistently due to it being the *wrong*
 * way to do it (via https://bit.ly/2Ns6Vkv)
 * 
 * Thanks to Choonster@github, this code is mostly theirs: https://bit.ly/2BLB9t3 (Code used under their MIT license)
 */

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class RecipeRemover
{
    private static final Field RECIPES = ObfuscationReflectionHelper.findField(RecipeManager.class,
            "field_199522_d" /* recipes */);

    @SubscribeEvent
    public static void onServerStart(final FMLServerStartedEvent event)
    {
        final RecipeManager mgr = event.getServer().getRecipeManager();
        remove(mgr);
    }

    private static void remove(RecipeManager mgr)
    {
        final Item[] extrasToRemove = new Item[]
        { Items.WOODEN_AXE, Items.WOODEN_HOE, Items.WOODEN_SHOVEL, Items.WOODEN_SWORD, Items.WOODEN_PICKAXE };
        removeRecipes(mgr, ItemTags.PLANKS);

        for (Item extra : extrasToRemove)
        {
            removeRecipes(mgr, new ItemStack(extra, 1));
        }
    }

    /**
     * Removes all crafting recipes with an output item contained in the specified tag.
     *
     * @param recipeManager The recipe manager
     * @param stack         The ItemStack output of the recipe to remove
     */
    private static void removeRecipes(final RecipeManager recipeManager, final ItemStack stack)
    {
        final int recipesRemoved = removeRecipes(recipeManager, recipe -> {
            final ItemStack recipeOutput = recipe.getRecipeOutput();
            return recipeOutput.equals(stack, false);
        });

        NaturalProgression.getInstance().LOGGER.info("Removed {} recipe(s) for {}", recipesRemoved,
                stack.getDisplayName().getFormattedText());
    }

    /**
     * Removes all crafting recipes with an output item contained in the specified tag.
     *
     * @param recipeManager The recipe manager
     * @param tag           The tag
     */
    private static void removeRecipes(final RecipeManager recipeManager, final Tag<Item> tag)
    {
        final int recipesRemoved = removeRecipes(recipeManager, recipe -> {
            final ItemStack recipeOutput = recipe.getRecipeOutput();
            return !recipeOutput.isEmpty() && recipeOutput.getItem().isIn(tag);
        });

        NaturalProgression.getInstance().LOGGER.info("Removed {} recipe(s) for tag {}", recipesRemoved, tag.getId());
    }

    /**
     * Remove all crafting recipes that match the specified predicate.
     *
     * @param recipeManager The recipe manager
     * @param predicate     The predicate
     * @return The number of recipes removed
     */
    private static int removeRecipes(final RecipeManager recipeManager, final Predicate<IRecipe> predicate)
    {
        final Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> existingRecipes;
        try
        {
            @SuppressWarnings("unchecked")
            final Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipesMap = (Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>>) RECIPES
                    .get(recipeManager);
            existingRecipes = recipesMap;
        }
        catch (final IllegalAccessException e)
        {
            throw new RuntimeException("Couldn't get recipes map while removing recipes", e);
        }

        final Object2IntMap<IRecipeType<?>> removedCounts = new Object2IntOpenHashMap<>();
        final ImmutableMap.Builder<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> newRecipes = ImmutableMap
                .builder();

        // For each recipe type, create a new map that doesn't contain the recipes to be removed
        existingRecipes.forEach((recipeType, existingRecipesForType) -> {
            // noinspection UnstableApiUsage
            final ImmutableMap<ResourceLocation, IRecipe<?>> newRecipesForType = existingRecipesForType.entrySet()
                    .stream().filter(entry -> !predicate.test(entry.getValue()))
                    .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));

            removedCounts.put(recipeType, existingRecipesForType.size() - newRecipesForType.size());
            newRecipes.put(recipeType, newRecipesForType);
        });

        final int removedCount = removedCounts.values().stream().reduce(0, Integer::sum);

        try
        {
            RECIPES.set(recipeManager, newRecipes.build());
        }
        catch (final IllegalAccessException e)
        {
            throw new RuntimeException("Couldn't replace recipes map while removing recipes", e);
        }

        return removedCount;
    }
}