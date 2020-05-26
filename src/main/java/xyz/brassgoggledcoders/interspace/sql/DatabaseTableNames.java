package xyz.brassgoggledcoders.interspace.sql;

import net.minecraft.util.ResourceLocation;

public class DatabaseTableNames {
    private final String itemTableName;
    private final String markerTableName;
    private final String chunkTableName;

    public DatabaseTableNames(ResourceLocation dimensionTypeName) {
        String tableName = dimensionTypeName.toString()
                .replace(":", "_")
                .replace("/", "_")
                .replace(".", "_");
        this.itemTableName = tableName + "_items";
        this.markerTableName = tableName + "_markers";
        this.chunkTableName = tableName + "_chunks";
    }

    public String getItemTableName() {
        return itemTableName;
    }

    public String getMarkerTableName() {
        return markerTableName;
    }

    public String getChunkTableName() {
        return chunkTableName;
    }
}
