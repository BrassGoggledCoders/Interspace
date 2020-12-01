package xyz.brassgoggledcoders.interspace.task.interspace;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.interspace.InterspaceCache;
import xyz.brassgoggledcoders.interspace.api.interspace.InterspaceVolume;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;
import xyz.brassgoggledcoders.interspace.api.task.interspace.IInterspaceTaskRunner;
import xyz.brassgoggledcoders.interspace.api.task.interspace.InterspaceTask;
import xyz.brassgoggledcoders.interspace.content.InterspaceTaskTypes;
import xyz.brassgoggledcoders.interspace.sql.SQLPreparations;
import xyz.brassgoggledcoders.interspace.sql.SQLStatements;
import xyz.brassgoggledcoders.interspace.util.NBTTransformers;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;

public class SetupChunkInterspaceTask extends InterspaceTask {
    private ResourceLocation world;
    private ChunkPos chunkPos;
    private InterspaceVolume volume;
    private List<InterspaceCache> caches;
    private boolean force;

    public SetupChunkInterspaceTask(TaskType type) {
        super(type);
    }

    public SetupChunkInterspaceTask(ResourceLocation world, ChunkPos chunkPos, InterspaceVolume volume,
                                    List<InterspaceCache> caches, boolean force) {
        this(InterspaceTaskTypes.SETUP_CHUNK_INTERSPACE.get());
        this.world = world;
        this.chunkPos = chunkPos;
        this.volume = volume;
        this.caches = caches;
        this.force = force;
    }

    @Override
    public int getPriority() {
        return 750;
    }

    @Override
    public void run(IInterspaceTaskRunner taskRunner) {
        try {
            long chunkId = taskRunner.getSQLClient()
                    .insert(String.format(SQLStatements.INSERT_CHUNK_SQL, force ? "REPLACE" : "IGNORE", world.toString()),
                            preparedStatement -> {
                                preparedStatement.setInt(1, chunkPos.x);
                                preparedStatement.setInt(2, chunkPos.z);
                                preparedStatement.setInt(3, volume.getVolume());
                            }
                    );
            if (!caches.isEmpty()) {
                taskRunner.getSQLClient()
                        .batchedInsert(String.format(SQLStatements.INSERT_CACHE_SQL, world.toString()),
                                SQLPreparations.CACHE_PREPARATION.apply(chunkId, volume), caches);
            }
        } catch (SQLException sqlException) {
            InterspaceMod.LOGGER.error("Failed to Insert Interspace Chunk", sqlException);
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putString("world", world.toString());
        nbt.put("chunkPos", NBTTransformers.fromChunkPos(chunkPos));
        nbt.put("volume", volume.toNBT());
        nbt.putBoolean("force", force);
        if (!caches.isEmpty()) {
            ListNBT cachesNBT = new ListNBT();
            for (InterspaceCache interspaceCache : caches) {
                cachesNBT.add(interspaceCache.toNBT());
            }
            nbt.put("caches", cachesNBT);
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
        this.caches = Lists.newArrayList();
        if (nbt.contains("caches")) {
            ListNBT cachesNBT = nbt.getList("caches", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < cachesNBT.size(); i++) {
                this.caches.add(InterspaceCache.fromNBT(cachesNBT.getCompound(i)));
            }
        }
    }

    public static void submit(RegistryKey<World> world, Random random, ChunkPos chunkPos) {
        InterspaceVolume volume = InterspaceAPI.getVolumeManager().getVolume(world, random);
        InterspaceAPI.getManager()
                .submitTask(new SetupChunkInterspaceTask(world.getLocation(), chunkPos, volume,
                        InterspaceAPI.getCacheManager().getRandomCaches(world, random, volume.getCacheChance(),
                                volume.getCacheTries()), false
                ));
    }
}
