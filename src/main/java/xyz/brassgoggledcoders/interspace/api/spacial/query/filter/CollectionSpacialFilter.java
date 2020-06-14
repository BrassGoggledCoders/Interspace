package xyz.brassgoggledcoders.interspace.api.spacial.query.filter;

import xyz.brassgoggledcoders.interspace.api.spacial.query.field.SpacialComparison;
import xyz.brassgoggledcoders.interspace.api.spacial.query.field.SpacialField;

import java.util.Arrays;
import java.util.Collection;

public class CollectionSpacialFilter extends SpacialFilter {
    private final SpacialField field;
    private final Object[] values;

    public CollectionSpacialFilter(SpacialField field, Object... values) {
        this.field = field;
        this.values = values;
    }

    @Override
    public String toQuery() {
        return SpacialComparison.IN.parse(field, values.length);
    }

    @Override
    public Collection<Object> getValues() {
        return Arrays.asList(values);
    }
}
