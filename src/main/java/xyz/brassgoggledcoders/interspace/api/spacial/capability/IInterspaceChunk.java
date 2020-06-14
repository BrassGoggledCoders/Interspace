package xyz.brassgoggledcoders.interspace.api.spacial.capability;

import xyz.brassgoggledcoders.interspace.api.spacial.type.SpacialInstance;

import javax.annotation.Nonnull;

public interface IInterspaceChunk extends IInterspace{
    @Nonnull
    SpacialInstance getSpacialInstance();
}
