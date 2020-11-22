package xyz.brassgoggledcoders.interspace.api.interspace;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Map;
import java.util.stream.Stream;

public interface IInterspaceVolumeManager {
    int getVolume(RegistryKey<World> world);

    Stream<InterspaceVolume> getVolumes(RegistryKey<World> world);

    Map<ResourceLocation, Stream<InterspaceVolume>> getAllVolumes();
}
