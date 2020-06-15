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
import xyz.brassgoggledcoders.interspace.api.spacial.entry.SpacialEntry;
import xyz.brassgoggledcoders.interspace.util.JsonHelper;

import java.util.List;

public class SpacialWorldEntryBuilder {
    private final List<SpacialEntry> spacialEntries;

    public SpacialWorldEntryBuilder() {
        this.spacialEntries = Lists.newArrayList();
    }

    public void addEntry(SpacialEntry spacialEntry) {
        this.spacialEntries.add(spacialEntry);
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

    public SpacialWorldEntry build(ResourceLocation name) {
        return new SpacialWorldEntry(name, spacialEntries);
    }

    public SpacialEntry createSpacialEntry(JsonObject entry) {
        return new SpacialEntry(
                JsonHelper.getRegistryEntry(entry, "type", InterspaceRegistries.SPACIAL_TYPES),
                JsonHelper.getDouble(entry, "weight"),
                JsonHelper.getCompoundNBT(entry, "nbt", null)
        );
    }
}
