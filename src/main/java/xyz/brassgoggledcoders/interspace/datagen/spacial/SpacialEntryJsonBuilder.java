package xyz.brassgoggledcoders.interspace.datagen.spacial;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import xyz.brassgoggledcoders.interspace.api.spacial.entry.SpacialEntry;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialType;
import xyz.brassgoggledcoders.interspace.nbt.CompoundNBTBuilder;
import xyz.brassgoggledcoders.interspace.util.NBTHelper;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class SpacialEntryJsonBuilder {
    private final List<ICondition> conditions;
    private SpacialType spacialType;
    private double weight = 1.0;
    private CompoundNBT nbt;

    private SpacialEntryJsonBuilder() {
        conditions = Lists.newArrayList();
    }

    public SpacialEntryJsonBuilder withType(SpacialType type) {
        this.spacialType = type;
        return this;
    }

    public SpacialEntryJsonBuilder withType(Supplier<SpacialType> type) {
        return this.withType(type.get());
    }

    public SpacialEntryJsonBuilder withWeight(double weight) {
        this.weight = weight;
        return this;
    }

    public SpacialEntryJsonBuilder withNBT(CompoundNBT nbt) {
        this.nbt = nbt;
        return this;
    }

    public SpacialEntryJsonBuilder withNBT(CompoundNBTBuilder nbt) {
        return this.withNBT(nbt.build());
    }

    public SpacialEntryJsonBuilder withCondition(ICondition condition) {
        this.conditions.add(condition);
        return this;
    }

    public JsonObject build() {
        JsonObject jsonObject = new JsonObject();
        if (spacialType == null) {
            throw new IllegalStateException("Failed to Build Spacial Entry, Missing 'type'");
        } else {
            jsonObject.addProperty("type", Objects.requireNonNull(spacialType.getRegistryName()).toString());
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

    public static SpacialEntryJsonBuilder create() {
        return new SpacialEntryJsonBuilder();
    }
}
