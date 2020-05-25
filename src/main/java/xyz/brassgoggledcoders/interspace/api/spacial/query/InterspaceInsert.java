package xyz.brassgoggledcoders.interspace.api.spacial.query;

import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItemType;

public class InterspaceInsert<T> {
    private final SpacialItemType<T> type;
    private final T item;

    public InterspaceInsert(SpacialItemType<T> type, T item) {
        this.type = type;
        this.item = item;
    }

    public SpacialItemType<T> getType() {
        return type;
    }

    public T getItem() {
        return item;
    }
}
