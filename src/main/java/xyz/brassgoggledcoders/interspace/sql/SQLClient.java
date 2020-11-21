package xyz.brassgoggledcoders.interspace.sql;

import org.sqlite.javax.SQLiteConnectionPoolDataSource;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.functional.ThrowingFunction;
import xyz.brassgoggledcoders.interspace.api.sql.ISQLClient;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class SQLClient implements ISQLClient, AutoCloseable {
    private final Connection connection;

    public SQLClient(Connection connection) {
        this.connection = connection;
    }

    @Nonnull
    @Override
    public <T> Future<T> inTransaction(@Nonnull ThrowingFunction<ISQLClient, T, SQLException> transaction) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                this.getConnection().setAutoCommit(false);
                return transaction.apply(this);
            } catch (SQLException sqlException) {
                throw new IllegalStateException("Failed to Run Interspace Transaction", sqlException);
            } finally {
                try {
                    this.getConnection().setAutoCommit(true);
                } catch (SQLException sqlException) {
                    InterspaceMod.LOGGER.error("Failed to Reset Interspace Auto Commit");
                }
            }
        });
    }

    @Nonnull
    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void blockingCall(@Nonnull String sql) throws SQLException {
        this.getConnection().prepareStatement(sql).execute();
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
