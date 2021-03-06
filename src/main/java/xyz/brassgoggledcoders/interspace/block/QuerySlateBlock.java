package xyz.brassgoggledcoders.interspace.block;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import xyz.brassgoggledcoders.interspace.content.tag.InterspaceBlockTags;
import xyz.brassgoggledcoders.interspace.tileentity.QuerySlateTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;

public class QuerySlateBlock extends Block {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    private static final EnumMap<Direction, VoxelShape> SHAPES = Util.make(Maps.newEnumMap(Direction.class), map -> {
        map.put(Direction.DOWN, Block.makeCuboidShape(0D, 12D, 0D, 16D, 16D, 16D));
        map.put(Direction.UP, Block.makeCuboidShape(0D, 0D, 0D, 16D, 4D, 16D));
        map.put(Direction.SOUTH, Block.makeCuboidShape(0D, 0D, 0D, 16D, 16D, 4D));
        map.put(Direction.NORTH, Block.makeCuboidShape(0D, 0D, 12D, 16D, 16D, 16D));
        map.put(Direction.WEST, Block.makeCuboidShape(12D, 0D, 0D, 16D, 16D, 16D));
        map.put(Direction.EAST, Block.makeCuboidShape(0D, 0D, 0D, 4D, 16D, 16D));
    });

    public QuerySlateBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        VoxelShape shape = SHAPES.get(state.get(FACING));
        return shape != null ? shape : super.getShape(state, world, pos, context);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader reader) {
        return new QuerySlateTileEntity();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@Nonnull BlockItemUseContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement != null) {
            Direction facing = context.getFace();
            BlockPos blockPos = context.getPos();
            BlockState placedAgainst = context.getWorld().getBlockState(blockPos.offset(facing.getOpposite()));
            if (placedAgainst.getBlock().isIn(InterspaceBlockTags.OBELISK)) {
                return stateForPlacement.with(FACING, facing);
            }
        }
        return null;
    }

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.offset(state.get(FACING).getOpposite())).isIn(InterspaceBlockTags.OBELISK);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public BlockState updatePostPlacement(BlockState blockState, Direction facing, BlockState facingState, IWorld world,
                                          BlockPos currentPos, BlockPos facingPos) {
        return facing.getOpposite() == blockState.get(FACING) && !blockState.isValidPosition(world, currentPos) ?
                Blocks.AIR.getDefaultState() : blockState;
    }
}
