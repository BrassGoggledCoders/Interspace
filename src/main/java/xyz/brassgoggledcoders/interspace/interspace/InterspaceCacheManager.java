package xyz.brassgoggledcoders.interspace.interspace;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceCacheManager;
import xyz.brassgoggledcoders.interspace.api.interspace.InterspaceCache;
import xyz.brassgoggledcoders.interspace.json.JsonManager;
import xyz.brassgoggledcoders.interspace.util.RandomSelector;

import java.util.List;
import java.util.Random;

public class InterspaceCacheManager extends JsonManager<InterspaceCache> implements IInterspaceCacheManager {
    public InterspaceCacheManager() {
        super("cache");
    }

    @Override
    protected InterspaceCache parse(JsonElement jsonElement) {
        return InterspaceCache.fromJson(jsonElement);
    }

    @Override
    protected InterspaceCache createDefault() {
        return new InterspaceCache(InterspaceMod.rl("junk"), false, 1D, 0F);
    }

    @Override
    protected double getWeight(InterspaceCache value) {
        return value.getWeight();
    }

    @Override
    public List<InterspaceCache> getRandomCaches(RegistryKey<World> world, Random random, float chance, int tries) {
        List<InterspaceCache> interspaceCaches = Lists.newArrayList();
        RandomSelector<InterspaceCache> randomSelector = this.getValue(world.getLocation());
        for (int i = 0; i < tries; i++) {
            while (chance > 1F) {
                chance -= 1F;
                interspaceCaches.add(randomSelector.next(random));
            }
            if (random.nextFloat() < chance) {
                interspaceCaches.add(randomSelector.next(random));
            }
        }
        return interspaceCaches;
    }
}
