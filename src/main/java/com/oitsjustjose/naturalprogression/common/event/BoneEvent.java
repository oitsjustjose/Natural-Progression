package com.oitsjustjose.naturalprogression.common.event;

import com.oitsjustjose.naturalprogression.common.config.CommonConfig;
import com.oitsjustjose.naturalprogression.common.items.NaturalProgressionItems;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

public class BoneEvent
{
    @SubscribeEvent
    public void onItemRightClick(PlayerInteractEvent.RightClickItem event)
    {
        Hand boneHand = event.getPlayer().getHeldItemMainhand().getItem() == Items.BONE ? Hand.MAIN_HAND
                : event.getPlayer().getHeldItemOffhand().getItem() == Items.BONE ? Hand.OFF_HAND : null;
        Hand flintHand = event.getPlayer().getHeldItemMainhand().getItem() == Items.FLINT ? Hand.MAIN_HAND
                : event.getPlayer().getHeldItemOffhand().getItem() == Items.FLINT ? Hand.OFF_HAND : null;

        if (boneHand == null || flintHand == null)
        {
            return;
        }

        event.getPlayer().swingArm(boneHand);

        // TODO: Play sounds

        if (event.getPlayer().getRNG().nextInt(100) <= CommonConfig.BONE_SHARD_CHANCE.get())
        {
            int count = event.getPlayer().getRNG().nextInt(2) + 1;
            ItemHandlerHelper.giveItemToPlayer(event.getPlayer(),
                    new ItemStack(NaturalProgressionItems.boneShard, count));
            event.getPlayer().getHeldItem(boneHand).shrink(1);
            event.getPlayer().playSound(SoundEvents.ENTITY_SKELETON_HURT, 1F, 1F);
        }
        else
        {
            event.getPlayer().playSound(SoundEvents.ENTITY_SKELETON_HURT, 1F, 1F);
        }
    }

    @SubscribeEvent
    public void onDirtBreak(BlockEvent.BreakEvent event)
    {
        if (event.getState().getBlock() == Blocks.DIRT)
        {
            if (event.getPlayer().getRNG().nextInt(100) <= 100)
            {
                Block.spawnAsEntity(event.getWorld().getWorld(), event.getPos(), new ItemStack(Items.BONE));
            }
        }
    }
}
