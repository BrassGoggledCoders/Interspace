package xyz.brassgoggledcoders.interspace.registration;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class LazyForgeRegistry<V extends IForgeRegistryEntry<V>> implements IForgeRegistry<V> {
    private final NonNullLazy<IForgeRegistry<V>> registry;

    protected LazyForgeRegistry(final ResourceLocation resourceLocation) {
        registry = NonNullLazy.of(() ->
                Objects.requireNonNull(
                        RegistryManager.ACTIVE.getRegistry(resourceLocation),
                        () -> String.format("Registry of name %s not present", resourceLocation)
                )
        );
    }

    public static <V extends IForgeRegistryEntry<V>> LazyForgeRegistry<V> of(final ResourceLocation resourceLocation) {
        return new LazyForgeRegistry<>(resourceLocation);
    }

    private IForgeRegistry<V> getRegistry() {
        return registry.get();
    }

    @Override
    public ResourceLocation getRegistryName() {
        return getRegistry().getRegistryName();
    }

    @Override
    public Class<V> getRegistrySuperType() {
        return getRegistry().getRegistrySuperType();
    }

    @Override
    public void register(final V value) {
        getRegistry().register(value);
    }

    @SafeVarargs
    @Override
    public final void registerAll(final V... values) {
        getRegistry().registerAll(values);
    }

    @Override
    public boolean containsKey(final ResourceLocation key) {
        return getRegistry().containsKey(key);
    }

    @Override
    public boolean containsValue(final V value) {
        return getRegistry().containsValue(value);
    }

    @Override
    public boolean isEmpty() {
        return getRegistry().isEmpty();
    }

    @Override
    @Nullable
    public V getValue(final ResourceLocation key) {
        return getRegistry().getValue(key);
    }

    @Override
    @Nullable
    public ResourceLocation getKey(final V value) {
        return getRegistry().getKey(value);
    }

    @Override
    @Nullable
    public ResourceLocation getDefaultKey() {
        return getRegistry().getDefaultKey();
    }

    @Override
    @Nonnull
    public Set<ResourceLocation> getKeys() {
        return getRegistry().getKeys();
    }

    @Override
    @Nonnull
    public Collection<V> getValues() {
        return getRegistry().getValues();
    }

    @Override
    @Nonnull
    public Set<Map.Entry<ResourceLocation, V>> getEntries() {
        return getRegistry().getEntries();
    }

    @Override
    public <T> T getSlaveMap(final ResourceLocation slaveMapName, final Class<T> type) {
        return getRegistry().getSlaveMap(slaveMapName, type);
    }

    @Override
    public Iterator<V> iterator() {
        return getRegistry().iterator();
    }
}
