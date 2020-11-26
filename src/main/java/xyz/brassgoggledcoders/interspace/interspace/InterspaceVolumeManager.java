package xyz.brassgoggledcoders.interspace.interspace;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceVolumeManager;
import xyz.brassgoggledcoders.interspace.api.interspace.InterspaceVolume;
import xyz.brassgoggledcoders.interspace.util.RandomSelector;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class InterspaceVolumeManager extends JsonReloadListener implements IInterspaceVolumeManager {
    public static final ResourceLocation DEFAULT = InterspaceMod.rl("default");

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
        this.defaultVolumeProvider = null;
        for (Map.Entry<ResourceLocation, JsonElement> entry : jsonElements.entrySet()) {
            JsonElement fileElement = entry.getValue();
            if (fileElement.isJsonArray()) {
                JsonArray volumesJson = fileElement.getAsJsonArray();
                List<InterspaceVolume> volumes = Lists.newArrayList();
                try {
                    for (JsonElement volumeElement : volumesJson) {
                        volumes.add(InterspaceVolume.fromJson(volumeElement));
                    }
                    if (!volumes.isEmpty()) {
                        if (entry.getKey().equals(DEFAULT)) {
                            defaultVolumeProvider = RandomSelector.weighted(volumes, InterspaceVolume::getWeight);
                        } else {
                            volumeProviders.put(entry.getKey(), RandomSelector.weighted(volumes, InterspaceVolume::getWeight));
                        }
                    } else {
                        throw new JsonParseException("Found zero valid values");
                    }
                } catch (JsonParseException jsonParseException) {
                    InterspaceMod.LOGGER.error("Parsing error loading Interspace Volumes {}: {}",
                            entry.getKey(), jsonParseException.getMessage());
                }
            } else {
                InterspaceMod.LOGGER.error("Parsing error loading interspace volumes {}: {}", entry.getKey(),
                        "Top level value must be an array");
            }
        }
        if (defaultVolumeProvider == null) {
            defaultVolumeProvider = RandomSelector.uniform(Collections.singleton(
                    new InterspaceVolume(16, 1.0, InterspaceMod.getServerConfig()
                            .getDefaultCacheChance().floatValue(), null)
            ));
        }
    }

    @Override
    public InterspaceVolume getVolume(RegistryKey<World> world, Random random) {
        return volumeProviders.getOrDefault(world.getLocation(), defaultVolumeProvider).next(random);
    }
}
