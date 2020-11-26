package xyz.brassgoggledcoders.interspace.interspace;

import com.google.common.cache.CacheLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import xyz.brassgoggledcoders.interspace.api.functional.ThrowingFunction;
import xyz.brassgoggledcoders.interspace.api.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.sql.ISQLClient;
import xyz.brassgoggledcoders.interspace.sql.SQLResultSetTransformers;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.function.IntSupplier;

public class InterspaceCacheLoader extends CacheLoader<ChunkPos, Interspace> {
    private final ISQLClient sqlClient;
    private final String world;
    private final ThrowingFunction<ResultSet, Interspace, SQLException> interspaceTransformer;
    private final IntSupplier volumeSupplier;

    public InterspaceCacheLoader(ISQLClient sqlClient, ResourceLocation world, IntSupplier volumeSupplier) {
        this.sqlClient = sqlClient;
        this.interspaceTransformer = SQLResultSetTransformers.INTERSPACE.apply(world);
        this.world = world.toString();
        this.volumeSupplier = volumeSupplier;
    }

    @Override
    public Interspace load(@Nonnull ChunkPos key) throws Exception {
        return sqlClient.inTransaction(transactionClient -> {
            Interspace interspace = transactionClient.blockingQuery(
                    "SELECT * FROM \"" + world + "_chunks\" WHERE x = ? AND z = ?",
                    preparedStatement -> {
                        preparedStatement.setInt(1, key.x);
                        preparedStatement.setInt(2, key.z);
                    },
                    interspaceTransformer,
                    () -> null);

            if (interspace == null) {
                int volume = volumeSupplier.getAsInt();
                long interspaceId = transactionClient.insert(
                        "INSERT INTO \"" + world + "_chunks\"(volume, x, z) VALUES(?, ?, ?)",
                        preparedStatement -> {
                            preparedStatement.setInt(1, volume);
                            preparedStatement.setInt(2, key.x);
                            preparedStatement.setInt(3, key.z);
                        }
                );
                interspace = new Interspace(interspaceId, new ResourceLocation(world), volume, key);
            }

            return interspace;
        }).get(5, TimeUnit.SECONDS);
    }
}
