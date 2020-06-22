package xyz.brassgoggledcoders.interspace.api.spatial.item;

import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public abstract class SpatialItemType<T> extends ForgeRegistryEntry<SpatialItemType<?>> {
    @Nonnull
    public abstract T fromSpacialItem(@Nonnull SpatialItem spatialItem);

    @Nullable
    public abstract SpatialItem toSpacialItem(@Nonnull T object);

    public abstract int getRetrievalSize();

    public abstract boolean matchesType(Object object);

    public abstract Collection<SpatialItem> convert(Collection<T> collection);
}
