package com.oitsjustjose.natprog.common.event;

import com.oitsjustjose.natprog.NatProg;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TwigPlacement {
    @SubscribeEvent
    public void registerEvent(PlayerInteractEvent.RightClickBlock event) {
        if (!CommonConfig.ARE_TWIGS_PLACEABLE.get()) return;

        if (event.getItemStack().getItem() != Items.STICK) return;
        if (event.getHitVec().getDirection() != Direction.UP) return;

        var twig = NatProg.getInstance().REGISTRY.twigBlock.get();
        var whereToPlace = event.getHitVec().getBlockPos().above();
        var toPlace = twig.defaultBlockState();
        var at = event.getLevel().getBlockState(whereToPlace);
        var below = event.getLevel().getBlockState(event.getHitVec().getBlockPos());

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
