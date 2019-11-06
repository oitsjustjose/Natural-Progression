package com.oitsjustjose.naturalprogression.common.event;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.oitsjustjose.naturalprogression.NaturalProgression;
import com.oitsjustjose.naturalprogression.common.config.CommonConfig;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class StoneBreak
{
    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed event)
    {
        final BrokenHandSource brokenHandSource = new BrokenHandSource();
        final List<Material> hardMaterials = Lists.asList(Material.ROCK, new Material[]
        { Material.IRON, Material.ANVIL });

        if (event.getState() == null || event.getPlayer() == null)
        {
            return;
        }

        if (hardMaterials.contains(event.getState().getMaterial()))
        {
            // If the player **isn't** using a pickaxe on a hard block, don't let them break it
            if (!event.getPlayer().getHeldItemMainhand().getToolTypes().contains(ToolType.PICKAXE))
            {
                event.setNewSpeed(0F);

                if (CommonConfig.SHOW_BREAKING_HELP.get())
                {
                    event.getPlayer()
                            .sendStatusMessage(new TranslationTextComponent("natural-progression.stone.warning"), true);
                }
                // Random chance to even perform the hurt anim if the player is empty-handed
                if (event.getPlayer().getHeldItemMainhand().isEmpty() && event.getPlayer().getRNG().nextInt(25) == 1)
                {
                    // And when it's shown, random chance to actually hurt from breaking bones
                    if (event.getPlayer().getRNG().nextInt(2) == 1)
                    {
                        event.getPlayer().attackEntityFrom(brokenHandSource, 1F);
                    }
                    else
                    {
                        NaturalProgression.proxy.doHurtAnimation(event.getPlayer());
                    }
                }
            }
        }
    }

    public static class BrokenHandSource extends DamageSource
    {
        public BrokenHandSource()
        {
            super("broken hand");
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
            return new TranslationTextComponent("natural-progression.broken.bones",
                    entityLivingBaseIn.getDisplayName());
        }
    }
}