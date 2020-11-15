package xyz.brassgoggledcoders.interspace.api.interspace;

import net.minecraft.util.math.ChunkPos;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspace;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceStorage;

public class EmptyInterspaceStorage implements IInterspaceStorage {
    @Override
    public IInterspace getInterspace(ChunkPos chunkPos) {
        return null;
    }
}
