package com.oitsjustjose.natprog.common.event;

import com.oitsjustjose.natprog.common.config.CommonConfig;
import com.oitsjustjose.natprog.common.utils.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class BoneEvent {
    private HashMap<UUID, Long> playersLastRightClicked = new HashMap<>();
    private Random rand = new Random();

    @SubscribeEvent
    public void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
        if (playersLastRightClicked.containsKey(event.getEntity().getUUID())) {
            if ((System.currentTimeMillis()
                    - playersLastRightClicked.get(event.getEntity().getUUID())) < 200) {
                return;
            }
        }

        InteractionHand boneHand = event.getEntity().getMainHandItem().getItem() == Items.BONE
                ? InteractionHand.MAIN_HAND
                : event.getEntity().getOffhandItem().getItem() == Items.BONE
                ? InteractionHand.OFF_HAND
                : null;
        InteractionHand flintHand = event.getEntity().getMainHandItem().getItem() == Items.FLINT
                ? InteractionHand.MAIN_HAND
                : event.getEntity().getOffhandItem().getItem() == Items.FLINT
                ? InteractionHand.OFF_HAND
                : null;

        if (boneHand == null || flintHand == null) {
            return;
        }

        event.getEntity().swing(flintHand);

        if (event.getEntity().getRandom().nextInt(100) <= CommonConfig.BONE_SHARD_CHANCE.get()) {
            if (event.getLevel().isClientSide()) {
                event.getEntity().playSound(SoundEvents.PARROT_IMITATE_SKELETON, 1F, 0.7F);
            } else {
                int count = event.getEntity().getRandom().nextInt(2) + 1;
                Item shard = ForgeRegistries.ITEMS.getValue(new ResourceLocation(Constants.MODID, "bone_shard"));
                ItemHandlerHelper.giveItemToPlayer(event.getEntity(), new ItemStack(shard, count));
                event.getEntity().getItemInHand(boneHand).shrink(1);
            }
        }

        playersLastRightClicked.put(event.getEntity().getUUID(), System.currentTimeMillis());
    }

    @SubscribeEvent
    public void livingDrop(LivingDropsEvent event) {
        if (CommonConfig.BONE_DROP_CHANCE.get() == 0) {
            return;
        }

        boolean exists = event.getEntity().getType().is(Constants.BONE_DROP);

        if (exists || CommonConfig.ALL_ENTITIES_DROP_BONES.get()) {
            if (rand.nextInt(100) < CommonConfig.BONE_DROP_CHANCE.get()) {
                ItemEntity drop = new ItemEntity(event.getEntity().getLevel(),
                        event.getEntity().getX(), event.getEntity().getY(),
                        event.getEntity().getZ(), new ItemStack(Items.BONE));
                event.getDrops().add(drop);
            }
        }
    }
}
