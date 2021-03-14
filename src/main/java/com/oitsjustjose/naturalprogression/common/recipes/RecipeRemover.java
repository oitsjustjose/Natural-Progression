package com.oitsjustjose.naturalprogression.common.recipes;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableMap;
import com.oitsjustjose.naturalprogression.NaturalProgression;
import com.oitsjustjose.naturalprogression.common.config.CommonConfig;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ToolItem;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * This code is written as a result to PlankRecipe#match() not catching plank
 * crafting consistently due to it being the *wrong* way to do it (via
 * https://bit.ly/2Ns6Vkv)
 * <p>
 * Thanks to Choonster@github, this code is mostly theirs:
 * https://bit.ly/2BLB9t3 (Code used under their MIT license)
 */

public class RecipeRemover {
    private static final Field RECIPES = ObfuscationReflectionHelper.findField(RecipeManager.class,
            "field_199522_d" /* recipes */);

    @SubscribeEvent
    public void onServerStart(final FMLServerStartedEvent evt) {
        remove(evt.getServer().getRecipeManager());
    }

    @SubscribeEvent
    public void addReloadListener(final AddReloadListenerEvent evt) {
        evt.addListener(new ReloadListener<Void>() {
            @Override
            protected void apply(Void objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
                remove(evt.getDataPackRegistries().getRecipeManager());
            }

            @Override
            protected Void prepare(IResourceManager resourceManagerIn, IProfiler profilerIn) {
                return null;
            }
        });
    }

    private static void remove(RecipeManager mgr) {
        if (CommonConfig.REMOVE_WOODEN_TOOL_RECIPES.get()) {
            for (Item i : ForgeRegistries.ITEMS.getValues()) {
                if (i instanceof ToolItem) {
                    ToolItem t = (ToolItem) i;
                    if (t.getTier() == ItemTier.WOOD) {
                        removeRecipes(mgr, new ItemStack(t, 1));
                    }
                }
            }
        }
        if (CommonConfig.REMOVE_STONE_TOOL_RECIPES.get()) {
            for (Item i : ForgeRegistries.ITEMS.getValues()) {
                if (i instanceof ToolItem) {
                    ToolItem t = (ToolItem) i;
                    if (t.getTier() == ItemTier.STONE) {
                        removeRecipes(mgr, new ItemStack(t, 1));
                    }
                }
            }
        }
    }

    /**
     * Removes all crafting recipes with an output item contained in the specified
     * tag.
     *
     * @param recipeManager The recipe manager
     * @param stack         The ItemStack output of the recipe to remove
     */
    private static void removeRecipes(final RecipeManager recipeManager, final ItemStack stack) {
        final int recipesRemoved = removeRecipes(recipeManager, recipe -> {
            final ItemStack recipeOutput = recipe.getRecipeOutput();
            return recipeOutput.equals(stack, false)
                    && !(recipe.getSerializer() instanceof DamageItemRecipe.Serializer);
        });

        NaturalProgression.getInstance().LOGGER.info("Removed {} recipe(s) for {}", recipesRemoved,
                stack.getDisplayName().getString());
    }

    /**
     * Removes all crafting recipes with an output item contained in the specified
     * tag.
     *
     * @param recipeManager The recipe manager
     * @param tag           The tag
     */
    private static void removeRecipes(final RecipeManager recipeManager, final ITag.INamedTag<Item> tag) {
        try {
            final int recipesRemoved = removeRecipes(recipeManager, recipe -> {
                final ItemStack recipeOutput = recipe.getRecipeOutput();
                return !recipeOutput.isEmpty() && recipeOutput.getItem().isIn(tag)
                        && !(recipe.getSerializer() instanceof DamageItemRecipe.Serializer);
            });

            NaturalProgression.getInstance().LOGGER.info("Removed {} recipe(s) for tag {}", recipesRemoved,
                    tag.getName());
        } catch (IllegalStateException ex) {
            return;
        }
    }

    /**
     * Remove all crafting recipes that match the specified predicate.
     *
     * @param recipeManager The recipe manager
     * @param predicate     The predicate
     * @return The number of recipes removed
     */
    private static int removeRecipes(final RecipeManager recipeManager, final Predicate<IRecipe> predicate) {
        final Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> existingRecipes;
        try {
            @SuppressWarnings("unchecked")
            final Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipesMap = (Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>>) RECIPES
                    .get(recipeManager);
            existingRecipes = recipesMap;
        } catch (final IllegalAccessException e) {
            throw new RuntimeException("Couldn't get recipes map while removing recipes", e);
        }

        final Object2IntMap<IRecipeType<?>> removedCounts = new Object2IntOpenHashMap<>();
        final ImmutableMap.Builder<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> newRecipes = ImmutableMap
                .builder();

        existingRecipes.forEach((recipeType, existingRecipesForType) -> {
            final ImmutableMap<ResourceLocation, IRecipe<?>> newRecipesForType = existingRecipesForType.entrySet()
                    .stream().filter(entry -> !predicate.test(entry.getValue()))
                    .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));

            removedCounts.put(recipeType, existingRecipesForType.size() - newRecipesForType.size());
            newRecipes.put(recipeType, newRecipesForType);
        });

        final int removedCount = removedCounts.values().stream().reduce(0, Integer::sum);

        try {
            RECIPES.set(recipeManager, newRecipes.build());
        } catch (final IllegalAccessException e) {
            throw new RuntimeException("Couldn't replace recipes map while removing recipes", e);
        }

        return removedCount;
    }
}