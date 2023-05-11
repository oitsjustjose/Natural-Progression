package com.oitsjustjose.natprog.common.data.damageitem;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DamageItemRecipeSerializer implements RecipeSerializer<DamageItemRecipe> {
    @Override
    public @NotNull DamageItemRecipe fromJson(@NotNull ResourceLocation resloc, @NotNull JsonObject obj) {
        var group = GsonHelper.getAsString(obj, "group");
        var ingredients = readIngredients(GsonHelper.getAsJsonArray(obj, "ingredients"));

        if (ingredients.isEmpty()) {
            throw new JsonParseException("No ingredients for natprog:damage_tools");
        }

        ItemStack stack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(obj, "result"));
        return new DamageItemRecipe(resloc, group, CraftingBookCategory.MISC, stack, ingredients);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull DamageItemRecipe recipe) {
        buf.writeUtf(recipe.getGroup());
        buf.writeVarInt(recipe.getIngredients().size());

        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.toNetwork(buf);
        }

        buf.writeItemStack(recipe.getResultItem(), false);
    }

    @Override
    public @Nullable DamageItemRecipe fromNetwork(@NotNull ResourceLocation rl, @NotNull FriendlyByteBuf buf) {
        var s = buf.readUtf(32767);
        var i = buf.readVarInt();
        var ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

        ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buf));

        ItemStack stack = buf.readItem();
        return new DamageItemRecipe(rl, s, CraftingBookCategory.MISC, stack, ingredients);
    }

    private static NonNullList<Ingredient> readIngredients(JsonArray jsonArray) {
        NonNullList<Ingredient> ingredients = NonNullList.create();

        for (int i = 0; i < jsonArray.size(); i++) {
            var ingr = Ingredient.fromJson(jsonArray.get(i));
            if (!ingr.isEmpty()) {
                ingredients.add(ingr);
            }
        }
        return ingredients;
    }
}
