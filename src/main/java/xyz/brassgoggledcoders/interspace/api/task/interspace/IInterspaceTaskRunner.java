package xyz.brassgoggledcoders.interspace.api.task.interspace;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.interspace.api.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.sql.ISQLClient;
import xyz.brassgoggledcoders.interspace.api.task.ITaskRunner;

import javax.annotation.Nonnull;
import java.util.concurrent.Future;

public interface IInterspaceTaskRunner extends ITaskRunner {
    @Nonnull
    ISQLClient getSQLClient();

    Future<Interspace> getInterspace(RegistryKey<World> world, ChunkPos chunkPos);
}
