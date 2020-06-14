package xyz.brassgoggledcoders.interspace.api.spacial.capability;

import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialInstance;

import javax.annotation.Nonnull;

public interface IInterspaceChunk {
    @Nonnull
    SpacialInstance getSpacialInstance();
}
