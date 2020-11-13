package xyz.brassgoggledcoders.interspace.spatial;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Lazy;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.InterspaceRegistries;
import xyz.brassgoggledcoders.interspace.api.spatial.ISpatialClient;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItemType;
import xyz.brassgoggledcoders.interspace.api.spatial.query.SpatialQuery;
import xyz.brassgoggledcoders.interspace.api.spatial.query.SpatialQueryBuilder;
import xyz.brassgoggledcoders.interspace.api.spatial.query.Transaction;
import xyz.brassgoggledcoders.interspace.api.spatial.query.filter.SpatialFilter;
import xyz.brassgoggledcoders.interspace.sql.DatabaseTableNames;
import xyz.brassgoggledcoders.interspace.sql.SQLStatements;
import xyz.brassgoggledcoders.interspace.sql.ThrowingConsumer;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    public CompletableFuture<Integer> setupWorld(World world) {
        return this.multiInsert(this.setupTableSql(this.getDatabaseTableNames(world))
                .stream()
                .map(sql -> Pair.of(sql, this.nothing()))
                .collect(Collectors.toList()));
    }

    @Override
    public Transaction<Collection<SpatialItem>> offer(World world, ChunkPos chunkPos,
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
    public Transaction<Collection<SpatialItem>> query(World world, SpatialQueryBuilder spatialQueryBuilder) {
        return Transaction.of(transactionId -> CompletableFuture.supplyAsync(() -> {
            DatabaseTableNames tableNames = this.getDatabaseTableNames(world);
            SpatialQuery query = spatialQueryBuilder.build();
            String sql = String.format(SQLStatements.QUERY_ITEMS, tableNames.getItemTableName(), query.asSQL());
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                int spot = 1;
                for (SpatialFilter spatialFilter: query.getSpatialFilters()) {
                    for (Object object : spatialFilter.getValues()) {
                        if (object instanceof Long) {
                            preparedStatement.setLong(spot++, (Long) object);
                        } else if (object instanceof Integer) {
                            preparedStatement.setInt(spot++, (Integer) object);
                        } else {
                            preparedStatement.setString(spot++, object.toString());
                        }
                    }
                }
                ResultSet resultSet = preparedStatement.executeQuery();
                List<SpatialItem> spatialItems = Lists.newArrayList();
                while (resultSet.next()) {
                    SpatialItem item = this.getSpatialItem(resultSet);
                    if (item != null) {
                        spatialItems.add(item);
                    }
                }
                return spatialItems;
            } catch (SQLException sqlException) {
                Interspace.LOGGER.error("Failed to Query Items", sqlException);
            }
            return Lists.newArrayList();
        }));

    }

    @Nullable
    public SpatialItem getSpatialItem(ResultSet resultSet) throws SQLException {
        SpatialItemType<?> type = InterspaceRegistries.SPACIAL_ITEM_TYPES.getValue(new ResourceLocation(resultSet.getString(2)));
        if (type != null) {
            return new SpatialItem(
                    type,
                    resultSet.getString(3),
                    resultSet.getInt(4),
                    this.handleNBT(resultSet.getString(5))
            );
        } else {
            return null;
        }
    }

    private Lazy<CompoundNBT> handleNBT(final String string) {
        return Lazy.of(() -> {
            try {
                return JsonToNBT.getTagFromJson(string);
            } catch (CommandSyntaxException e) {
                Interspace.LOGGER.error("Failed to Parse NBT from Database", e);
            }
            return null;
        });
    }

    @Override
    public Transaction<Collection<SpatialItem>> retrieve(World world, SpatialQueryBuilder spatialQueryBuilder) {
        return Transaction.of(transactionId -> CompletableFuture.completedFuture(Lists.newArrayList()));
    }

    @Override
    public Transaction<Either<String, Integer>> cancel(World world, UUID transactionIdToCancel) {
        return Transaction.of(transactionId -> CompletableFuture.supplyAsync(() -> {
            DatabaseTableNames tableNames = this.getDatabaseTableNames(world);
            String sql = String.format(SQLStatements.DELETE_TRANSACTIONS, tableNames.getTransactionTableName());
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, transactionIdToCancel.toString());
                return Either.right(statement.executeUpdate());
            } catch (SQLException sqlException) {
                return Either.left(sqlException.getMessage());
            }
        }));
    }

    private DatabaseTableNames getDatabaseTableNames(World world) {
        return databaseTableNamesMap.computeIfAbsent(Objects.requireNonNull(world.getDimensionKey().getLocation()),
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
