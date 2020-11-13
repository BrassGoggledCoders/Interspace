package xyz.brassgoggledcoders.interspace.tileentity;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.interspace.api.functional.ObeliskFunction;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatial;
import xyz.brassgoggledcoders.interspace.api.spatial.query.Transaction;

import java.util.function.Function;
import java.util.function.IntSupplier;

public class ObeliskCapabilityHandler {
    private final Capability<?> capability;
    private final Function<Capability<?>, LazyOptional<?>> newCapability;
    private final IntSupplier coolDownSupplier;

    private LazyOptional<ObeliskFunction> lazyOptional;
    private Transaction<?> transaction;
    private int coolDown;

    public ObeliskCapabilityHandler(Capability<?> capability, Function<Capability<?>, LazyOptional<?>> newCapability,
                                    IntSupplier coolDownSupplier) {
        this.capability = capability;
        this.newCapability = newCapability;
        //TODO Fix I guess, probably nuke
        this.lazyOptional = LazyOptional.empty();
        if (this.lazyOptional.isPresent()) {
            this.lazyOptional.addListener(this::handleInvalidation);
        }
        this.coolDownSupplier = coolDownSupplier;
        this.coolDown = coolDownSupplier.getAsInt();
    }

    public void tick(ISpatial spatial) {
        if (--this.coolDown < 0 && (transaction == null || transaction.isDone())) {
            this.coolDown = coolDownSupplier.getAsInt();
            this.transaction = this.lazyOptional
                    .map(value -> value.apply(spatial))
                    .orElseGet(Transaction::ofEmptyCast);
        }
    }

    public boolean isPresent() {
        return lazyOptional.isPresent();
    }

    public String getName() {
        return capability.getName();
    }

    private void handleInvalidation(LazyOptional<?> lazyOptional) {
        this.lazyOptional = LazyOptional.empty();
    }
}
