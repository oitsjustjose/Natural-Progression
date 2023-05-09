package com.oitsjustjose.natprog.common.data.damageitem;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * @author Credit to Integral for the Enchantment Transpose Recipe as a guide
 * Special shapeless recipe that allows use of Tools, and damages the tool properly
 */

public class DamageItemRecipe extends ShapelessRecipe {
    private final NonNullList<Ingredient> inputs;
    private final ItemStack output;

    public DamageItemRecipe(ResourceLocation id, String group, ItemStack output, NonNullList<Ingredient> inputs) {
        super(id, group, output, inputs);
        this.inputs = inputs;
        this.output = output;
    }

    @Override
    @Nonnull
    public ItemStack assemble(@NotNull CraftingContainer p_44260_) {
        return this.output.copy();
    }

    @Override
    public boolean matches(CraftingContainer inv, @NotNull Level level) {
        var stackedcontents = new StackedContents();
        int i = 0;

        for (int j = 0; j < inv.getContainerSize(); ++j) {
            ItemStack itemstack = inv.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                stackedcontents.accountStack(itemstack, 1);
            }
        }

        return i == this.inputs.size() && stackedcontents.canCraft(this, null);
    }

    @Override
    @Nonnull
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        var ret = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < ret.size(); ++i) {
            var stack = inv.getItem(i);

            if (stack.isEmpty()) continue;
            if (!stack.getItem().isDamageable(stack)) continue;

            var savedStack = stack.copy();
            var random = new Random();
            var unbreakingLvl = savedStack.getEnchantmentLevel(Enchantments.UNBREAKING);
            var shouldAttemptDmg = unbreakingLvl <= 0 || ((1 + random.nextInt(5)) <= unbreakingLvl);

            if (savedStack.getDamageValue() < savedStack.getMaxDamage()) {
                if (shouldAttemptDmg) {
                    savedStack.setDamageValue(savedStack.getDamageValue() + 1);
                }
                ret.set(i, savedStack);
            } else {
                ret.set(i, ItemStack.EMPTY);
            }
        }

        return ret;
    }
}
