package com.oitsjustjose.natprog.common.items;

import com.oitsjustjose.natprog.common.config.CommonConfig;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class PebbleItem extends BlockItem {
    public PebbleItem(Block blockForm, Item.Properties props) {
        super(blockForm, props);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer().getOffhandItem().getItem() instanceof PebbleItem) {
            if (context.getPlayer().getMainHandItem().getItem() instanceof PebbleItem) {
                return use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
            }
        }
        return super.useOn(context);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand hand) {
        ItemStack mainHand = playerIn.getMainHandItem();
        ItemStack offHand = playerIn.getOffhandItem();
        if (!mainHand.isEmpty() && !offHand.isEmpty()) {
            if (mainHand.getItem() instanceof PebbleItem
                    && offHand.getItem() instanceof PebbleItem) {
                InteractionHand swingArm = playerIn.getRandom().nextBoolean() ? InteractionHand.MAIN_HAND
                        : InteractionHand.OFF_HAND;

                if (worldIn.isClientSide()) {
                    playerIn.swing(swingArm);
                } else {
                    // 5% chance of knapping actually working:
                    if (playerIn.getRandom().nextInt(100) <= 5) {
                        playerIn.getItemInHand(swingArm).shrink(1);

                        if (playerIn.getRandom().nextInt(100) < CommonConfig.FLINT_CHANCE.get()) {
                            worldIn.playSound(null, playerIn.getOnPos(),
                                    SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS, 1.0F,
                                    0.5F);
                            ItemHandlerHelper.giveItemToPlayer(playerIn,
                                    new ItemStack(Items.FLINT, 1));
                        } else {
                            worldIn.playSound(null, playerIn.getOnPos(),
                                    SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 0.5F,
                                    0.75F);
                        }
                    } else {
                        worldIn.playSound(null, playerIn.getOnPos(), SoundEvents.STONE_HIT,
                                SoundSource.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, playerIn.getItemInHand(hand));
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }
}
