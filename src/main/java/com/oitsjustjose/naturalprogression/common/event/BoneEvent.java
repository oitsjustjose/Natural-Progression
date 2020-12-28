package com.oitsjustjose.naturalprogression.common.event;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import com.oitsjustjose.naturalprogression.common.config.CommonConfig;
import com.oitsjustjose.naturalprogression.common.items.NaturalProgressionItems;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class BoneEvent {
    private HashMap<UUID, Long> playersLastRightClicked = new HashMap<>();
    private Random rand = new Random();

    @SubscribeEvent
    public void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
        if (playersLastRightClicked.containsKey(event.getPlayer().getUniqueID())) {
            if ((System.currentTimeMillis() - playersLastRightClicked.get(event.getPlayer().getUniqueID())) < 200) {
                return;
            }
        }

        Hand boneHand = event.getPlayer().getHeldItemMainhand().getItem() == Items.BONE ? Hand.MAIN_HAND
                : event.getPlayer().getHeldItemOffhand().getItem() == Items.BONE ? Hand.OFF_HAND : null;
        Hand flintHand = event.getPlayer().getHeldItemMainhand().getItem() == Items.FLINT ? Hand.MAIN_HAND
                : event.getPlayer().getHeldItemOffhand().getItem() == Items.FLINT ? Hand.OFF_HAND : null;

        if (boneHand == null || flintHand == null) {
            return;
        }

        event.getPlayer().swingArm(flintHand);

        if (event.getPlayer().getRNG().nextInt(100) <= CommonConfig.BONE_SHARD_CHANCE.get()) {
            if (event.getWorld().isRemote) {
                event.getPlayer().playSound(SoundEvents.ENTITY_PARROT_IMITATE_SKELETON, 1F, 0.7F);
            } else {
                int count = event.getPlayer().getRNG().nextInt(2) + 1;
                ItemHandlerHelper.giveItemToPlayer(event.getPlayer(),
                        new ItemStack(NaturalProgressionItems.boneShard, count));
                event.getPlayer().getHeldItem(boneHand).shrink(1);
            }
        }

        playersLastRightClicked.put(event.getPlayer().getUniqueID(), System.currentTimeMillis());
    }

    @SubscribeEvent
    public void livingDrop(LivingDropsEvent event) {
        if (CommonConfig.BONE_DROP_CHANCE.get() == 0) {
            return;
        }

        ResourceLocation loc = ForgeRegistries.ENTITIES.getKey(event.getEntityLiving().getType());

        CommonConfig.BONE_DROP_ENTITIES.get().forEach((name) -> {
            if (name.equals(loc.toString())) {
                if (rand.nextInt(100) < CommonConfig.BONE_DROP_CHANCE.get()) {
                    ItemEntity drop = new ItemEntity(event.getEntityLiving().world, event.getEntityLiving().getPosX(),
                            event.getEntityLiving().getPosY(), event.getEntityLiving().getPosZ(),
                            new ItemStack(Items.BONE));
                    event.getDrops().add(drop);
                }
            }
        });
    }
}