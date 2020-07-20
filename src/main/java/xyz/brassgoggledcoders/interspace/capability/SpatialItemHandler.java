package xyz.brassgoggledcoders.interspace.capability;

import net.minecraftforge.items.ItemStackHandler;
import xyz.brassgoggledcoders.interspace.api.functional.ObeliskFunction;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatial;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.query.Transaction;
import xyz.brassgoggledcoders.interspace.content.InterspaceSpatialItemTypes;

import javax.annotation.Nonnull;
import java.util.Collection;

public class SpatialItemHandler extends ItemStackHandler implements ObeliskFunction {
    private boolean changed = false;

    public SpatialItemHandler() {
        this(10);
    }

    public SpatialItemHandler(int size) {
        super(size);
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        this.changed = true;
    }

    @Override
    @Nonnull
    public Transaction<?> apply(@Nonnull ISpatial spatial) {
        if (changed) {
            Transaction<Collection<SpatialItem>> transaction = spatial.offer(this.supplyItems());
            this.changed = false;
            return transaction;
        } else {
            return Transaction.ofEmpty();
        }
    }

    private Collection<SpatialItem> supplyItems() {
        return InterspaceSpatialItemTypes.ITEM_STACK.get().convert(this.stacks);
    }
}
