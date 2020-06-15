package xyz.brassgoggledcoders.interspace.datagen.spatial;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialType;
import xyz.brassgoggledcoders.interspace.nbt.CompoundNBTBuilder;
import xyz.brassgoggledcoders.interspace.util.NBTHelper;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class SpatialEntryJsonBuilder {
    private final List<ICondition> conditions;
    private SpatialType spatialType;
    private double weight = 1.0;
    private CompoundNBT nbt;

    private SpatialEntryJsonBuilder() {
        conditions = Lists.newArrayList();
    }

    public SpatialEntryJsonBuilder withType(SpatialType type) {
        this.spatialType = type;
        return this;
    }

    public SpatialEntryJsonBuilder withType(Supplier<SpatialType> type) {
        return this.withType(type.get());
    }

    public SpatialEntryJsonBuilder withWeight(double weight) {
        this.weight = weight;
        return this;
    }

    public SpatialEntryJsonBuilder withNBT(CompoundNBT nbt) {
        this.nbt = nbt;
        return this;
    }

    public SpatialEntryJsonBuilder withNBT(CompoundNBTBuilder nbt) {
        return this.withNBT(nbt.build());
    }

    public SpatialEntryJsonBuilder withCondition(ICondition condition) {
        this.conditions.add(condition);
        return this;
    }

    public JsonObject build() {
        JsonObject jsonObject = new JsonObject();
        if (spatialType == null) {
            throw new IllegalStateException("Failed to Build Spatial Entry, Missing 'type'");
        } else {
            jsonObject.addProperty("type", Objects.requireNonNull(spatialType.getRegistryName()).toString());
        }
        if (!conditions.isEmpty()) {
            JsonArray jsonConditions = new JsonArray();
            for (ICondition condition : conditions) {
                jsonConditions.add(CraftingHelper.serialize(condition));
            }
            jsonObject.add("conditions", jsonConditions);
        }
        if (weight > 0) {
            jsonObject.addProperty("weight", weight);
        } else {
            throw new IllegalStateException("weight most be greater than 0");
        }
        if (nbt != null) {
            jsonObject.add("nbt", NBTHelper.toPrettyJson(nbt));
        }
        return jsonObject;
    }

    public static SpatialEntryJsonBuilder create() {
        return new SpatialEntryJsonBuilder();
    }
}
