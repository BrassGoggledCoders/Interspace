package xyz.brassgoggledcoders.interspace.api.spatial.item;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.Objects;

public class SpatialItem {
    private final SpatialItemType<?> type;
    private final String registryName;
    private final int amount;
    private final Lazy<CompoundNBT> data;

    public SpatialItem(SpatialItemType<?> type, String registryName, int amount, @Nullable CompoundNBT data) {
        this(type, registryName, amount, Lazy.of(() -> data));
    }

    public SpatialItem(SpatialItemType<?> type, String registryName, int amount, Lazy<CompoundNBT> data) {
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
        return data.get();
    }

    public SpatialItemType<?> getType() {
        return type;
    }

    public String getTypeString() {
        return Objects.requireNonNull(this.getType().getRegistryName()).toString();
    }

    @Override
    public String toString() {
        return "SpatialItem{" +
                "type=" + type.getRegistryName() +
                ", registryName='" + registryName + '\'' +
                ", amount=" + amount +
                ", data=" + data.get() +
                '}';
    }
}
