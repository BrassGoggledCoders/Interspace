package xyz.brassgoggledcoders.interspace.sql;

public class SQLStatements {
    public static final String ITEM_TABLE_SQL = "CREATE TABLE IF NOT EXISTS %s(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "type TEXT NOT NULL, " +
            "registry_name TEXT NOT NULL, " +
            "count INTEGER NOT NULL CHECK(count >= 0), " +
            "nbt TEXT, " +
            "chunkX INT NOT NULL, " +
            "chunkZ INT NOT NULL," +
            ");";

    public static final String TRANSACTION_TABLE_SQL = "CREATE TABLE IF NOT EXISTS %s(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "item_id INTEGER NOT NULL, " +
            "change INTEGER NOT NULL, " +
            "transactionId TEXT NOT NULL, " +
            "status INTEGER NOT NULL, " +
            "FOREIGN KEY(item_id) REFERENCES %s(id)" +
            "}";

    public static final String TRANSACTION_TRIGGER_SQL = "CREATE TRIGGER IF NOT EXISTS %1$s_trigger(" +
            "AFTER INSERT ON %1$s " +
            "BEGIN " +
            "  UPDATE %2$s SET count + NEW.change WHERE id = NEW.item_id " +
            "END";

    public static final String SELECT_ITEM_SQL = "SELECT id, type, registry_name, count, nbt, chunkX, chunkZ FROM %s " +
            "WHERE chunkX = ? AND chunkZ = ? AND type = ? AND registry_name = ? and nbt = ?";

    public static final String INSERT_ITEM_SQL = "INSERT INTO %s(type, registry_name, count, nbt, chunkX, chunkZ) " +
            "VALUE(?, ?, 0, ?, ?, ?)";
}
