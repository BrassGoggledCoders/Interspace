package xyz.brassgoggledcoders.interspace.spacial;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.spacial.IInterspaceClient;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.parameter.SpacialParameter;
import xyz.brassgoggledcoders.interspace.sql.DatabaseTableNames;
import xyz.brassgoggledcoders.interspace.sql.SQLStatements;
import xyz.brassgoggledcoders.interspace.sql.ThrowingConsumer;
import xyz.brassgoggledcoders.interspace.sql.ThrowingSupplier;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class InterspaceClient implements IInterspaceClient, AutoCloseable {
    private final Map<ResourceLocation, DatabaseTableNames> databaseTableNamesMap;
    private final Connection connection;

    public InterspaceClient(Connection connection) {
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
    public CompletableFuture<Collection<SpacialItem>> offer(UUID transactionId, IWorld world, ChunkPos chunkPos,
                                                            Collection<SpacialItem> offered) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                DatabaseTableNames databaseTableNames = this.getDatabaseTableNames(world);
                final Iterator<SpacialItem> itemIterator = offered.iterator();
                return this.doInTransaction(() -> {
                    while (itemIterator.hasNext()) {
                        connection.prepareStatement(SQLStatements.INSERT_ITEM_SQL);
                    }
                    return offered;
                });
            } catch (SQLException sqlException) {
                Interspace.LOGGER.error("Failed to Offer Items", sqlException);
                return offered;
            }
        });
    }

    @Override
    public CompletableFuture<Collection<SpacialItem>> query(IWorld world, Collection<SpacialParameter<?>> parameters,
                                                            @Nullable Integer limit) {
        return CompletableFuture.completedFuture(Lists.newArrayList());

    }

    @Override
    public CompletableFuture<Collection<SpacialItem>> retrieve(UUID transactionId, IWorld world,
                                                               Collection<SpacialParameter<?>> parameters,
                                                               @Nullable Integer limit) {
        return CompletableFuture.completedFuture(Lists.newArrayList());
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
                String.format(SQLStatements.ITEM_TABLE_SQL, databaseTableNames.getItemTableName())
        );
    }

    @Override
    public void close() throws Exception {
        this.connection.close();
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

    public <T> T doInTransaction(ThrowingSupplier<T, SQLException> handling) throws SQLException {
        try {
            connection.setAutoCommit(false);
            return handling.get();
        } catch (SQLException sqlException) {
            connection.rollback();
            throw sqlException;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
