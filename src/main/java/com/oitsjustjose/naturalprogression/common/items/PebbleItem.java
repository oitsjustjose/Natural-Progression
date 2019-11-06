package com.oitsjustjose.naturalprogression.common.items;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.oitsjustjose.naturalprogression.common.utils.NaturalProgressionGroup;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
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
        tooltip.add(new TranslationTextComponent("natural-progression.hit.together"));
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
                if (worldIn.isRemote())
                {
                    playerIn.swingArm(playerIn.getRNG().nextBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND);
                }
                else
                {
                    // 5% chance of knapping actually working:
                    if (playerIn.getRNG().nextInt(100) <= 5)
                    {
                        playerIn.getHeldItemMainhand().shrink(1);
                        playerIn.getHeldItemOffhand().shrink(1);

                        worldIn.playSound(null, playerIn.getPosition(), SoundEvents.ITEM_SHIELD_BREAK,
                                SoundCategory.PLAYERS, 0.75F, 0.75F);

                        ItemHandlerHelper.giveItemToPlayer(playerIn, new ItemStack(Items.FLINT, 1));
                    }
                    else
                    {
                        worldIn.playSound(null, playerIn.getPosition(), SoundEvents.BLOCK_STONE_HIT,
                                SoundCategory.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}