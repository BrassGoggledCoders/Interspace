package xyz.brassgoggledcoders.interspace.api.spatial.capability;

import xyz.brassgoggledcoders.interspace.api.spatial.type.SpatialInstance;

import javax.annotation.Nonnull;

public interface ISpatialChunk extends ISpatial {
    @Nonnull
    SpatialInstance getSpacialInstance();
}
