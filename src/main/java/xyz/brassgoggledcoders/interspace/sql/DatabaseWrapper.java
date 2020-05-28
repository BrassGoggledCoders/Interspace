package xyz.brassgoggledcoders.interspace.sql;

import com.google.common.collect.Lists;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;
import xyz.brassgoggledcoders.interspace.Interspace;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class DatabaseWrapper implements AutoCloseable {
    private static DatabaseWrapper instance;

    private final Connection connection;

    private DatabaseWrapper(Connection connection) {
        this.connection = connection;
    }

    public <T> CompletableFuture<List<T>> query(String sql, ThrowingConsumer<PreparedStatement, SQLException> prepared,
                                                ThrowingFunction<ResultSet, T, SQLException> resultSetParser) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                prepared.accept(preparedStatement);
                List<T> results = Lists.newArrayList();
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    T value = resultSetParser.apply(resultSet);
                    if (value != null) {
                        results.add(value);
                    }
                }
                resultSet.close();
                return results;
            } catch (SQLException sqlException) {
                Interspace.LOGGER.error("Failed to query Interspace Database", sqlException);
            }
            return Collections.emptyList();
        });
    }

    public Future<Integer> insert(String sql, ThrowingConsumer<PreparedStatement, SQLException> prepared) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                prepared.accept(preparedStatement);
                if (!preparedStatement.execute()) {
                    return preparedStatement.getUpdateCount();
                }
            } catch (SQLException sqlException) {
                Interspace.LOGGER.error("Failed to query Interspace Database", sqlException);
            }
            return 0;
        });
    }

    public <U> Integer batchInsert(String sql, Collection<U> inserts, ThrowingBiConsumer<PreparedStatement, U, SQLException> preparations) {
        try {
            int inserted = 0;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (U insert : inserts) {
                preparations.accept(preparedStatement, insert);
                preparedStatement.addBatch();
            }
            for (int amountInserted : preparedStatement.executeBatch()) {
                inserted += amountInserted;
            }
            return inserted;
        } catch (SQLException sqlException) {
            Interspace.LOGGER.error("Failed to query Interspace Database", sqlException);
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

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    @Nullable
    public static DatabaseWrapper getInstance() {
        return instance;
    }

    public static void handleServerStart(FMLServerStartingEvent event) {
        Interspace.LOGGER.info("Creating Interspace Database for: " + event.getServer().getWorldName());
        if (instance != null) {
            try {
                instance.close();
            } catch (Exception e) {
                Interspace.LOGGER.error("Failed to Close Database Connection", e);
            }
        }

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException classNotFoundException) {
            Interspace.LOGGER.error("Failed to Load Database Connector", classNotFoundException);
            throw new IllegalStateException("Failed to Load Database Connector", classNotFoundException);
        }

        SQLiteConnectionPoolDataSource dataSource = new SQLiteConnectionPoolDataSource();
        dataSource.setUrl("jdbc:sqlite:" + event.getServer().getActiveAnvilConverter().getSavesDir()
                .resolve(event.getServer().getFolderName()).resolve("interspace.db").toString());

        try {
            instance = new DatabaseWrapper(dataSource.getConnection());
        } catch (SQLException sqlException) {
            Interspace.LOGGER.error("Failed to Create Database Connection", sqlException);
            throw new IllegalStateException("Failed to Create Database Connection", sqlException);
        }
    }

    public static void handleServerStop(FMLServerStoppingEvent event) {
        Interspace.LOGGER.info("Shutting down Interspace Database for: " + event.getServer().getWorldName());

        try {
            instance.close();
        } catch (Exception e) {
            Interspace.LOGGER.error("Failed to Close Database Connection", e);
        }
    }

    public static ThrowingConsumer<PreparedStatement, SQLException> nothing() {
        return preparedStatement -> {
        };
    }
}
