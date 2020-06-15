package xyz.brassgoggledcoders.interspace.api.spatial.query.filter;

import xyz.brassgoggledcoders.interspace.api.spatial.query.field.SpatialComparison;
import xyz.brassgoggledcoders.interspace.api.spatial.query.field.SpatialField;

import java.util.Collection;
import java.util.Collections;

public class ValueSpatialFilter extends SpatialFilter {
    private final SpatialField field;
    private final SpatialComparison comparison;
    private final Object value;

    public ValueSpatialFilter(SpatialField field, SpatialComparison comparison, Object value) {
        this.field = field;
        this.comparison = comparison;
        this.value = value;
    }

    @Override
    public String toQuery() {
        return comparison.parse(field);
    }

    @Override
    public Collection<Object> getValues() {
        return Collections.singletonList(value);
    }
}
