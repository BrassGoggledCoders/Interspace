package xyz.brassgoggledcoders.interspace.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullLazy;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatial;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatialChunk;
import xyz.brassgoggledcoders.interspace.block.ObeliskCoreBlock;
import xyz.brassgoggledcoders.interspace.block.ObeliskCoreState;
import xyz.brassgoggledcoders.interspace.content.InterspaceTileEntities;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class ObeliskControllerTileEntity extends PassThroughSpatialTileEntity<ISpatialChunk> implements ITickableTileEntity {

    private final NonNullLazy<List<ObeliskCapabilityHandler>> obeliskHandlers;

    private int timeSinceLastCheck = 20;

    private Random random = new Random();

    public ObeliskControllerTileEntity() {
        super(InterspaceTileEntities.OBELISK_CONTROLLER.get());
        this.obeliskHandlers = NonNullLazy.of(this::setupObeliskHandlers);
    }

    @Nonnull
    private List<ObeliskCapabilityHandler> setupObeliskHandlers() {
        return InterspaceAPI.getObeliskHandlers()
                .stream()
                .map(this::createObeliskHandler)
                .filter(ObeliskCapabilityHandler::isPresent)
                .collect(Collectors.toList());
    }

    private ObeliskCapabilityHandler createObeliskHandler(Capability<?> capability) {
        return new ObeliskCapabilityHandler(capability, this::getCapability, this::supplyCoolDown);
    }

    private int supplyCoolDown() {
        return this.random.nextInt(40);
    }

    @Override
    public void setPos(@Nonnull BlockPos blockPos) {
        super.setPos(blockPos);
        this.random = new Random(blockPos.toLong());
    }

    @Override
    protected LazyOptional<ISpatialChunk> getCapabilityForPassThrough() {
        return this.getWorld().getChunkAt(this.pos).getCapability(InterspaceAPI.SPATIAL_CHUNK);
    }

    @Override
    public void tick() {
        timeSinceLastCheck--;
        if (timeSinceLastCheck <= 0) {
            if (!world.isRemote()) {
                checkStructure();
            }
            timeSinceLastCheck = 100 + world.rand.nextInt(10);
        }

        if (!world.isRemote()) {
            this.getSpatial().ifPresent(this::tickWithSpatial);
        }
    }

    private void tickWithSpatial(@Nonnull ISpatial spatial) {
        for (ObeliskCapabilityHandler handler: obeliskHandlers.get()) {
            handler.tick(spatial);
        }
    }

    public void checkStructure() {
        List<Pair<BlockPos, Boolean>> checkedPositions = checkLayer(this.getPos());
        if (checkedPositions.size() == 9) {
            checkedPositions.forEach(this::handleCheckedLocation);
        } else {
            this.invalidate();
            this.getWorld().setBlockState(this.getPos(), this.getBlockState()
                    .with(ObeliskCoreBlock.CORE_STATE, ObeliskCoreState.INACTIVE));
            this.remove();
        }
    }

    private void handleCheckedLocation(Pair<BlockPos, Boolean> checkedPosition) {
        if (checkedPosition.getRight()) {
            this.getWorld().setBlockState(checkedPosition.getLeft(), this.getBlockState()
                    .with(ObeliskCoreBlock.CORE_STATE, ObeliskCoreState.CONNECTED));

            TileEntity tileEntity = this.getWorld().getTileEntity(checkedPosition.getLeft());
            if (tileEntity instanceof ObeliskConnectedTileEntity) {
                ((ObeliskConnectedTileEntity) tileEntity).setControllerPosition(this.getPos());
            }
        }
    }

    public List<Pair<BlockPos, Boolean>> checkLayer(BlockPos center) {
        return BlockPos.getAllInBox(center.add(1, 0, 1), center.add(-1, 0, -1))
                .map(this::getPosStatus)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Pair<BlockPos, Boolean> getPosStatus(BlockPos blockPos) {
        BlockState blockState = this.getWorld().getBlockState(blockPos);
        if (blockState.getBlock() == this.getBlockState().getBlock()) {
            boolean needsUpdate = blockState.get(ObeliskCoreBlock.CORE_STATE) == ObeliskCoreState.INACTIVE;
            return Pair.of(needsUpdate ? blockPos.toImmutable() : null, needsUpdate);
        }
        return null;
    }
}
