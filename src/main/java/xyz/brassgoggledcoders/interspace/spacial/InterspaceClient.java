package xyz.brassgoggledcoders.interspace.spacial;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.spacial.IInterspaceClient;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.query.filter.SpacialFilter;
import xyz.brassgoggledcoders.interspace.api.spacial.query.Transaction;
import xyz.brassgoggledcoders.interspace.sql.DatabaseTableNames;
import xyz.brassgoggledcoders.interspace.sql.SQLStatements;
import xyz.brassgoggledcoders.interspace.sql.ThrowingConsumer;

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
    public Transaction<Collection<SpacialItem>> offer(IWorld world, ChunkPos chunkPos,
                                                      Collection<SpacialItem> offered) {
        return Transaction.of(transactionId -> CompletableFuture.supplyAsync(() -> {
            DatabaseTableNames databaseTableNames = this.getDatabaseTableNames(world);
            Iterator<SpacialItem> itemIterator = offered.iterator();
            final String transactionIdString = transactionId.toString();
            Set<SpacialItem> notAccepted = Sets.newHashSet();
            while (itemIterator.hasNext()) {
                SpacialItem spacialItem = itemIterator.next();
                int updated = this.insert(String.format(SQLStatements.INSERT_TRANSACTION, databaseTableNames.getTransactionTableName()),
                        preparedStatement -> {
                            preparedStatement.setString(1, spacialItem.getTypeString());
                            preparedStatement.setString(2, spacialItem.getRegistryName());
                            preparedStatement.setLong(3, spacialItem.getCount());
                            if (spacialItem.getNBT() != null) {
                                preparedStatement.setString(4, spacialItem.getNBT().toString());
                            } else {
                                preparedStatement.setString(4, "");
                            }
                            preparedStatement.setLong(5, chunkPos.x);
                            preparedStatement.setLong(6, chunkPos.z);
                            preparedStatement.setString(7, transactionIdString);
                        });
                if (updated == 0) {
                    notAccepted.add(spacialItem);
                }
            }
            return notAccepted;
        }));
    }

    @Override
    public Transaction<Collection<SpacialItem>> query(IWorld world, Collection<SpacialFilter<?>> parameters, int limit) {
        return Transaction.of(transactionId -> CompletableFuture.completedFuture(Lists.newArrayList()));

    }

    @Override
    public Transaction<Collection<SpacialItem>> retrieve(IWorld world, Collection<SpacialFilter<?>> parameters,
                                                         int maxSize, int limit) {
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
