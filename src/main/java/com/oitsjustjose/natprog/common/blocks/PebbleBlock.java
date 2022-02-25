package com.oitsjustjose.natprog.common.blocks;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PebbleBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public PebbleBlock() {
        super(Properties.of(Material.REPLACEABLE_PLANT, MaterialColor.STONE)
                .strength(0.125F, 2F).sound(SoundType.STONE).dynamicShape().noCollission());
        this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, Boolean.FALSE));

    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getLevel().getBlockState(context.getClickedPos()).getBlock() == Blocks.WATER) {
            return this.defaultBlockState().setValue(WATERLOGGED, Boolean.TRUE);
        }
        return this.defaultBlockState();
    }

    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Vec3 offset = state.getOffset(worldIn, pos);
        return Shapes.create(0.37D, 0.0D, 0.37D, 0.69D, 0.065D, 0.69D).move(offset.x,
                offset.y, offset.z);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.fallOn(level, state, pos, entity, fallDistance);
        // One in ten chance for the sample to break when fallen on
        Random random = new Random();
        if (((int) fallDistance) > 0) {
            if (random.nextInt((int) fallDistance) > 5) {
                level.destroyBlock(pos, true);
            }
        }
    }

    @Nonnull
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn,
            BlockHitResult hit) {
        if (!player.isCrouching()) {
            worldIn.destroyBlock(pos, true);
            player.swing(handIn);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockState below = worldIn.getBlockState(pos.below());
        return below.isSolidRender(worldIn, pos.below());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    @Nonnull
    public Block.OffsetType getOffsetType() {
        return Block.OffsetType.XZ;
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
            boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if (!this.canSurvive(state, worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
        // Update the water from flowing to still or vice-versa
        if (state.getValue(WATERLOGGED)) {
            worldIn.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }
    }

    public BlockState asWaterlogged() {
        return this.defaultBlockState().setValue(WATERLOGGED, Boolean.TRUE);
    }
}
