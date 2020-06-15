package xyz.brassgoggledcoders.interspace.api.spatial.query.filter;

import xyz.brassgoggledcoders.interspace.api.spatial.query.field.SpatialComparison;
import xyz.brassgoggledcoders.interspace.api.spatial.query.field.SpatialField;

import java.util.Arrays;
import java.util.Collection;

public class CollectionSpatialFilter extends SpatialFilter {
    private final SpatialField field;
    private final Object[] values;

    public CollectionSpatialFilter(SpatialField field, Object... values) {
        this.field = field;
        this.values = values;
    }

    @Override
    public String toQuery() {
        return SpatialComparison.IN.parse(field, values.length);
    }

    @Override
    public Collection<Object> getValues() {
        return Arrays.asList(values);
    }
}
