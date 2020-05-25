package xyz.brassgoggledcoders.interspace.api.spacial.item;

import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class SpacialItemType<T> extends ForgeRegistryEntry<SpacialItemType<?>> {
    @Nonnull
    public abstract T fromSpacialItem(@Nonnull SpacialItem spacialItem);

    public abstract SpacialItem toSpacialItem(@Nonnull T object);

    public abstract boolean matchesType(Object object);
}
