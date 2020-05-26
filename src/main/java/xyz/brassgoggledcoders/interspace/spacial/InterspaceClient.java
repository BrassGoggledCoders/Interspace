package xyz.brassgoggledcoders.interspace.spacial;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.interspace.api.spacial.IInterspaceClient;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceInsert;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceQuery;
import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialInstance;
import xyz.brassgoggledcoders.interspace.content.InterspaceSpacialTypes;
import xyz.brassgoggledcoders.interspace.sql.DatabaseTableNames;
import xyz.brassgoggledcoders.interspace.sql.DatabaseWrapper;
import xyz.brassgoggledcoders.interspace.sql.SQLStatements;

import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class InterspaceClient implements IInterspaceClient {
    private static final String INSERT_SPACIAL_ITEM_SQL = "INSERT INTO %s(registry_name, count, nbt, chunkX, chunkZ) " +
            "VALUES(?, ?, ?, ?, ?);";

    private final Map<ResourceLocation, DatabaseTableNames> databaseTableNamesMap;

    public InterspaceClient() {
        databaseTableNamesMap = Maps.newHashMap();
    }

    @Override
    public CompletableFuture<Integer> setupWorld(IWorld world) {
        DatabaseWrapper wrapper = DatabaseWrapper.getInstance();
        if (wrapper != null) {
            return DatabaseWrapper.getInstance().multiInsert(this.setupTableSql(this.getDatabaseTableNames(world))
                    .stream()
                    .map(sql -> Pair.of(sql, DatabaseWrapper.nothing()))
                    .collect(Collectors.toList()));
        } else {
            return CompletableFuture.completedFuture(0);
        }
    }

    @Override
    public CompletableFuture<Pair<ChunkPos, SpacialInstance>> setupChunk(IWorld world, IChunk chunk) {
        return CompletableFuture.completedFuture(Pair.of(chunk.getPos(), world.getRandom().nextInt(10) != 0 ?
            InterspaceSpacialTypes.EMPTY.get().createInstance(world, chunk) :
                InterspaceSpacialTypes.BASIC_CACHE.get().createInstance(world, chunk)));
    }

    @Override
    public CompletableFuture<List<SpacialItem>> query(InterspaceQuery query) {
        return CompletableFuture.completedFuture(Lists.newArrayList());
    }

    @Override
    public CompletableFuture<Integer> insert(InterspaceInsert insert) {
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance();
        if (databaseWrapper != null) {
            DatabaseTableNames databaseTableNames = this.getDatabaseTableNames(insert.getWorld());
            return CompletableFuture.supplyAsync(() -> databaseWrapper.batchInsert(
                    String.format(INSERT_SPACIAL_ITEM_SQL, databaseTableNames.getItemTableName()), insert.getSpacialItems(),
                    (preparedStatement, value) -> {
                        preparedStatement.setString(1, value.getRegistryName());
                        preparedStatement.setInt(2, value.getCount());
                        if (value.getNBT() != null) {
                            preparedStatement.setString(3, value.getNBT().toString());
                        } else {
                            preparedStatement.setNull(3, Types.VARCHAR);
                        }
                        preparedStatement.setInt(4, insert.getChunkPos().x);
                        preparedStatement.setInt(5, insert.getChunkPos().z);
                    }
            ));
        } else {
            return CompletableFuture.completedFuture(-1);
        }
    }

    private DatabaseTableNames getDatabaseTableNames(IWorld world) {
        return databaseTableNamesMap.computeIfAbsent(Objects.requireNonNull(world.getDimension().getType().getRegistryName()),
                DatabaseTableNames::new);
    }

    private List<String> setupTableSql(DatabaseTableNames databaseTableNames) {
        return Lists.newArrayList(
                String.format(SQLStatements.DATA_TABLE_SQL, databaseTableNames.getItemTableName()),
                String.format(SQLStatements.MARKER_TABLE_SQL, databaseTableNames.getMarkerTableName(),
                        databaseTableNames.getItemTableName()),
                String.format(SQLStatements.CHUNK_TABLE_SQL, databaseTableNames.getChunkTableName()));
    }
}
