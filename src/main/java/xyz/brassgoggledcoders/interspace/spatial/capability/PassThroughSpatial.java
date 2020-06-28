package xyz.brassgoggledcoders.interspace.spatial.capability;

import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatial;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.query.SpatialQueryBuilder;
import xyz.brassgoggledcoders.interspace.api.spatial.query.Transaction;

import java.util.Collection;

public class PassThroughSpatial<T extends ISpatial> implements ISpatial {
    private final T spatial;

    public PassThroughSpatial(T spatial) {
        this.spatial = spatial;
    }

    @Override
    public Transaction<Collection<SpatialItem>> offer(Collection<SpatialItem> offered) {
        return spatial.offer(offered);
    }

    @Override
    public Transaction<Collection<SpatialItem>> query(SpatialQueryBuilder spatialQueryBuilder) {
        return spatial.query(spatialQueryBuilder);
    }

    @Override
    public Transaction<Collection<SpatialItem>> retrieve(SpatialQueryBuilder spatialQueryBuilder) {
        return spatial.retrieve(spatialQueryBuilder);
    }
}
