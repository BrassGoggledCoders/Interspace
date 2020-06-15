package xyz.brassgoggledcoders.interspace.api.spatial.item;

import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;

public abstract class SpatialItemType<T> extends ForgeRegistryEntry<SpatialItemType<?>> {
    @Nonnull
    public abstract T fromSpacialItem(@Nonnull SpatialItem spatialItem);

    public abstract SpatialItem toSpacialItem(@Nonnull T object);

    public abstract boolean matchesType(Object object);
}
