package xyz.brassgoggledcoders.interspace.api.spatial.entry;

import net.minecraft.world.IWorld;

import java.util.Collection;
import java.util.Random;

public interface ISpatialEntryManager {
    Collection<SpatialEntry> getSpacialEntriesFor(IWorld world);

    Collection<SpatialEntry> getDefaultEntries();

    SpatialEntry getRandomSpacialEntryFor(IWorld world, Random random);
}
