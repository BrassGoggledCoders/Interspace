package xyz.brassgoggledcoders.interspace.spacial;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorld;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.interspace.InterspaceRegistries;
import xyz.brassgoggledcoders.interspace.api.spacial.IInterspaceClient;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItem;
import xyz.brassgoggledcoders.interspace.api.spacial.item.SpacialItemType;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceInsert;
import xyz.brassgoggledcoders.interspace.api.spacial.query.InterspaceQuery;
import xyz.brassgoggledcoders.interspace.sql.DatabaseTableNames;
import xyz.brassgoggledcoders.interspace.sql.DatabaseWrapper;
import xyz.brassgoggledcoders.interspace.sql.SQLStatements;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class InterspaceClient implements IInterspaceClient {
    private static final String INSERT_SPACIAL_ITEM_SQL = "INSERT INTO %s(type, registry_name, count, nbt, chunkX, chunkZ) " +
            "VALUES(?, ?, ?, ?, ?, ?);";

    private static final String QUERY_SPACIAL_ITEM_SQL = "SELECT item.id, item.type, item.registry_name, item.count, item.nbt FROM %s item " +
            "WHERE item.chunkX = ? AND item.chunkZ = ? ";

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
                        preparedStatement.setString(1, Objects.requireNonNull(value.getType().getRegistryName()).toString());
                        preparedStatement.setString(2, value.getRegistryName());
                        preparedStatement.setInt(3, value.getCount());
                        if (value.getNBT() != null) {
                            preparedStatement.setString(4, value.getNBT().toString());
                        } else {
                            preparedStatement.setNull(4, Types.VARCHAR);
                        }
                        preparedStatement.setInt(5, insert.getChunkPos().x);
                        preparedStatement.setInt(6, insert.getChunkPos().z);
                    }
            ));
        } else {
            return CompletableFuture.completedFuture(-1);
        }
    }

    @Override
    public CompletableFuture<List<SpacialItem>> remove(InterspaceQuery query) {
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance();
        if (databaseWrapper != null) {
            DatabaseTableNames databaseTableNames = this.getDatabaseTableNames(query.getWorld());
            return databaseWrapper.query(
                    String.format(QUERY_SPACIAL_ITEM_SQL, databaseTableNames.getItemTableName(),
                            databaseTableNames.getTransactionTableName()),
                    preparedStatement -> {
                        preparedStatement.setInt(1, query.getChunkPos().x);
                        preparedStatement.setInt(2, query.getChunkPos().z);
                    },
                    resultSet -> {
                        long id = resultSet.getLong(1);
                        SpacialItemType<?> spacialItemType = InterspaceRegistries.SPACIAL_ITEM_TYPES.getValue(
                                new ResourceLocation(resultSet.getString(2)));

                        String registryName = resultSet.getString(3);
                        int count = resultSet.getInt(4);
                        String nbtString = resultSet.getString(5);
                        if (spacialItemType != null) {
                            CompoundNBT compoundNBT = null;
                            if (nbtString != null) {
                                try {
                                    compoundNBT = JsonToNBT.getTagFromJson(nbtString);
                                } catch (CommandSyntaxException e) {
                                    throw new SQLException("Failed to Parse NBT for Id " + id + " in Table " +
                                            databaseTableNames.getItemTableName(), e);
                                }
                            }

                            return new SpacialItem(spacialItemType, registryName, count, compoundNBT);
                        } else {
                            return null;
                        }
                    }
            );
        } else {
            return CompletableFuture.completedFuture(Collections.emptyList());
        }
    }

    private DatabaseTableNames getDatabaseTableNames(IWorld world) {
        return databaseTableNamesMap.computeIfAbsent(Objects.requireNonNull(world.getDimension().getType().getRegistryName()),
                DatabaseTableNames::new);
    }

    private List<String> setupTableSql(DatabaseTableNames databaseTableNames) {
        return Lists.newArrayList(
                String.format(SQLStatements.ITEM_TABLE_SQL, databaseTableNames.getItemTableName()),
                String.format(SQLStatements.TRANSACTION_TABLE_SQL, databaseTableNames.getTransactionTableName(), databaseTableNames.getItemTableName()),
                String.format(SQLStatements.TRANSACTION_TRIGGER_SQL, databaseTableNames.getTransactionTableName(), databaseTableNames.getItemTableName())
        );
    }
}
