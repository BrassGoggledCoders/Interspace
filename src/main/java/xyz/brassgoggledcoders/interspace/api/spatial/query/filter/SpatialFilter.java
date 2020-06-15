package xyz.brassgoggledcoders.interspace.api.spatial.query.filter;

import java.util.Collection;

public abstract class SpatialFilter {

    public abstract String toQuery();

    public abstract Collection<Object> getValues();
}
