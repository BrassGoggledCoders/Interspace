package xyz.brassgoggledcoders.interspace.sql;

public class SQLStatements {
    public static final String CHUNK_SQL = "CREATE TABLE IF NOT EXISTS \"%s_chunks\"" +
            "(" +
            "    id         INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    max_volume INTEGER NOT NULL," +
            "    x          INTEGER NOT NULL," +
            "    z          INTEGER NOT NULL," +
            "    UNIQUE (x, z)" +
            ")";

    public static final String ITEM_TABLE_SQL = "CREATE TABLE IF NOT EXISTS \"%1$s_items\"" +
            "(" +
            "    id            INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    type          TEXT    NOT NULL," +
            "    registry_name TEXT    NOT NULL," +
            "    count         INTEGER NOT NULL," +
            "    nbt           TEXT    NOT NULL," +
            "    chunk_id      INT     NOT NULL," +
            "    FOREIGN KEY (chunk_id) REFERENCES \"%1$s_chunks\"(id)," +
            "    UNIQUE (type, registry_name, nbt, chunk_id)" +
            ")";

    public static final String TRANSACTION_TABLE_SQL = "CREATE TABLE IF NOT EXISTS \"%1$s_transactions\"" +
            "(" +
            "    id             INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    type           TEXT    NOT NULL," +
            "    registry_name  TEXT    NOT NULL," +
            "    change         INTEGER NOT NULL CHECK (change != 0)," +
            "    nbt            TEXT    NOT NULL," +
            "    chunk_id       INT     NOT NULL," +
            "    transaction_id TEXT    NOT NULL," +
            "    FOREIGN KEY (chunk_id) REFERENCES \"%1$s_chunks\"(id)" +
            ")";

    public static final String TRANSACTION_TRIGGER_SQL = "CREATE TRIGGER IF NOT EXISTS \"%1$s_transactions_trigger\"" +
            "    AFTER INSERT" +
            "    ON \"%1$s_transactions\" " +
            "BEGIN" +
            "    INSERT INTO \"%1$s_items\"(type, registry_name, count, nbt, chunk_id)" +
            "    VALUES (new.type, new.registry_name, new.change, new.nbt, new.chunk_id)" +
            "    ON CONFLICT(type, registry_name, nbt, chunk_id) DO UPDATE SET COUNT = COUNT + excluded.count;" +
            "END;";

    public static final String ITEM_CHECK_INVENTORY_TRIGGER = "CREATE TRIGGER IF NOT EXISTS \"%1$s_items_trigger\"" +
            "    AFTER UPDATE" +
            "    ON \"%1$s_items\" " +
            "BEGIN" +
            "    SELECT CASE" +
            "               WHEN new.count < 0 THEN" +
            "                   raise(ABORT, 'item counts cannot go below 0')" +
            "               END;" +
            "END";
}
