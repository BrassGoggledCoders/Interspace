package xyz.brassgoggledcoders.interspace.api.spatial.query.field;

public enum SpatialField {
    ID("id", SpatialFieldType.LONG),
    TYPE("type", SpatialFieldType.STRING),
    REGISTRY_NAME("registry_name", SpatialFieldType.STRING),
    COUNT("count", SpatialFieldType.LONG),
    NBT("nbt", SpatialFieldType.STRING),
    CHUNK_X("chunk_x", SpatialFieldType.LONG),
    CHUNK_Z("chunk_z", SpatialFieldType.LONG);

    private final String fieldName;
    private final SpatialFieldType type;

    SpatialField(String fieldName, SpatialFieldType type) {
        this.fieldName = fieldName;
        this.type = type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public SpatialFieldType getType() {
        return type;
    }
}
