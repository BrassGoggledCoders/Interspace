package xyz.brassgoggledcoders.interspace.api;

import com.mojang.datafixers.util.Function3;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import xyz.brassgoggledcoders.interspace.api.interspace.EmptyInterspaceStorage;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceStorage;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class InterspaceAPI {
    private static final IInterspaceStorage EMPTY = new EmptyInterspaceStorage();
    private static Function<ServerWorld, IInterspaceStorage> storageFunction = world -> EMPTY;

    public static void setStorageFunction(@Nonnull Function<ServerWorld, IInterspaceStorage> storageFunction) {
        InterspaceAPI.storageFunction = storageFunction;
    }

    @Nonnull
    public IInterspaceStorage getStorage(World world) {
        if (world instanceof ServerWorld) {
            return storageFunction.apply((ServerWorld) world);
        } else {
            return EMPTY;
        }
    }
}
