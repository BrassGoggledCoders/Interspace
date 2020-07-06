package xyz.brassgoggledcoders.interspace.tileentity;

import com.google.common.collect.Maps;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullLazy;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.functional.ObeliskFunction;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatial;
import xyz.brassgoggledcoders.interspace.api.spatial.query.Transaction;
import xyz.brassgoggledcoders.interspace.content.InterspaceTileEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;

public class ObeliskConnectedTileEntity extends PassThroughSpatialTileEntity<ISpatial> implements ITickableTileEntity {
    private final static Transaction<?> EMPTY = Transaction.ofEmpty();

    private final NonNullLazy<Map<String, LazyOptional<ObeliskFunction>>> obeliskCapabilities;
    private final Map<String, Transaction<?>> pendingTransactions;
    private BlockPos controllerPosition;

    public ObeliskConnectedTileEntity() {
        super(InterspaceTileEntities.OBELISK_CONNECTED.get());
        this.obeliskCapabilities = NonNullLazy.of(this::setupObeliskCapabilities);
        this.pendingTransactions = Maps.newHashMap();
    }

    public void setControllerPosition(BlockPos blockPos) {
        this.controllerPosition = blockPos;
        this.invalidate();
    }

    @Nullable
    private ObeliskControllerTileEntity getController() {
        if (controllerPosition != null && this.getWorld().isAreaLoaded(controllerPosition, 1)) {
            TileEntity tileEntity = this.getWorld().getTileEntity(controllerPosition);
            if (tileEntity instanceof ObeliskControllerTileEntity) {
                return (ObeliskControllerTileEntity) tileEntity;
            }
        }
        return null;
    }

    @Override
    protected LazyOptional<ISpatial> getCapabilityForPassThrough() {
        ObeliskControllerTileEntity controller = this.getController();
        if (controller != null) {
            return controller.getCapability(InterspaceAPI.SPATIAL);
        } else {
            return LazyOptional.empty();
        }
    }

    @Override
    public void tick() {
        this.getSpatial().ifPresent(this::tickWithSpatial);
    }

    private void tickWithSpatial(@Nonnull ISpatial spatial) {
        for (Map.Entry<String, LazyOptional<ObeliskFunction>> current : obeliskCapabilities.get().entrySet()) {
            Transaction<?> pendingTransaction = pendingTransactions.get(current.getKey());
            if (pendingTransaction == null || pendingTransaction.isDone()) {
                Transaction<?> newTransaction = current.getValue()
                        .map(obeliskFunction -> obeliskFunction.apply(spatial))
                        .orElse(null);
                pendingTransactions.put(current.getKey(), newTransaction);
            }
        }
    }

    private Map<String, LazyOptional<ObeliskFunction>> setupObeliskCapabilities() {
        Map<String, LazyOptional<ObeliskFunction>> obeliskCapabilities = Maps.newHashMap();
        for (Capability<?> capability : InterspaceAPI.getObeliskHandlers()) {
            obeliskCapabilities.put(capability.getName(), this.getCapability(capability)
                    .map(value -> {
                        if (value instanceof ObeliskFunction) {
                            return ((ObeliskFunction) value);
                        } else {
                            return spatial -> null;
                        }
                    })
            );
        }
        return obeliskCapabilities;
    }
}
