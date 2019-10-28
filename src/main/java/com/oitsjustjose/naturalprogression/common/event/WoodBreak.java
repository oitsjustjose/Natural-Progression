package com.oitsjustjose.naturalprogression.common.event;

import javax.annotation.Nullable;

import com.oitsjustjose.naturalprogression.NaturalProgression;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WoodBreak
{
    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed event)
    {
        final SplinterSource splinterSource = new SplinterSource();

        if (event.getState() == null || event.getPlayer() == null)
        {
            return;
        }

        if (event.getState().getMaterial() == Material.WOOD)
        {
            // If the player **isn't** using an axe on a log, don't let them break it
            if (!event.getPlayer().getHeldItemMainhand().getToolTypes().contains(ToolType.AXE))
            {
                event.setNewSpeed(0F);

                event.getPlayer().sendStatusMessage(new TranslationTextComponent("natural-progression.wood.warning"),
                        true);
                // Random chance to even perform the hurt anim if the player is empty-handed
                if (event.getPlayer().getHeldItemMainhand().isEmpty() && event.getPlayer().getRNG().nextInt(25) == 1)
                {
                    // And when it's shown, random chance to actually hurt from "splintering"
                    if (event.getPlayer().getRNG().nextInt(10) == 1)
                    {
                        event.getPlayer().attackEntityFrom(splinterSource, 1F);
                    }
                    else
                    {
                        NaturalProgression.proxy.doHurtAnimation(event.getPlayer());
                    }
                }
            }
        }
    }

    public static class SplinterSource extends DamageSource
    {
        public SplinterSource()
        {
            super("splintering");
        }

        @Override
        @Nullable
        public Entity getTrueSource()
        {
            return null;
        }

        @Override
        public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn)
        {
            return new TranslationTextComponent("natural-progression.splintered.to.death",
                    entityLivingBaseIn.getDisplayName());
        }
    }
}