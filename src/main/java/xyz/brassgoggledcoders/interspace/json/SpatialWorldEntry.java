package xyz.brassgoggledcoders.interspace.json;

import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.interspace.api.spatial.entry.SpatialEntry;
import xyz.brassgoggledcoders.interspace.util.RandomSelector;

import java.util.Collection;
import java.util.Random;

public class SpatialWorldEntry {
    private final ResourceLocation name;
    private final Collection<SpatialEntry> spacialEntries;
    private final RandomSelector<SpatialEntry> randomSelector;

    public SpatialWorldEntry(ResourceLocation name, Collection<SpatialEntry> spacialEntries) {
        this.name = name;
        this.spacialEntries = spacialEntries;
        this.randomSelector = RandomSelector.weighted(spacialEntries, SpatialEntry::getWeight);
    }

    public ResourceLocation getName() {
        return name;
    }

    public Collection<SpatialEntry> getSpacialEntries() {
        return spacialEntries;
    }

    public RandomSelector<SpatialEntry> getRandomSelector() {
        return randomSelector;
    }

    public SpatialEntry getSpacialEntry(Random random) {
        return this.getRandomSelector().next(random);
    }
}
