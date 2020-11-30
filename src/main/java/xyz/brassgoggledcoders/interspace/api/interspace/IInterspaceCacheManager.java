package xyz.brassgoggledcoders.interspace.api.interspace;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public interface IInterspaceCacheManager {
    List<InterspaceCache> getRandomCaches(RegistryKey<World> world, Random random, float chance);
}
