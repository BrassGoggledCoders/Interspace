package xyz.brassgoggledcoders.interspace.interspace;

import com.google.gson.JsonElement;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.interspace.InterspaceCache;
import xyz.brassgoggledcoders.interspace.json.JsonManager;

public class InterspaceCacheManager extends JsonManager<InterspaceCache> {
    public InterspaceCacheManager() {
        super("cache");
    }

    @Override
    protected InterspaceCache parse(JsonElement jsonElement) {
        return null;
    }

    @Override
    protected InterspaceCache createDefault() {
        return new InterspaceCache(InterspaceMod.rl("junk"), false, 1.0D, null);
    }

    @Override
    protected double getWeight(InterspaceCache value) {
        return value.getWeight();
    }
}
