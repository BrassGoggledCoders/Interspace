package xyz.brassgoggledcoders.interspace.api.interspace;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

import java.util.Random;

public interface IInterspaceVolumeManager {
    InterspaceVolume getVolume(RegistryKey<World> world, Random random);
}
