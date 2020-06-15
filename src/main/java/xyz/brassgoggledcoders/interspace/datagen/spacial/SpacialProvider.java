package xyz.brassgoggledcoders.interspace.datagen.spacial;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class SpacialProvider implements IDataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    protected final DataGenerator dataGenerator;

    private final Map<ResourceLocation, SpacialWorldEntryJsonBuilder> spacialWorldEntryBuilders;

    protected SpacialProvider(DataGenerator dataGenerator) {
        this.dataGenerator = dataGenerator;
        this.spacialWorldEntryBuilders = Maps.newHashMap();
    }

    @Override
    public void act(@Nonnull DirectoryCache cache) throws IOException {
        this.spacialWorldEntryBuilders.clear();
        this.registerSpacialWorldEntries();
        spacialWorldEntryBuilders.forEach(this.saveToFile(cache));
    }

    private BiConsumer<ResourceLocation, SpacialWorldEntryJsonBuilder> saveToFile(DirectoryCache cache) {
        return (resourceLocation, spacialWorldEntryJsonBuilder) -> {
            Path entryPath = getPath(this.dataGenerator.getOutputFolder(), resourceLocation);
            try {
                IDataProvider.save(GSON, cache, spacialWorldEntryJsonBuilder.build(), entryPath);
            } catch (IOException ioexception) {
                LOGGER.error("Couldn't save loot table {}", entryPath, ioexception);
            }
        };
    }

    protected abstract void registerSpacialWorldEntries();

    protected SpacialWorldEntryJsonBuilder getBuilder(ResourceLocation resourceLocation) {
        return spacialWorldEntryBuilders.computeIfAbsent(resourceLocation, rl -> SpacialWorldEntryJsonBuilder.create());
    }

    protected SpacialWorldEntryJsonBuilder getBuilder(DimensionType dimensionType) {
        return this.getBuilder(dimensionType.getRegistryName());
    }

    protected static Path getPath(Path pathIn, ResourceLocation id) {
        return pathIn.resolve("data/" + id.getNamespace() + "/spacial/" + id.getPath() + ".json");
    }

    @Override
    @Nonnull
    public String getName() {
        return "Spacial World Entries";
    }
}
