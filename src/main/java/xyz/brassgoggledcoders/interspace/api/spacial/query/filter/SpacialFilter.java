package xyz.brassgoggledcoders.interspace.api.spacial.query.filter;

import java.util.Collection;

public abstract class SpacialFilter {

    public abstract String toQuery();

    public abstract Collection<Object> getValues();
}
