package xyz.brassgoggledcoders.interspace.api.spacial.entry;

import net.minecraft.world.IWorld;

import java.util.Collection;
import java.util.Random;

public interface ISpacialEntryManager {
    Collection<SpacialEntry> getSpacialEntriesFor(IWorld world);

    Collection<SpacialEntry> getDefaultEntries();

    SpacialEntry getRandomSpacialEntryFor(IWorld world, Random random);
}
