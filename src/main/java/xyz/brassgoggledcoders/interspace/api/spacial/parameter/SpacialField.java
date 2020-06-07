package xyz.brassgoggledcoders.interspace.api.spacial.parameter;

public enum SpacialField {
    ID("id", SpacialParameterType.LONG),
    TYPE("type", SpacialParameterType.STRING),
    REGISTRY_NAME("registry_name", SpacialParameterType.STRING),
    COUNT("count", SpacialParameterType.LONG),
    NBT("nbt", SpacialParameterType.STRING),
    CHUNK_X("chunk_x", SpacialParameterType.LONG),
    CHUNK_Z("chunk_z", SpacialParameterType.LONG);

    private final String fieldName;
    private final SpacialParameterType type;

    SpacialField(String fieldName, SpacialParameterType type) {
        this.fieldName = fieldName;
        this.type = type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public SpacialParameterType getType() {
        return type;
    }
}
