package xyz.brassgoggledcoders.interspace.json;

import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.interspace.api.spacial.entry.SpacialEntry;
import xyz.brassgoggledcoders.interspace.util.RandomSelector;

import java.util.Collection;
import java.util.Random;

public class SpacialWorldEntry {
    private final ResourceLocation name;
    private final Collection<SpacialEntry> spacialEntries;
    private final RandomSelector<SpacialEntry> randomSelector;

    public SpacialWorldEntry(ResourceLocation name, Collection<SpacialEntry> spacialEntries) {
        this.name = name;
        this.spacialEntries = spacialEntries;
        this.randomSelector = RandomSelector.weighted(spacialEntries, SpacialEntry::getWeight);
    }

    public ResourceLocation getName() {
        return name;
    }

    public Collection<SpacialEntry> getSpacialEntries() {
        return spacialEntries;
    }

    public RandomSelector<SpacialEntry> getRandomSelector() {
        return randomSelector;
    }

    public SpacialEntry getSpacialEntry(Random random) {
        return this.getRandomSelector().next(random);
    }
}
