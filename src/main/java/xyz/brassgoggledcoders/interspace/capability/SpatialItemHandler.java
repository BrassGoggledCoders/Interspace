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

public class SpatialItemHandler<T extends ISpatial> extends ItemStackHandler implements ObeliskFunction {
    boolean changed = false;

    public SpatialItemHandler(int size) {
        super(size);
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        this.changed = true;
    }

    @Override
    public Transaction<?> apply(@Nonnull ISpatial spatial) {
        if (changed) {
            Transaction<?> transaction = InterspaceSpatialItemTypes.ITEM_STACK
                    .map(this::convert)
                    .map(spatial::offer)
                    .orElseGet(Transaction::ofEmpty);
            changed = false;
            return transaction;
        } else {
            return Transaction.ofEmpty();
        }
    }

    private Collection<SpatialItem> convert(ItemStackSpatialItemType type) {
        return type.convertInventory(this);
    }
}
