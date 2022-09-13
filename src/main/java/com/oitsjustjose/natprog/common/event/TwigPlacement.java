package com.oitsjustjose.natprog.common.event;

import com.oitsjustjose.natprog.common.blocks.TwigBlock;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import com.oitsjustjose.natprog.common.utils.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class TwigPlacement {
    @SubscribeEvent
    public void registerEvent(PlayerInteractEvent.RightClickBlock event) {
        if (!CommonConfig.ARE_TWIGS_PLACEABLE.get()) return;

        if (event.getItemStack().getItem() == Items.STICK) {
            if (event.getHitVec().getDirection() == Direction.UP) {
                Block twig = Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(Constants.MODID, "twigs")));
                BlockPos whereToPlace = event.getHitVec().getBlockPos().above();
                BlockState toPlace = twig.defaultBlockState();
                BlockState at = event.getLevel().getBlockState(whereToPlace);
                BlockState below = event.getLevel().getBlockState(event.getHitVec().getBlockPos());

                if (!below.isSolidRender(event.getLevel(), event.getHitVec().getBlockPos())) return;
                if (at.getBlock() == twig) return;

                if (at.getFluidState().is(Fluids.WATER)) {
                    toPlace = toPlace.setValue(BlockStateProperties.WATERLOGGED, Boolean.TRUE);
                }

                event.getLevel().setBlock(whereToPlace, toPlace, 2 | 16);
                event.getLevel().playSound(null, whereToPlace.getX(), whereToPlace.getY(), whereToPlace.getZ(), SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1F, 1F);
                event.getEntity().swing(event.getHand());
                if (!event.getEntity().isCreative()) {
                    event.getItemStack().shrink(1);
                }
            }
        }
    }
}
