package xyz.brassgoggledcoders.interspace.spatial.capability;

import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatial;
import xyz.brassgoggledcoders.interspace.api.spatial.item.SpatialItem;
import xyz.brassgoggledcoders.interspace.api.spatial.query.SpatialQueryBuilder;
import xyz.brassgoggledcoders.interspace.api.spatial.query.Transaction;

import java.util.Collection;

public class EmptySpatial implements ISpatial {
    @Override
    public Transaction<Collection<SpatialItem>> offer(Collection<SpatialItem> offered) {
        return Transaction.of(offered);
    }

    @Override
    public Transaction<Collection<SpatialItem>> query(SpatialQueryBuilder spatialQueryBuilder) {
        return Transaction.ofEmpty();
    }

    @Override
    public Transaction<Collection<SpatialItem>> retrieve(SpatialQueryBuilder spatialQueryBuilder) {
        return Transaction.ofEmpty();
    }
}
