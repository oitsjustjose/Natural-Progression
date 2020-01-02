package com.oitsjustjose.naturalprogression.common.utils;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.oitsjustjose.naturalprogression.NaturalProgression;
import com.oitsjustjose.naturalprogression.common.config.CommonConfig;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class PassableLeaves
{
    public static void init()
    {
        if (!CommonConfig.PASSABLE_LEAVES.get())
        {
            return;
        }
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        for (Block block : ForgeRegistries.BLOCKS.getValues())
        {
            if (block instanceof LeavesBlock)
            {
                if (reflect((LeavesBlock) block))
                {
                    NaturalProgression.getInstance().LOGGER.info("Made {} passable", block.getRegistryName());
                }
                else
                {
                    NaturalProgression.getInstance().LOGGER.info("Unable to make {} passable", block.getRegistryName());
                }
            }
        }
    }

    private static boolean reflect(LeavesBlock block)
    {
        try
        {
            final Field blocksMvmt = ObfuscationReflectionHelper.findField(Block.class,
                    "field_196274_w" /* blocksMovement */);
            blocksMvmt.setAccessible(true);
            blocksMvmt.setBoolean(block, false);
        }
        catch (IllegalArgumentException | IllegalAccessException ex)
        {
            NaturalProgression.getInstance().LOGGER.error(ex.getMessage());
            return false;
        }
        return true;
    }

    public static class EventHandler
    {
        private HashMap<Integer, Long> idToLongMap = new HashMap<>();

        @SubscribeEvent
        public void onLivingDo(LivingEvent.LivingUpdateEvent event)
        {
            if (!CommonConfig.PASSABLE_LEAVES.get())
            {
                return;
            }

            if (event.getEntityLiving() == null)
            {
                return;
            }

            /* I chose to split it up this way (into two different if/then/else's) to save performance if possible */

            BlockState stateAtFeet = event.getEntityLiving().getEntityWorld()
                    .getBlockState(event.getEntityLiving().getPosition());
            if (stateAtFeet.getBlock() instanceof LeavesBlock)
            {
                event.getEntityLiving().setMotion(event.getEntityLiving().getMotion().mul(.75, .75, .75));
                event.getEntityLiving().getPersistentData().putBoolean(Constants.MODID + ":fallingInLeaves", true);

                if (idToLongMap.containsKey(event.getEntityLiving().getEntityId()))
                {
                    if (isMoving(event.getEntityLiving().getMotion()))
                    {
                        if ((System.currentTimeMillis()
                                - idToLongMap.get(event.getEntityLiving().getEntityId())) >= 500)
                        {
                            event.getEntityLiving().getEntityWorld().playSound(event.getEntityLiving().posX,
                                    event.getEntityLiving().posY, event.getEntityLiving().posZ,
                                    SoundEvents.BLOCK_GRASS_STEP, SoundCategory.PLAYERS, .5F, .9F, true);
                            idToLongMap.put(event.getEntityLiving().getEntityId(), System.currentTimeMillis());

                        }
                    }
                }
                else
                {
                    event.getEntityLiving().getEntityWorld().playSound(event.getEntityLiving().posX,
                            event.getEntityLiving().posY, event.getEntityLiving().posZ, SoundEvents.BLOCK_GRASS_STEP,
                            SoundCategory.PLAYERS, .5F, .9F, true);
                    idToLongMap.put(event.getEntityLiving().getEntityId(), System.currentTimeMillis());
                }
            }
            else
            {
                BlockState stateAtEyes = event.getEntityLiving().getEntityWorld()
                        .getBlockState(event.getEntityLiving().getPosition().up());
                if (stateAtEyes.getBlock() instanceof LeavesBlock)
                {
                    event.getEntityLiving().setMotion(event.getEntityLiving().getMotion().mul(.75, .75, .75));
                    event.getEntityLiving().getPersistentData().putBoolean(Constants.MODID + ":fallingInLeaves", true);

                    if (idToLongMap.containsKey(event.getEntityLiving().getEntityId()))
                    {
                        if (isMoving(event.getEntityLiving().getMotion()))
                        {
                            if ((System.currentTimeMillis()
                                    - idToLongMap.get(event.getEntityLiving().getEntityId())) >= 500)
                            {
                                event.getEntityLiving().getEntityWorld().playSound(event.getEntityLiving().posX,
                                        event.getEntityLiving().posY, event.getEntityLiving().posZ,
                                        SoundEvents.BLOCK_GRASS_STEP, SoundCategory.PLAYERS, .5F, .9F, true);
                                idToLongMap.put(event.getEntityLiving().getEntityId(), System.currentTimeMillis());
                            }
                        }
                    }
                    else
                    {
                        event.getEntityLiving().getEntityWorld().playSound(event.getEntityLiving().posX,
                                event.getEntityLiving().posY, event.getEntityLiving().posZ,
                                SoundEvents.BLOCK_GRASS_STEP, SoundCategory.PLAYERS, .5F, .9F, true);
                        idToLongMap.put(event.getEntityLiving().getEntityId(), System.currentTimeMillis());
                    }
                }
                else
                {
                    event.getEntityLiving().getPersistentData().putBoolean(Constants.MODID + ":fallingInLeaves", false);
                }
            }

        }

        private boolean isMoving(Vec3d vector)
        {
            return (vector.x > 0 || vector.y > 0 || vector.z > 0);
        }

        @SubscribeEvent
        public void onHurt(LivingHurtEvent event)
        {
            if (event.getEntity() == null || event.getSource() != DamageSource.FALL)
            {
                return;
            }

            if (event.getEntityLiving().getPersistentData().getBoolean(Constants.MODID + ":fallingInLeaves"))
            {
                event.getEntityLiving().getPersistentData().putBoolean(Constants.MODID + ":fallingInLeaves", false);
                event.setCanceled(true);
            }
        }
    }
}