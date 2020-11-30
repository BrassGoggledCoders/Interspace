package xyz.brassgoggledcoders.interspace.api.interspace;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.interspace.InterspaceMod;

public class InterspaceCache {
    private final ResourceLocation name;
    private final boolean deep;
    private final double weight;
    private final Float luck;

    public InterspaceCache(ResourceLocation name, boolean deep, double weight, Float luck) {
        this.name = name;
        this.deep = deep;
        this.weight = weight;
        this.luck = luck;
    }

    public ResourceLocation getName() {
        return name;
    }

    public Float getLuck() {
        return luck;
    }

    public boolean isDeep() {
        return deep;
    }

    public double getWeight() {
        return weight;
    }

    public CompoundNBT toNBT() {

    }

    public static InterspaceCache fromNBT(CompoundNBT nbt) {

    }

    public static InterspaceCache fromJson(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            JsonObject volumeObject = jsonElement.getAsJsonObject();
            return new InterspaceCache(
                    new ResourceLocation(JSONUtils.getString(volumeObject, "location")),
                    JSONUtils.getBoolean(volumeObject, "deep", false),
                    JSONUtils.getFloat(volumeObject, "weight", InterspaceMod.getServerConfig()
                            .getDefaultCacheChance()
                            .floatValue()
                    ),
                    volumeObject.has("luck") ? JSONUtils.getFloat(volumeObject,
                            "luck") : null
            );
        } else if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive volumePrimitive = jsonElement.getAsJsonPrimitive();
            if (volumePrimitive.isString()) {
                return new InterspaceCache(new ResourceLocation(volumePrimitive.getAsString()), false, 1.0D,
                        null);
            } else {
                throw new JsonParseException("Single Values in array must be Strings");
            }
        } else {
            throw new JsonParseException("Values in Array must be object or integer");
        }
    }
}
