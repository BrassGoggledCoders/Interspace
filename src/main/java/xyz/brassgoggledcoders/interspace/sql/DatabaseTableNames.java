package xyz.brassgoggledcoders.interspace.sql;

import net.minecraft.util.ResourceLocation;

public class DatabaseTableNames {
    private final String itemTableName;
    private final String transactionTableName;

    public DatabaseTableNames(ResourceLocation dimensionTypeName) {
        String tableName = dimensionTypeName.toString()
                .replace(":", "_")
                .replace("/", "_")
                .replace(".", "_");
        this.itemTableName = tableName + "_items";
        this.transactionTableName = tableName + "_transactions";
    }

    public String getItemTableName() {
        return itemTableName;
    }

    public String getTransactionTableName() {
        return transactionTableName;
    }
}
