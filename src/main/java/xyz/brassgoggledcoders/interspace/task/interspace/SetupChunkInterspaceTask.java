package xyz.brassgoggledcoders.interspace.task.interspace;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.interspace.InterspaceVolume;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;
import xyz.brassgoggledcoders.interspace.api.task.interspace.IInterspaceTaskRunner;
import xyz.brassgoggledcoders.interspace.api.task.interspace.InterspaceTask;
import xyz.brassgoggledcoders.interspace.content.InterspaceTaskTypes;
import xyz.brassgoggledcoders.interspace.sql.SQLStatements;
import xyz.brassgoggledcoders.interspace.util.NBTTransformers;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class SetupChunkInterspaceTask extends InterspaceTask {
    private Future<Void> future;

    private ResourceLocation world;
    private ChunkPos chunkPos;
    private InterspaceVolume volume;
    private ResourceLocation cache;
    private boolean force;

    public SetupChunkInterspaceTask(TaskType type) {
        super(type);
    }

    public SetupChunkInterspaceTask(ResourceLocation world, ChunkPos chunkPos, InterspaceVolume volume,
                                    ResourceLocation cache, boolean force) {
        this(InterspaceTaskTypes.SETUP_CHUNK_INTERSPACE.get());
        this.world = world;
        this.chunkPos = chunkPos;
        this.volume = volume;
        this.cache = cache;
        this.force = force;
    }

    @Override
    public boolean isDone() {
        return future != null && future.isDone();
    }

    @Override
    public int getPriority() {
        return 750;
    }

    @Override
    public void run(IInterspaceTaskRunner taskRunner) {
        future = CompletableFuture.supplyAsync(() -> {
            try {
                taskRunner.getSQLClient()
                        .insert(String.format(SQLStatements.INSERT_CHUNK_SQL, force ? "REPLACE" : "IGNORE", world.toString()),
                                preparedStatement -> {
                                    preparedStatement.setInt(1, chunkPos.x);
                                    preparedStatement.setInt(2, chunkPos.z);
                                    preparedStatement.setInt(3, volume.getVolume());
                                    if (cache != null) {
                                        preparedStatement.setString(4, cache.toString());
                                    } else {
                                        preparedStatement.setNull(4, Types.VARCHAR);
                                    }
                                    if (volume.getCacheLuck() != null) {
                                        preparedStatement.setFloat(5, volume.getCacheLuck());
                                    } else {
                                        preparedStatement.setNull(5, Types.FLOAT);
                                    }
                                }
                        );
            } catch (SQLException sqlException) {
                InterspaceMod.LOGGER.error("Failed to Insert Interspace Chunk", sqlException);
            }
            return null;
        });
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putString("world", world.toString());
        nbt.put("chunkPos", NBTTransformers.fromChunkPos(chunkPos));
        nbt.put("volume", volume.toNBT());
        nbt.putBoolean("force", force);
        if (cache != null) {
            nbt.putString("cache", cache.toString());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.world = new ResourceLocation(nbt.getString("world"));
        this.chunkPos = NBTTransformers.toChunkPos(nbt.getCompound("chunkPos"));
        this.volume = InterspaceVolume.fromNBT(nbt.getCompound("volume"));
        this.force = nbt.getBoolean("force");
        if (nbt.contains("cache")) {
            this.cache = new ResourceLocation(nbt.getString("cache"));
        }
    }

    public static void submit(RegistryKey<World> world, Random random, ChunkPos chunkPos) {
        InterspaceAPI.getManager()
                .submitTask(new SetupChunkInterspaceTask(
                        world.getLocation(),
                        chunkPos,
                        InterspaceAPI.getVolumeManager().getVolume(world, random),
                        null,
                        false
                ));
    }
}
