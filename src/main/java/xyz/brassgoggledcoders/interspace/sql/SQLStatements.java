package xyz.brassgoggledcoders.interspace.sql;

public class SQLStatements {
    public static final String DATA_TABLE_SQL = "CREATE TABLE IF NOT EXISTS %s(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "registry_name TEXT NOT NULL, " +
            "count INT NOT NULL, " +
            "nbt TEXT, " +
            "chunkX INT NOT NULL, " +
            "chunkZ INT NOT NULL" +
            ");";

    public static final String MARKER_TABLE_SQL = "CREATE TABLE IF NOT EXISTS %s(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "data_id INT NOT NULL, " +
            "marker TEXT NOT NULL, " +
            "value TEXT NOT NULL, " +
            "FOREIGN KEY(data_id) REFERENCES %s(id)" +
            ");";

    public static final String CHUNK_TABLE_SQL = "CREATE TABLE IF NOT EXISTS %s(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "registry_name TEXT NOT NULL, " +
            "nbt TEXT NOT NULL, " +
            "chunkX INT NOT NULL, " +
            "chunkZ INT NOT NULL" +
            ");";
}
