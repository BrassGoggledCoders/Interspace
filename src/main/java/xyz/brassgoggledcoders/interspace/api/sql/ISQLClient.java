package xyz.brassgoggledcoders.interspace.api.sql;

import xyz.brassgoggledcoders.interspace.api.functional.ThrowingConsumer;
import xyz.brassgoggledcoders.interspace.api.functional.ThrowingFunction;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Supplier;

public interface ISQLClient {
    @Nonnull
    <T> CompletableFuture<T> inTransaction(@Nonnull ThrowingFunction<ISQLClient, T, SQLException> transaction);

    @Nonnull
    Connection getConnection();

    Future<Void> call(@Nonnull String sql) throws SQLException;

    void blockingCall(@Nonnull String sql) throws SQLException;

    <T> T blockingQuery(String sql, ThrowingConsumer<PreparedStatement, SQLException> prepare,
                        ThrowingFunction<ResultSet, T, SQLException> resultSet, Supplier<T> defaultSupplier) throws SQLException;

    <T> CompletableFuture<T> query(String sql, ThrowingConsumer<PreparedStatement, SQLException> prepare,
                                   ThrowingFunction<ResultSet, T, SQLException> resultTransformer);

    <T> CompletableFuture<T> query(String sql, ThrowingConsumer<PreparedStatement, SQLException> prepare,
                                   ThrowingFunction<ResultSet, T, SQLException> resultTransformer, Supplier<T> defaultSupplier);

    long insert(@Nonnull String sql, ThrowingConsumer<PreparedStatement, SQLException> prepare) throws SQLException;
}
