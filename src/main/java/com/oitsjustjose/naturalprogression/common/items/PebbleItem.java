package com.oitsjustjose.naturalprogression.common.items;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.oitsjustjose.naturalprogression.common.config.CommonConfig;
import com.oitsjustjose.naturalprogression.common.utils.NaturalProgressionGroup;

import org.lwjgl.glfw.GLFW;

import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;

public class PebbleItem extends BlockItem
{
    public PebbleItem(Block blockForm)
    {
        super(blockForm, new Item.Properties().group(NaturalProgressionGroup.getInstance()));
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
            @Nonnull ITooltipFlag flagIn)
    {
        if (Screen.hasShiftDown())
        {
            if (Screen.hasAltDown())
            {
                tooltip.add(new TranslationTextComponent("natural-progression.pebble.help.1"));
                tooltip.add(new TranslationTextComponent("natural-progression.pebble.help.2"));
            }
            else
            {
                tooltip.add(new TranslationTextComponent("natural-progression.pebble.desc.1"));
                tooltip.add(new TranslationTextComponent("natural-progression.pebble.desc.2"));
            }
        }
        else
        {
            tooltip.add(new TranslationTextComponent("natural-progression.pebble.shift"));
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        if (context.getPlayer().getHeldItemOffhand().getItem() instanceof PebbleItem)
        {
            if (context.getPlayer().getHeldItemMainhand().getItem() instanceof PebbleItem)
            {
                return onItemRightClick(context.getWorld(), context.getPlayer(), context.getHand()).getType();
            }
        }
        return super.onItemUse(context);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn)
    {
        ItemStack mainHand = playerIn.getHeldItemMainhand();
        ItemStack offHand = playerIn.getHeldItemOffhand();
        if (!mainHand.isEmpty() && !offHand.isEmpty())
        {
            if (mainHand.getItem() instanceof PebbleItem && offHand.getItem() instanceof PebbleItem)
            {
                Hand swingArm = playerIn.getRNG().nextBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND;

                if (worldIn.isRemote())
                {
                    playerIn.swingArm(swingArm);
                }
                else
                {
                    // 5% chance of knapping actually working:
                    if (playerIn.getRNG().nextInt(100) <= 5)
                    {
                        playerIn.getHeldItem(swingArm).shrink(1);

                        if (playerIn.getRNG().nextInt(100) < CommonConfig.FLINT_CHANCE.get())
                        {
                            worldIn.playSound(null, playerIn.getPosition(), SoundEvents.ITEM_FLINTANDSTEEL_USE,
                                    SoundCategory.PLAYERS, 1.0F, 0.5F);
                            ItemHandlerHelper.giveItemToPlayer(playerIn, new ItemStack(Items.FLINT, 1));
                        }
                        else
                        {
                            worldIn.playSound(null, playerIn.getPosition(), SoundEvents.ITEM_SHIELD_BREAK,
                                    SoundCategory.PLAYERS, 0.5F, 0.75F);
                        }
                    }
                    else
                    {
                        worldIn.playSound(null, playerIn.getPosition(), SoundEvents.BLOCK_STONE_HIT,
                                SoundCategory.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }
        }
        return new ActionResult<>(ActionResultType.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book)
    {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return false;
    }

    @Override
    public int getItemEnchantability()
    {
        return 0;
    }
}