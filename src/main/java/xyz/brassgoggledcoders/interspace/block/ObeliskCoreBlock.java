package xyz.brassgoggledcoders.interspace.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import xyz.brassgoggledcoders.interspace.content.tag.InterspaceBlockTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class ObeliskCoreBlock extends Block {
    public ObeliskCoreBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.ATTACHED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@Nonnull BlockItemUseContext context) {
        return this.getDefaultState().with(BlockStateProperties.ATTACHED, this.isValid(context.getWorld(), context.getPos()));
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        boolean valid = this.isValid(world, pos);
        if (state.get(BlockStateProperties.ATTACHED) != valid) {
            world.setBlockState(pos, state.with(BlockStateProperties.ATTACHED, valid));
        }
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world,
                                          BlockPos currentPos, BlockPos facingPos) {
        return state.with(BlockStateProperties.ATTACHED, (facing.getAxis() == Direction.Axis.Y ||
                facingState.getBlock().isIn(InterspaceBlockTags.NAFASI)) && this.isValid(world, currentPos));
    }

    private boolean isValid(IWorld world, BlockPos pos) {

        boolean valid = BlockPos.getAllInBox(pos.add(1, 0, 1), pos.add(-1, 0, -1))
                .allMatch(blockPos -> world.getBlockState(blockPos).getBlock().isIn(InterspaceBlockTags.NAFASI));
        BlockPos up = pos.up();
        return valid && (world.getBlockState(pos.up()).getBlock() == this || this.isValid(world, up));
    }
}
