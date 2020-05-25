package xyz.brassgoggledcoders.interspace.sql;

import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;
import xyz.brassgoggledcoders.interspace.Interspace;

import javax.annotation.Nullable;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class DatabaseWrapper implements AutoCloseable {
    private static DatabaseWrapper instance;

    private final Connection connection;

    private DatabaseWrapper(Connection connection) {
        this.connection = connection;
    }

    public <T> Future<T> query(String sql, ThrowingConsumer<PreparedStatement, SQLException> prepared,
                               ThrowingFunction<ResultSet, T, SQLException> resultSetParser) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                prepared.accept(preparedStatement);
                if (preparedStatement.execute()) {
                    ResultSet resultSet = preparedStatement.getResultSet();
                    T value = resultSetParser.apply(resultSet);
                    resultSet.close();
                    return value;
                }
            } catch (SQLException sqlException) {
                Interspace.LOGGER.error("Failed to query Interspace Database", sqlException);
            }
            return null;
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
        dataSource.setUrl(new File(event.getServer().getDataDirectory(), "interspace.db").getAbsolutePath());
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
}
