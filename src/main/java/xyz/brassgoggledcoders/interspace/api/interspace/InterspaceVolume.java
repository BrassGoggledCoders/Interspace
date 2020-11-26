package xyz.brassgoggledcoders.interspace.api.interspace;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import xyz.brassgoggledcoders.interspace.InterspaceMod;

public class InterspaceVolume {
    private final int volume;
    private final double weight;
    private final float cacheChance;
    private final Float cacheLuck;

    public InterspaceVolume(int volume, double weight, float cacheChance, Float luck) {
        this.volume = volume;
        this.weight = weight;
        this.cacheChance = cacheChance;
        this.cacheLuck = luck;
    }

    public int getVolume() {
        return volume;
    }

    public double getWeight() {
        return weight;
    }

    public float getCacheChance() {
        return cacheChance;
    }

    public Float getCacheLuck() {
        return cacheLuck;
    }

    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("volume", this.getVolume());
        nbt.putDouble("weight", this.getWeight());
        nbt.putFloat("cacheChance", this.getCacheChance());
        if (this.getCacheLuck() != null) {
            nbt.putFloat("cacheLuck", this.getCacheLuck());
        }
        return nbt;
    }

    public static InterspaceVolume fromNBT(CompoundNBT nbt) {
        return new InterspaceVolume(
                nbt.getInt("volume"),
                nbt.getDouble("weight"),
                nbt.getFloat("cacheChance"),
                nbt.contains("cacheLuck") ? nbt.getFloat("cacheLuck") : null
        );
    }

    public static InterspaceVolume fromJson(JsonElement jsonElement) throws JsonParseException {
        if (jsonElement.isJsonObject()) {
            JsonObject volumeObject = jsonElement.getAsJsonObject();
            return new InterspaceVolume(
                    JSONUtils.getInt(volumeObject, "volume", 64),
                    JSONUtils.getFloat(volumeObject, "weight", 1.0F),
                    JSONUtils.getFloat(volumeObject, "cacheChance", InterspaceMod.getServerConfig()
                            .getDefaultCacheChance()
                            .floatValue()),
                    volumeObject.has("cacheLuck") ? JSONUtils.getFloat(volumeObject,
                            "cacheLuck") : null
            );
        } else if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive volumePrimitive = jsonElement.getAsJsonPrimitive();
            if (volumePrimitive.isNumber()) {
                return new InterspaceVolume(
                        volumePrimitive.getAsInt(),
                        1.0D,
                        InterspaceMod.getServerConfig()
                                .getDefaultCacheChance()
                                .floatValue(),
                        null
                );
            } else {
                throw new JsonParseException("Single Values in array must be integer");
            }
        } else {
            throw new JsonParseException("Values in Array must be object or integer");
        }
    }
}
