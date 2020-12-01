package xyz.brassgoggledcoders.interspace.interspace;

import com.google.gson.JsonElement;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceVolumeManager;
import xyz.brassgoggledcoders.interspace.api.interspace.InterspaceVolume;
import xyz.brassgoggledcoders.interspace.json.JsonManager;

import java.util.Random;

public class InterspaceVolumeManager extends JsonManager<InterspaceVolume> implements IInterspaceVolumeManager {
    public InterspaceVolumeManager() {
        super("volume");
    }

    @Override
    public InterspaceVolume getVolume(RegistryKey<World> world, Random random) {
        return this.getValue(world.getLocation()).next(random);
    }

    @Override
    protected InterspaceVolume parse(JsonElement jsonElement) {
        return InterspaceVolume.fromJson(jsonElement);
    }

    @Override
    protected InterspaceVolume createDefault() {
        return new InterspaceVolume(16, 1.0, InterspaceMod.getServerConfig()
                .getDefaultCacheChance()
                .floatValue(), 1, 0F);
    }

    @Override
    protected double getWeight(InterspaceVolume value) {
        return value.getWeight();
    }
}
