package xyz.brassgoggledcoders.interspace.api.sql;

import xyz.brassgoggledcoders.interspace.api.functional.ThrowingFunction;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Future;

public interface ISQLClient {
    @Nonnull
    <T> Future<T> inTransaction(@Nonnull ThrowingFunction<ISQLClient, T, SQLException> transaction);

    @Nonnull
    Connection getConnection();

    void blockingCall(@Nonnull String sql) throws SQLException;
}
