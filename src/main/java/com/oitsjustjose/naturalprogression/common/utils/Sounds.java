package com.oitsjustjose.naturalprogression.common.utils;

import com.oitsjustjose.naturalprogression.common.config.CommonConfig;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Objects;

public class Sounds
{
    private static final SoundEvent CRAFT_PLANK = new SoundEvent(new ResourceLocation(Constants.MODID, "craft_plank"));
    private static final SoundEvent CRAFT_OTHER = new SoundEvent(new ResourceLocation(Constants.MODID, "craft_other"));

    // Handles the sound for when planks or items are crafted
    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event)
    {
        if (CommonConfig.CRAFTING_SOUNDS.get() == CommonConfig.CraftingSounds.NONE)
        {
            return;
        }

        if (event.getPlayer() != null)
        {
            PlayerEntity player = event.getPlayer();
            float pitch = Math.min(1.0F, 0.5F + event.getPlayer().getRNG().nextFloat());
            if (CommonConfig.CRAFTING_SOUNDS.get() == CommonConfig.CraftingSounds.PLANKS)
            {
                if (ItemTags.PLANKS.contains(event.getCrafting().getItem()))
                {
                    player.playSound(Sounds.CRAFT_PLANK, 0.85F, pitch);
                }
            }
            else
            {
                if (event.getCrafting().getItem() instanceof BlockItem)
                {
                    BlockItem blockItem = (BlockItem) event.getCrafting().getItem();
                    if (blockItem.getBlock().getDefaultState().getMaterial() == Material.WOOD)
                    {
                        player.playSound(Sounds.CRAFT_PLANK, 0.85F, pitch);
                    }
                    else
                    {
                        player.playSound(Sounds.CRAFT_OTHER, 0.85F, pitch);
                    }
                }
                else if (Objects.requireNonNull(event.getCrafting().getItem().getRegistryName()).getPath().toLowerCase()
                        .contains("wood"))
                {
                    player.playSound(Sounds.CRAFT_PLANK, 0.85F, pitch);
                }
                else
                {
                    player.playSound(Sounds.CRAFT_OTHER, 0.85F, pitch);
                }
            }
        }
    }
}