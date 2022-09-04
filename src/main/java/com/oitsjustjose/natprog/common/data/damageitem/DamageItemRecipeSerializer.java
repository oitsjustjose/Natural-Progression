package com.oitsjustjose.natprog.common.data.damageitem;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.oitsjustjose.natprog.NatProg;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DamageItemRecipeSerializer implements RecipeSerializer<DamageItemRecipe> {
    @Override
    public @NotNull DamageItemRecipe fromJson(@NotNull ResourceLocation rl, @NotNull JsonObject obj) {
        String s = GsonHelper.getAsString(obj, "group");
        NonNullList<Ingredient> nonnulllist = readIngredients(GsonHelper.getAsJsonArray(obj, "ingredients"));

        if (nonnulllist.isEmpty()) {
            throw new JsonParseException("No ingredients for natprog:damage_tools");
        }

        ItemStack stack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(obj, "result"));
        return new DamageItemRecipe(rl, s, stack, nonnulllist);
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
        String s = buf.readUtf(32767);
        int i = buf.readVarInt();
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

        for (int j = 0; j < nonnulllist.size(); ++j) {
            nonnulllist.set(j, Ingredient.fromNetwork(buf));
        }

        ItemStack stack = buf.readItem();
        return new DamageItemRecipe(rl, s, stack, nonnulllist);
    }

    private ItemStack deserialize(JsonObject parent, String key) {
        try {
            return ShapedRecipe.itemStackFromJson(parent.getAsJsonObject(key));
        } catch (JsonSyntaxException ex) {
            NatProg.getInstance().LOGGER.error("Item {} does not exist", parent.get(key).toString());
            ex.printStackTrace();
        }
        return ItemStack.EMPTY;
    }

    private static NonNullList<Ingredient> readIngredients(JsonArray jsonArray) {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();

        for (int i = 0; i < jsonArray.size(); i++) {
            Ingredient ingr = Ingredient.fromJson(jsonArray.get(i));
            if (!ingr.isEmpty()) {
                nonnulllist.add(ingr);
            }
        }
        return nonnulllist;
    }
}
