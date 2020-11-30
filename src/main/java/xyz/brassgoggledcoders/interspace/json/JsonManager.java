package xyz.brassgoggledcoders.interspace.json;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.util.RandomSelector;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class JsonManager<T> extends JsonReloadListener {
    public static final ResourceLocation DEFAULT = InterspaceMod.rl("default");

    private final String name;
    private final Map<ResourceLocation, RandomSelector<T>> results;
    private RandomSelector<T> defaultResult = null;

    public JsonManager(String name) {
        super(new Gson(), "interspace/" + name);
        this.name = name;
        this.results = Maps.newHashMap();
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void apply(Map<ResourceLocation, JsonElement> jsonElements, IResourceManager resourceManager, IProfiler profiler) {
        this.results.clear();
        this.defaultResult = null;
        for (Map.Entry<ResourceLocation, JsonElement> entry : jsonElements.entrySet()) {
            JsonElement fileElement = entry.getValue();
            if (fileElement.isJsonArray()) {
                JsonArray valuesJson = fileElement.getAsJsonArray();
                List<T> values = Lists.newArrayList();
                try {
                    for (JsonElement valueElement : valuesJson) {
                        values.add(this.parse(valueElement));
                    }
                    if (!values.isEmpty()) {
                        if (entry.getKey().equals(DEFAULT)) {
                            defaultResult = RandomSelector.weighted(values, this::getWeight);
                        } else {
                            results.put(entry.getKey(), RandomSelector.weighted(values, this::getWeight));
                        }
                    } else {
                        throw new JsonParseException("Found zero valid values");
                    }
                } catch (JsonParseException jsonParseException) {
                    InterspaceMod.LOGGER.error("Parsing error loading Interspace {} {}: {}", name,
                            entry.getKey(), jsonParseException.getMessage());
                }
            } else {
                InterspaceMod.LOGGER.error("Parsing error loading interspace {} {}: {}", name, entry.getKey(),
                        "Top level value must be an array");
            }
        }
        if (defaultResult == null) {
            defaultResult = RandomSelector.uniform(Collections.singleton(this.createDefault()));
        }
    }

    protected RandomSelector<T> getValue(ResourceLocation location) {
        return results.getOrDefault(location, defaultResult);
    }

    protected abstract T parse(JsonElement jsonElement);

    protected abstract T createDefault();

    protected abstract double getWeight(T value);
}
