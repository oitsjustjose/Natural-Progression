package com.oitsjustjose.natprog.common.event;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import com.oitsjustjose.natprog.common.config.CommonConfig;
import com.oitsjustjose.natprog.common.items.NatProgItems;
import com.oitsjustjose.natprog.common.utils.Constants;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
        if (playersLastRightClicked.containsKey(event.getPlayer().getUUID())) {
            if ((System.currentTimeMillis()
                    - playersLastRightClicked.get(event.getPlayer().getUUID())) < 200) {
                return;
            }
        }

        InteractionHand boneHand = event.getPlayer().getMainHandItem().getItem() == Items.BONE
                ? InteractionHand.MAIN_HAND
                : event.getPlayer().getOffhandItem().getItem() == Items.BONE
                        ? InteractionHand.OFF_HAND
                        : null;
        InteractionHand flintHand = event.getPlayer().getMainHandItem().getItem() == Items.FLINT
                ? InteractionHand.MAIN_HAND
                : event.getPlayer().getOffhandItem().getItem() == Items.FLINT
                        ? InteractionHand.OFF_HAND
                        : null;

        if (boneHand == null || flintHand == null) {
            return;
        }

        event.getPlayer().swing(flintHand);

        if (event.getPlayer().getRandom().nextInt(100) <= CommonConfig.BONE_SHARD_CHANCE.get()) {
            if (event.getWorld().isClientSide()) {
                event.getPlayer().playSound(SoundEvents.PARROT_IMITATE_SKELETON, 1F, 0.7F);
            } else {
                int count = event.getPlayer().getRandom().nextInt(2) + 1;
                ItemHandlerHelper.giveItemToPlayer(event.getPlayer(),
                        new ItemStack(NatProgItems.boneShard, count));
                event.getPlayer().getItemInHand(boneHand).shrink(1);
            }
        }

        playersLastRightClicked.put(event.getPlayer().getUUID(), System.currentTimeMillis());
    }

    @SubscribeEvent
    public void livingDrop(LivingDropsEvent event) {
        if (CommonConfig.BONE_DROP_CHANCE.get() == 0) {
            return;
        }

        boolean exists = event.getEntity().getType().is(Constants.BONE_DROP);

        if (exists || CommonConfig.ALL_ENTITIES_DROP_BONES.get()) {
            if (rand.nextInt(100) < CommonConfig.BONE_DROP_CHANCE.get()) {
                ItemEntity drop = new ItemEntity(event.getEntityLiving().getLevel(),
                        event.getEntityLiving().getX(), event.getEntityLiving().getY(),
                        event.getEntityLiving().getZ(), new ItemStack(Items.BONE));
                event.getDrops().add(drop);
            }
        }
    }
}
