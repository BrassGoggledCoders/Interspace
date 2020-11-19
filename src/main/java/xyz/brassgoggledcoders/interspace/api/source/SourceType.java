package xyz.brassgoggledcoders.interspace.api.source;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.interspace.api.InterspaceRegistries;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class SourceType extends ForgeRegistryEntry<SourceType> {
    private final Function<SourceType, Source<?>> sourceCreator;

    public SourceType(Function<SourceType, Source<?>> sourceCreator) {
        this.sourceCreator = sourceCreator;
    }

    public Source<?> create() {
        return sourceCreator.apply(this);
    }

    public static SourceType of(Function<SourceType, Source<?>> sourceCreator) {
        return new SourceType(sourceCreator);
    }

    @Nullable
    public static Source<?> deserializeSource(CompoundNBT nbt) {
        SourceType type = InterspaceRegistries.SOURCE_TYPE.getValue(new ResourceLocation(nbt.getString("type")));
        if (type != null) {
            Source<?> source = type.create();
            source.deserializeNBT(nbt);
            return source;
        }
        return null;
    }

    public static CompoundNBT serializeSource(Source<?> source) {
        CompoundNBT nbt = source.serializeNBT();
        nbt.putString("type", Objects.requireNonNull(source.getType().getRegistryName()).toString());
        return nbt;
    }
}
