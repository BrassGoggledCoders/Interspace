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
    private final float luck;

    public InterspaceCache(ResourceLocation name, boolean deep, double weight, float luck) {
        this.name = name;
        this.deep = deep;
        this.weight = weight;
        this.luck = luck;
    }

    public ResourceLocation getName() {
        return name;
    }

    public float getLuck() {
        return luck;
    }

    public boolean isDeep() {
        return deep;
    }

    public double getWeight() {
        return weight;
    }

    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("name", this.getName().toString());
        nbt.putBoolean("deep", this.isDeep());
        nbt.putDouble("weight", this.getWeight());
        nbt.putFloat("luck", this.getLuck());
        return nbt;
    }

    public static InterspaceCache fromNBT(CompoundNBT nbt) {
        return new InterspaceCache(
                new ResourceLocation(nbt.getString("name")),
                nbt.getBoolean("deep"),
                nbt.getDouble("weight"),
                nbt.getFloat("luck")
        );
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
                    JSONUtils.getFloat(volumeObject, "luck", 0F)
            );
        } else if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive volumePrimitive = jsonElement.getAsJsonPrimitive();
            if (volumePrimitive.isString()) {
                return new InterspaceCache(new ResourceLocation(volumePrimitive.getAsString()), false, 1.0D,
                        0F);
            } else {
                throw new JsonParseException("Single Values in array must be Strings");
            }
        } else {
            throw new JsonParseException("Values in Array must be object or integer");
        }
    }
}
