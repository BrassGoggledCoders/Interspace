package xyz.brassgoggledcoders.interspace.util;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;

public class JsonHelper {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static <T extends ForgeRegistryEntry<T>> T getRegistryEntry(JsonObject jsonObject, String fieldName, IForgeRegistry<T> registry) {
        String registryName = JSONUtils.getString(jsonObject, fieldName);
        T value = registry.getValue(new ResourceLocation(registryName));
        if (value == null) {
            throw new JsonParseException("Failed to find Registry Value: " + registryName + " in Registry: " + registry.getRegistryName());
        }
        return value;
    }

    /**
     * Gets the float value of the given JsonElement.  Expects the second parameter to be the name of the element's field
     * if an error message needs to be thrown.
     */
    public static double getDouble(JsonElement json, String memberName) {
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isNumber()) {
            return json.getAsDouble();
        } else {
            throw new JsonSyntaxException("Expected " + memberName + " to be a Float, was " + JSONUtils.toString(json));
        }
    }

    public static double getDouble(JsonObject json, String memberName) {
        if (json.has(memberName)) {
            return getDouble(json.get(memberName), memberName);
        } else {
            throw new JsonSyntaxException("Missing " + memberName + ", expected to find a Float");
        }
    }

    public static CompoundNBT getCompoundNBT(JsonObject jsonObject, String memberName, CompoundNBT defaultValue) {
        if (jsonObject.has(memberName)) {
            JsonElement jsonElement = jsonObject.get(memberName);
            try {
                if (jsonElement.isJsonObject()) {
                    return JsonToNBT.getTagFromJson(GSON.toJson(jsonElement));
                } else {
                    return JsonToNBT.getTagFromJson(JSONUtils.getString(jsonElement, "nbt"));
                }
            } catch (CommandSyntaxException e) {
                throw new JsonSyntaxException("Invalid NBT Entry: " + jsonElement.toString());
            }
        } else {
            return defaultValue;
        }
    }
}
