package xyz.brassgoggledcoders.interspace.sql;

public class SQLStatements {
    public static final String CHUNK_SQL = "CREATE TABLE IF NOT EXISTS \"%s_chunks\"" +
            "(" +
            "    id     INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    x      INTEGER NOT NULL," +
            "    z      INTEGER NOT NULL," +
            "    volume INTEGER NOT NULL," +
            "    luck   INTEGER," +
            "    UNIQUE (x, z)" +
            ")";

    public static final String INSERT_CHUNK_SQL = "INSERT OR %s INTO \"%s_chunks\"" +
            "(x, z, volume) " +
            "VALUES " +
            "(?, ?, ?)";

    public static final String CACHE_SQL = "CREATE TABLE IF NOT EXISTS \"%1$s_caches\"" +
            "(" +
            "    id       INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    chunk_id INTEGER NOT NULL," +
            "    name     TEXT    NOT NULL," +
            "    luck     INTEGER," +
            "    FOREIGN KEY (chunk_id) REFERENCES \"%1$s_chunks\"(id)" +
            ")";

    public static final String ITEM_TABLE_SQL = "CREATE TABLE IF NOT EXISTS \"%1$s_items\"" +
            "(" +
            "    id            INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    type          TEXT    NOT NULL," +
            "    registry_name TEXT    NOT NULL," +
            "    count         INTEGER NOT NULL," +
            "    nbt           TEXT    NOT NULL," +
            "    chunk_id      INTEGER NOT NULL," +
            "    FOREIGN KEY (chunk_id) REFERENCES \"%1$s_chunks\"(id)," +
            "    UNIQUE (type, registry_name, nbt, chunk_id)" +
            ")";

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
