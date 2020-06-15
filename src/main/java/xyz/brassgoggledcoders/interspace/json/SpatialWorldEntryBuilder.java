package xyz.brassgoggledcoders.interspace.json;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import xyz.brassgoggledcoders.interspace.InterspaceRegistries;
import xyz.brassgoggledcoders.interspace.api.spatial.entry.SpatialEntry;
import xyz.brassgoggledcoders.interspace.util.JsonHelper;

import java.util.List;

public class SpatialWorldEntryBuilder {
    private final List<SpatialEntry> spacialEntries;

    public SpatialWorldEntryBuilder() {
        this.spacialEntries = Lists.newArrayList();
    }

    public void addEntry(SpatialEntry spatialEntry) {
        this.spacialEntries.add(spatialEntry);
    }

    public void fromJson(JsonObject jsonObject) {
        if (JSONUtils.getBoolean(jsonObject, "replace", false)) {
            this.spacialEntries.clear();
        }

        JsonArray entryArray = JSONUtils.getJsonArray(jsonObject, "entries");
        if (entryArray.size() > 0) {
            for (JsonElement jsonElement : entryArray) {
                if (jsonElement.isJsonObject()) {
                    JsonObject entry = jsonElement.getAsJsonObject();
                    if (CraftingHelper.processConditions(entry, "conditions")) {
                        this.addEntry(createSpacialEntry(entry));
                    }
                } else {
                    throw new JsonParseException("All entries must be JsonObjects");
                }
            }
        } else {
            throw new JsonParseException("entries must be a non zero length JsonArray");
        }
    }

    public SpatialWorldEntry build(ResourceLocation name) {
        return new SpatialWorldEntry(name, spacialEntries);
    }

    public SpatialEntry createSpacialEntry(JsonObject entry) {
        return new SpatialEntry(
                JsonHelper.getRegistryEntry(entry, "type", InterspaceRegistries.SPACIAL_TYPES),
                JsonHelper.getDouble(entry, "weight"),
                JsonHelper.getCompoundNBT(entry, "nbt", null)
        );
    }
}
