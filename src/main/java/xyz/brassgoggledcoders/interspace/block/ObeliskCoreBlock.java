package xyz.brassgoggledcoders.interspace.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import xyz.brassgoggledcoders.interspace.tileentity.ObeliskConnectedTileEntity;
import xyz.brassgoggledcoders.interspace.tileentity.ObeliskControllerTileEntity;

import javax.annotation.Nullable;

public class ObeliskCoreBlock extends Block {
    public static EnumProperty<ObeliskCoreState> CORE_STATE = EnumProperty.create("core_state", ObeliskCoreState.class);

    public ObeliskCoreBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(CORE_STATE, ObeliskCoreState.INACTIVE));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(CORE_STATE);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(CORE_STATE) != ObeliskCoreState.INACTIVE;
    }

    @Override
    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        ObeliskCoreState coreState = state.get(CORE_STATE);
        switch (coreState) {
            case CONTROLLER:
                return new ObeliskControllerTileEntity();
            case CONNECTED:
                return new ObeliskConnectedTileEntity();
            default:
                return null;
        }
    }
}
