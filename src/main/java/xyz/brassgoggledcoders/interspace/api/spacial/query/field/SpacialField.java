package xyz.brassgoggledcoders.interspace.api.spacial.query.field;

public enum SpacialField {
    ID("id", SpacialFieldType.LONG),
    TYPE("type", SpacialFieldType.STRING),
    REGISTRY_NAME("registry_name", SpacialFieldType.STRING),
    COUNT("count", SpacialFieldType.LONG),
    NBT("nbt", SpacialFieldType.STRING),
    CHUNK_X("chunk_x", SpacialFieldType.LONG),
    CHUNK_Z("chunk_z", SpacialFieldType.LONG);

    private final String fieldName;
    private final SpacialFieldType type;

    SpacialField(String fieldName, SpacialFieldType type) {
        this.fieldName = fieldName;
        this.type = type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public SpacialFieldType getType() {
        return type;
    }
}
