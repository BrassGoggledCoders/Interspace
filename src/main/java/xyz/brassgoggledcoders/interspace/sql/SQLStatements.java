package xyz.brassgoggledcoders.interspace.sql;

public class SQLStatements {
    public static final String ITEM_TABLE_SQL = "CREATE TABLE IF NOT EXISTS %s" +
            "(" +
            "    id            INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    type          TEXT    NOT NULL," +
            "    registry_name TEXT    NOT NULL," +
            "    count         INTEGER NOT NULL," +
            "    nbt           TEXT    NOT NULL," +
            "    chunkX        INT     NOT NULL," +
            "    chunkZ        INT     NOT NULL," +
            "    UNIQUE (type, registry_name, nbt, chunkX, chunkZ)" +
            ")";

    public static final String TRANSACTION_TABLE_SQL = "CREATE TABLE IF NOT EXISTS %s" +
            "(" +
            "    id            INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    type          TEXT    NOT NULL," +
            "    registry_name TEXT    NOT NULL," +
            "    change        INTEGER NOT NULL CHECK (change != 0)," +
            "    nbt           TEXT    NOT NULL," +
            "    chunkX        INT     NOT NULL," +
            "    chunkZ        INT     NOT NULL," +
            "    transactionId TEXT    NOT NULL" +
            ")";

    public static final String TRANSACTION_TRIGGER_SQL = "CREATE TRIGGER IF NOT EXISTS %1$s_trigger" +
            "    AFTER INSERT" +
            "    ON %1$s" +
            "BEGIN" +
            "    INSERT INTO %2$s(type, registry_name, count, nbt, chunkX, chunkZ)" +
            "    VALUES (new.type, new.registry_name, new.change, new.nbt, new.chunkX, new.chunkZ)" +
            "    ON CONFLICT(type, registry_name, nbt, chunkX, chunkZ) DO UPDATE SET COUNT = COUNT + excluded.count;" +
            "END;";

    public static final String ITEM_CHECK_INVENTORY_TRIGGER = "CREATE TRIGGER IF NOT EXISTS %s_trigger" +
            "    AFTER UPDATE" +
            "    ON %s" +
            "BEGIN" +
            "    SELECT CASE" +
            "               WHEN new.count < 0 THEN" +
            "                   raise(ABORT, 'item counts cannot go below 0')" +
            "               END;" +
            "END";
}
