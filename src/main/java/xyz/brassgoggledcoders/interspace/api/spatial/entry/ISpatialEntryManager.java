package xyz.brassgoggledcoders.interspace.api.spatial.entry;

import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Random;

public interface ISpatialEntryManager {
    Collection<SpatialEntry> getSpatialEntriesFor(World world);

    Collection<SpatialEntry> getDefaultEntries();

    SpatialEntry getRandomSpatialEntryFor(World world, Random random);
}
