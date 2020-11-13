package xyz.brassgoggledcoders.interspace.json;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.apache.commons.io.IOUtils;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.spatial.entry.ISpatialEntryManager;
import xyz.brassgoggledcoders.interspace.api.spatial.entry.SpatialEntry;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SpatialEntryManager implements IFutureReloadListener, ISpatialEntryManager {
    private static final int JSON_EXTENSION_LENGTH = ".json".length();
    private static final Gson GSON = new Gson();
    private static final ResourceLocation DEFAULT_LOCATION = Interspace.rl("default");

    private final Map<ResourceLocation, SpatialWorldEntry> spatialWorldEntries;

    public SpatialEntryManager() {
        spatialWorldEntries = Maps.newHashMap();
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public CompletableFuture<Void> reload(IStage stage, IResourceManager resourceManager, IProfiler preparationsProfiler,
                                          IProfiler reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        return CompletableFuture.supplyAsync(() -> this.handleJson(resourceManager), backgroundExecutor)
                .thenCompose(stage::markCompleteAwaitingOthers)
                .thenAcceptAsync(this::handleBuilders, gameExecutor);
    }

    private Map<ResourceLocation, SpatialWorldEntryBuilder> handleJson(IResourceManager resourceManager) {
        Map<ResourceLocation, SpatialWorldEntryBuilder> entries = Maps.newHashMap();

        String prefix = "spatial";
        for (ResourceLocation prefixedResourceLocation : resourceManager.getAllResourceLocations(prefix, string ->
                string.endsWith(".json"))) {
            String path = prefixedResourceLocation.getPath();
            ResourceLocation resourceLocation = new ResourceLocation(prefixedResourceLocation.getNamespace(),
                    path.substring(prefix.length() + 1, path.length() - JSON_EXTENSION_LENGTH));

            try {
                for (IResource resource : resourceManager.getAllResources(prefixedResourceLocation)) {
                    try (
                            InputStream inputstream = resource.getInputStream();
                            Reader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8))
                    ) {
                        JsonObject jsonObject = JSONUtils.fromJson(GSON, reader, JsonObject.class);
                        if (jsonObject == null) {
                            Interspace.LOGGER.error("Couldn't load {} spatial entry {} from {} in data pack {} as it's empty or null",
                                    prefix, resourceLocation, prefixedResourceLocation, resource.getPackName());
                        } else if (CraftingHelper.processConditions(jsonObject, "conditions")) {
                            entries.computeIfAbsent(resourceLocation, rl -> new SpatialWorldEntryBuilder()).fromJson(jsonObject);
                        }
                    } catch (RuntimeException | IOException exception) {
                        Interspace.LOGGER.error("Couldn't read {} spatial entry {} from {} in data pack {}",
                                prefix, resourceLocation, prefixedResourceLocation, resource.getPackName(), exception);
                    } finally {
                        IOUtils.closeQuietly(resource);
                    }
                }
            } catch (IOException exception) {
                Interspace.LOGGER.error("Couldn't read {} spatial entry {} from {}", prefix, resourceLocation,
                        prefixedResourceLocation, exception);
            }
        }
        return entries;
    }

    private void handleBuilders(Map<ResourceLocation, SpatialWorldEntryBuilder> builders) {
        this.spatialWorldEntries.clear();
        builders.forEach((name, value) -> spatialWorldEntries.put(name, value.build(name)));
    }

    @Override
    public Collection<SpatialEntry> getSpatialEntriesFor(World world) {
        if (spatialWorldEntries.containsKey(world.getDimensionKey().getLocation())) {
            return this.spatialWorldEntries.get(world.getDimensionKey().getLocation()).getSpacialEntries();
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<SpatialEntry> getDefaultEntries() {
        return spatialWorldEntries.get(DEFAULT_LOCATION).getSpacialEntries();
    }

    @Override
    public SpatialEntry getRandomSpatialEntryFor(World world, Random random) {
        SpatialWorldEntry worldEntry = this.spatialWorldEntries.get(world.getDimensionKey().getLocation());
        if (worldEntry == null) {
            worldEntry = this.spatialWorldEntries.get(DEFAULT_LOCATION);
        }
        return worldEntry.getSpacialEntry(random);
    }
}
