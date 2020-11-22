package xyz.brassgoggledcoders.interspace.interspace;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceVolumeManager;
import xyz.brassgoggledcoders.interspace.api.interspace.InterspaceVolume;
import xyz.brassgoggledcoders.interspace.util.RandomSelector;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

public class InterspaceVolumeManager extends JsonReloadListener implements IInterspaceVolumeManager {
    public static final ResourceLocation DEFAULT = InterspaceMod.rl("default");

    private final Random random = new Random();
    private final Map<ResourceLocation, RandomSelector<InterspaceVolume>> volumeProviders;
    private RandomSelector<InterspaceVolume> defaultVolumeProvider = null;

    public InterspaceVolumeManager() {
        super(new Gson(), "interspace/volume");
        this.volumeProviders = Maps.newHashMap();
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void apply(Map<ResourceLocation, JsonElement> jsonElements, IResourceManager resourceManager, IProfiler profiler) {
        this.volumeProviders.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : jsonElements.entrySet()) {
            JsonElement fileElement = entry.getValue();
            if (fileElement.isJsonArray()) {
                JsonArray volumesJson = fileElement.getAsJsonArray();
                List<InterspaceVolume> volumes = Lists.newArrayList();
                for (JsonElement volumeElement : volumesJson) {
                    if (volumeElement.isJsonObject()) {
                        JsonObject volumeObject = volumeElement.getAsJsonObject();
                        volumes.add(new InterspaceVolume(
                                JSONUtils.getInt(volumeObject, "volume", 64),
                                JSONUtils.getFloat(volumeObject, "weight", 1.0F)
                        ));
                    } else if (volumeElement.isJsonPrimitive()) {
                        JsonPrimitive volumePrimitive = volumeElement.getAsJsonPrimitive();
                        if (volumePrimitive.isNumber()) {
                            volumes.add(new InterspaceVolume(volumeElement.getAsInt(), 1.0D));
                        } else {
                            InterspaceMod.LOGGER.error("Parsing error loading interspace volumes {}: {}",
                                    entry, "Single Values in array must be integer");
                        }
                    } else {
                        InterspaceMod.LOGGER.error("Parsing error loading interspace volumes {}: {}",
                                entry.getKey(), "Values in Array must be object or integer");
                    }
                }
                if (!volumes.isEmpty()) {
                    if (entry.getKey().equals(DEFAULT)) {
                        defaultVolumeProvider = RandomSelector.weighted(volumes, InterspaceVolume::getWeight);
                    } else {
                        volumeProviders.put(entry.getKey(), RandomSelector.weighted(volumes, InterspaceVolume::getWeight));
                    }
                } else {
                    InterspaceMod.LOGGER.error("Parsing error loading interspace volumes {}: {}",
                            entry.getKey(), "Found zero valid values");
                }
            } else {
                InterspaceMod.LOGGER.error("Parsing error loading interspace volumes {}: File must be a Json Array", entry.getKey());
            }
        }
    }

    @Override
    public int getVolume(RegistryKey<World> world) {
        return volumeProviders.getOrDefault(world.getLocation(), defaultVolumeProvider).next(random).getVolume();
    }

    @Override
    public Stream<InterspaceVolume> getVolumes(RegistryKey<World> world) {
        return volumeProviders.getOrDefault(world.getLocation(), defaultVolumeProvider).stream(random);
    }

    @Override
    public Map<ResourceLocation, Stream<InterspaceVolume>> getAllVolumes() {
        Map<ResourceLocation, Stream<InterspaceVolume>> allVolumes = Maps.newHashMap();
        allVolumes.put(DEFAULT, defaultVolumeProvider.stream(random));
        volumeProviders.forEach((key, value) -> allVolumes.put(key, value.stream(random)));
        return allVolumes;
    }
}
