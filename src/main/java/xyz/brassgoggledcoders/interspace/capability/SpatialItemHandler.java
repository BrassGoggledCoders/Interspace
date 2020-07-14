package xyz.brassgoggledcoders.interspace.capability;

import net.minecraftforge.items.ItemStackHandler;
import xyz.brassgoggledcoders.interspace.api.functional.ObeliskFunction;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatial;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.query.Transaction;
import xyz.brassgoggledcoders.interspace.content.InterspaceSpatialItemTypes;
import xyz.brassgoggledcoders.interspace.spatial.itemtype.ItemStackSpatialItemType;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;

public class SpatialItemHandler extends ItemStackHandler implements ObeliskFunction {
    private boolean changed = false;
    private Transaction<Collection<SpatialItem>> existing = null;
    private int retries = 0;

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
        if (existing != null) {
            if (existing.isDone()) {
                Collection<SpatialItem> remaining = existing.getResult().getNow(Collections.emptyList());
                if (!remaining.isEmpty() && retries < 3) {
                    this.existing = spatial.offer(remaining);
                    retries++;
                    return this.existing;
                } else if (retries >= 3) {
                    retries = 0;
                    //TODO put back in inventory
                }
            }
        }

        if (changed) {
            this.existing = InterspaceSpatialItemTypes.ITEM_STACK
                    .map(this::convert)
                    .map(spatial::offer)
                    .orElseGet(Transaction::ofEmpty);
            this.changed = false;
            return existing;
        } else {
            return Transaction.ofEmpty();
        }
    }

    private Collection<SpatialItem> convert(ItemStackSpatialItemType type) {
        return type.convertInventory(this);
    }
}
