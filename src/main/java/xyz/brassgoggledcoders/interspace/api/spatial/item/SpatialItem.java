package xyz.brassgoggledcoders.interspace.api.spatial.item;

import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;
import java.util.Objects;

public class SpatialItem {
    private final SpatialItemType<?> type;
    private final String registryName;
    private final int amount;
    private final CompoundNBT data;

    public SpatialItem(SpatialItemType<?> type, String registryName, int amount, @Nullable CompoundNBT data) {
        this.type = type;
        this.registryName = registryName;
        this.amount = amount;
        this.data = data;
    }

    public String getRegistryName() {
        return registryName;
    }

    public int getCount() {
        return amount;
    }

    @Nullable
    public CompoundNBT getNBT() {
        return data;
    }

    public SpatialItemType<?> getType() {
        return type;
    }

    public String getTypeString() {
        return Objects.requireNonNull(this.getType().getRegistryName()).toString();
    }
}
