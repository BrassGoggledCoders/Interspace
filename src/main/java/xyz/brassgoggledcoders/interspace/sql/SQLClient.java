package xyz.brassgoggledcoders.interspace.sql;

import com.google.common.collect.Lists;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.functional.ThrowingBiConsumer;
import xyz.brassgoggledcoders.interspace.api.functional.ThrowingConsumer;
import xyz.brassgoggledcoders.interspace.api.functional.ThrowingFunction;
import xyz.brassgoggledcoders.interspace.api.sql.ISQLClient;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Supplier;

public class SQLClient implements ISQLClient, AutoCloseable {
    private final Connection connection;

    public SQLClient(Connection connection) {
        this.connection = connection;
    }

    @Nonnull
    @Override
    public <T> T inTransaction(@Nonnull ThrowingFunction<ISQLClient, T, SQLException> transaction) {
        try {
            this.getConnection().setAutoCommit(false);
            return transaction.apply(this);
        } catch (SQLException sqlException) {
            try {
                this.getConnection().rollback();
            } catch (SQLException rollBackException) {
                InterspaceMod.LOGGER.error("Tried to rollback, Failed", rollBackException);
            }
            throw new IllegalStateException("Failed to Run Interspace Transaction", sqlException);
        } finally {
            try {
                this.getConnection().setAutoCommit(true);
            } catch (SQLException sqlException) {
                InterspaceMod.LOGGER.error("Failed to Reset Interspace Auto Commit");
            }
        }
    }

    @Nonnull
    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public Future<Void> call(@Nonnull String sql) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                blockingCall(sql);
                return null;
            } catch (SQLException sqlException) {
                throw new IllegalStateException("Failed to Call Interspace", sqlException);
            }
        });
    }

    @Override
    public void blockingCall(@Nonnull String sql) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.execute();
        } catch (SQLException sqlException) {
            InterspaceMod.LOGGER.error("Failed to Run Interspace call", sqlException);
            throw sqlException;
        }
    }

    @Override
    public <T> T blockingQuery(String sql, ThrowingConsumer<PreparedStatement, SQLException> prepare,
                               ThrowingFunction<ResultSet, T, SQLException> resultTransformer,
                               Supplier<T> defaultSupplier) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            prepare.accept(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultTransformer.apply(resultSet);
            } else {
                return defaultSupplier.get();
            }
        } catch (SQLException sqlException) {
            InterspaceMod.LOGGER.error("Failed to Run Interspace Query", sqlException);
            throw sqlException;
        }
    }

    @Override
    public <T> CompletableFuture<T> query(String sql, ThrowingConsumer<PreparedStatement, SQLException> prepare,
                                          ThrowingFunction<ResultSet, T, SQLException> resultTransformer) {
        return query(sql, prepare, resultTransformer, () -> null);
    }

    @Override
    public <T> CompletableFuture<T> query(String sql, ThrowingConsumer<PreparedStatement, SQLException> prepare,
                                          ThrowingFunction<ResultSet, T, SQLException> resultTransformer,
                                          Supplier<T> defaultSupplier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return this.blockingQuery(sql, prepare, resultTransformer, defaultSupplier);
            } catch (SQLException sqlException) {
                throw new IllegalStateException("Failed to Run Interspace Query", sqlException);
            }
        });
    }

    @Override
    public long insert(@Nonnull String sql, ThrowingConsumer<PreparedStatement, SQLException> prepare) throws SQLException {
        try (PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql)) {
            prepare.accept(preparedStatement);
            preparedStatement.execute();
            if (preparedStatement.getUpdateCount() > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }

        return -1;
    }

    public <T> List<Long> batchedInsert(@Nonnull String sql, ThrowingBiConsumer<PreparedStatement, T, SQLException> prepare,
                                        List<T> inserts) throws SQLException {
        List<Long> insertedIds = Lists.newArrayList();
        try (PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql)) {
            for (T insert : inserts) {
                prepare.accept(preparedStatement, insert);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                insertedIds.add(resultSet.getLong(1));
            }
        }


        return insertedIds;
    }


    @Override
    public void close() throws SQLException {
        connection.close();
    }

    public static SQLClient create(Path path) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        SQLiteConnectionPoolDataSource dataSource = new SQLiteConnectionPoolDataSource();
        dataSource.setUrl("jdbc:sqlite:" + path.resolve("main.db").toString());
        return new SQLClient(dataSource.getConnection());
    }
}
