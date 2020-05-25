package xyz.brassgoggledcoders.interspace.api.spacial.query;

import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItemType;

public class InterspaceRemove<T> {
    private final SpacialItemType<T> type;

    public InterspaceRemove(SpacialItemType<T> type) {
        this.type = type;
    }
}
