package xyz.brassgoggledcoders.interspace.spatial;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.spatial.ISpatialClient;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.query.SpatialQueryBuilder;
import xyz.brassgoggledcoders.interspace.api.spatial.query.Transaction;
import xyz.brassgoggledcoders.interspace.sql.DatabaseTableNames;
import xyz.brassgoggledcoders.interspace.sql.SQLStatements;
import xyz.brassgoggledcoders.interspace.sql.ThrowingConsumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SpatialClient implements ISpatialClient, AutoCloseable {
    private final Map<ResourceLocation, DatabaseTableNames> databaseTableNamesMap;
    private final Connection connection;

    public SpatialClient(Connection connection) {
        this.connection = connection;
        this.databaseTableNamesMap = Maps.newHashMap();
    }

    @Override
    public CompletableFuture<Integer> setupWorld(IWorld world) {
        return this.multiInsert(this.setupTableSql(this.getDatabaseTableNames(world))
                .stream()
                .map(sql -> Pair.of(sql, this.nothing()))
                .collect(Collectors.toList()));
    }

    @Override
    public Transaction<Collection<SpatialItem>> offer(IWorld world, ChunkPos chunkPos,
                                                      Collection<SpatialItem> offered) {
        return Transaction.of(transactionId -> CompletableFuture.supplyAsync(() -> {
            DatabaseTableNames databaseTableNames = this.getDatabaseTableNames(world);
            Iterator<SpatialItem> itemIterator = offered.iterator();
            final String transactionIdString = transactionId.toString();
            Set<SpatialItem> notAccepted = Sets.newHashSet();
            while (itemIterator.hasNext()) {
                SpatialItem spatialItem = itemIterator.next();
                int updated = this.insert(String.format(SQLStatements.INSERT_TRANSACTION, databaseTableNames.getTransactionTableName()),
                        preparedStatement -> {
                            preparedStatement.setString(1, spatialItem.getTypeString());
                            preparedStatement.setString(2, spatialItem.getRegistryName());
                            preparedStatement.setLong(3, spatialItem.getCount());
                            if (spatialItem.getNBT() != null) {
                                preparedStatement.setString(4, spatialItem.getNBT().toString());
                            } else {
                                preparedStatement.setString(4, "");
                            }
                            preparedStatement.setLong(5, chunkPos.x);
                            preparedStatement.setLong(6, chunkPos.z);
                            preparedStatement.setString(7, transactionIdString);
                        });
                if (updated == 0) {
                    notAccepted.add(spatialItem);
                }
            }
            return notAccepted;
        }));
    }

    @Override
    public Transaction<Collection<SpatialItem>> query(IWorld world, SpatialQueryBuilder spatialQueryBuilder) {
        return Transaction.of(transactionId -> CompletableFuture.completedFuture(Lists.newArrayList()));

    }

    @Override
    public Transaction<Collection<SpatialItem>> retrieve(IWorld world, SpatialQueryBuilder spatialQueryBuilder) {
        return Transaction.of(transactionId -> CompletableFuture.completedFuture(Lists.newArrayList()));
    }

    private DatabaseTableNames getDatabaseTableNames(IWorld world) {
        return databaseTableNamesMap.computeIfAbsent(Objects.requireNonNull(world.getDimension().getType().getRegistryName()),
                DatabaseTableNames::new);
    }

    private List<String> setupTableSql(DatabaseTableNames databaseTableNames) {
        return Lists.newArrayList(
                String.format(SQLStatements.ITEM_TABLE_SQL, databaseTableNames.getItemTableName()),
                String.format(SQLStatements.TRANSACTION_TABLE_SQL, databaseTableNames.getTransactionTableName()),
                String.format(SQLStatements.TRANSACTION_TRIGGER_SQL, databaseTableNames.getTransactionTableName(), databaseTableNames.getItemTableName()),
                String.format(SQLStatements.ITEM_CHECK_INVENTORY_TRIGGER, databaseTableNames.getItemTableName())
        );
    }

    @Override
    public void close() throws Exception {
        this.connection.close();
    }

    public int insert(String sql, ThrowingConsumer<PreparedStatement, SQLException> preparer) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparer.accept(preparedStatement);
            if (!preparedStatement.execute()) {
                return preparedStatement.getUpdateCount();
            }
        } catch (SQLException sqlException) {
            Interspace.LOGGER.error("Failed to Insert Record", sqlException);
        }
        return 0;
    }

    public CompletableFuture<Integer> multiInsert(List<Pair<String, ThrowingConsumer<PreparedStatement, SQLException>>> inserts) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                int inserted = 0;
                for (Pair<String, ThrowingConsumer<PreparedStatement, SQLException>> insert : inserts) {
                    PreparedStatement preparedStatement = connection.prepareStatement(insert.getLeft());
                    insert.getRight().accept(preparedStatement);
                    if (!preparedStatement.execute()) {
                        inserted += preparedStatement.getUpdateCount();
                    }
                }
                return inserted;
            } catch (SQLException sqlException) {
                Interspace.LOGGER.error("Failed to query Interspace Database", sqlException);
            }
            return 0;
        });
    }

    public ThrowingConsumer<PreparedStatement, SQLException> nothing() {
        return preparedStatement -> {
        };
    }
}