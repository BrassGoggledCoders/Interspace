package xyz.brassgoggledcoders.interspace.api.sql;

import xyz.brassgoggledcoders.interspace.api.functional.ThrowingFunction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Future;

public interface ISQLClient {
    <T> Future<T> inTransaction(ThrowingFunction<ISQLClient, T, SQLException> transaction);

    Connection getConnection();

    void blockingCall(String sql) throws SQLException;
}
