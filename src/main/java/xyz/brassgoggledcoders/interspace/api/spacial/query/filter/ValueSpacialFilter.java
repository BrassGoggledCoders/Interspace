package xyz.brassgoggledcoders.interspace.api.spacial.query.filter;

import xyz.brassgoggledcoders.interspace.api.spacial.query.field.SpacialComparison;
import xyz.brassgoggledcoders.interspace.api.spacial.query.field.SpacialField;

import java.util.Collection;
import java.util.Collections;

public class ValueSpacialFilter extends SpacialFilter {
    private final SpacialField field;
    private final SpacialComparison comparison;
    private final Object value;

    public ValueSpacialFilter(SpacialField field, SpacialComparison comparison, Object value) {
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
