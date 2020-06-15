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
import net.minecraft.world.IWorld;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.apache.commons.io.IOUtils;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.spacial.entry.ISpacialEntryManager;
import xyz.brassgoggledcoders.interspace.api.spacial.entry.SpacialEntry;

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

public class SpacialEntryManager implements IFutureReloadListener, ISpacialEntryManager {
    private static final int JSON_EXTENSION_LENGTH = ".json".length();
    private static final Gson GSON = new Gson();
    private static final ResourceLocation DEFAULT_LOCATION = Interspace.rl("default");

    private final Map<ResourceLocation, SpacialWorldEntry> spacialWorldEntries;

    public SpacialEntryManager() {
        spacialWorldEntries = Maps.newHashMap();
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

    private Map<ResourceLocation, SpacialWorldEntryBuilder> handleJson(IResourceManager resourceManager) {
        Map<ResourceLocation, SpacialWorldEntryBuilder> entries = Maps.newHashMap();

        String prefix = "spacial_entry";
        for (ResourceLocation prefixedResourceLocation : resourceManager.getAllResourceLocations(prefix, string ->
                string.endsWith(".json"))) {
            String path = prefixedResourceLocation.getPath();
            ResourceLocation resourceLocation = new ResourceLocation(prefixedResourceLocation.getNamespace(),
                    path.substring(prefix.length() + 1, path.length() - JSON_EXTENSION_LENGTH));

            try {
                for (IResource resource : resourceManager.getAllResources(prefixedResourceLocation)) {
                    try (
                            InputStream inputstream = resource.getInputStream();
                            Reader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8));
                    ) {
                        JsonObject jsonObject = JSONUtils.fromJson(GSON, reader, JsonObject.class);
                        if (jsonObject == null) {
                            Interspace.LOGGER.error("Couldn't load {} tag list {} from {} in data pack {} as it's empty or null",
                                    prefix, resourceLocation, prefixedResourceLocation, resource.getPackName());
                        } else if (CraftingHelper.processConditions(jsonObject, "conditions")) {
                            entries.computeIfAbsent(resourceLocation, rl -> new SpacialWorldEntryBuilder()).fromJson(jsonObject);
                        }
                    } catch (RuntimeException | IOException exception) {
                        Interspace.LOGGER.error("Couldn't read {} tag list {} from {} in data pack {}",
                                prefix, resourceLocation, prefixedResourceLocation, resource.getPackName(), exception);
                    } finally {
                        IOUtils.closeQuietly(resource);
                    }
                }
            } catch (IOException exception) {
                Interspace.LOGGER.error("Couldn't read {} tag list {} from {}", prefix, resourceLocation,
                        prefixedResourceLocation, exception);
            }
        }
        return entries;
    }

    private void handleBuilders(Map<ResourceLocation, SpacialWorldEntryBuilder> builders) {
        this.spacialWorldEntries.clear();
        builders.forEach((name, value) -> spacialWorldEntries.put(name, value.build(name)));
    }

    @Override
    public Collection<SpacialEntry> getSpacialEntriesFor(IWorld world) {
        if (spacialWorldEntries.containsKey(world.getDimension().getType().getRegistryName())) {
            return this.spacialWorldEntries.get(world.getDimension().getType().getRegistryName()).getSpacialEntries();
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<SpacialEntry> getDefaultEntries() {
        return spacialWorldEntries.get(DEFAULT_LOCATION).getSpacialEntries();
    }

    @Override
    public SpacialEntry getRandomSpacialEntryFor(IWorld world, Random random) {
        SpacialWorldEntry worldEntry = this.spacialWorldEntries.get(world.getDimension().getType().getRegistryName());
        if (worldEntry == null) {
            worldEntry = this.spacialWorldEntries.get(DEFAULT_LOCATION);
        }
        return worldEntry.getSpacialEntry(random);
    }
}
