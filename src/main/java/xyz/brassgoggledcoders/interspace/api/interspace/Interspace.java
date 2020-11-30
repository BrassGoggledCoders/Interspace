package xyz.brassgoggledcoders.interspace.api.interspace;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.NonNullLazy;

public class Interspace {
    private final long id;
    private final ResourceLocation world;
    private final int volume;
    private final ChunkPos chunkPos;
    private final NonNullLazy<ITextComponent> loadDescriptor;

    public Interspace(long id, ResourceLocation world, int volume, ChunkPos chunkPos) {
        this.id = id;
        this.world = world;
        this.volume = volume;
        this.chunkPos = chunkPos;
        this.loadDescriptor = NonNullLazy.concurrentOf(this::loadDescriptor);
    }

    public ResourceLocation getWorld() {
        return world;
    }

    public int getVolume() {
        return volume;
    }

    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    public ITextComponent getDescriptor() {
        return loadDescriptor.get();
    }

    private ITextComponent loadDescriptor() {
        int power = 0;
        if (this.getVolume() > 0) {
            power = (int) Math.ceil(Math.log(this.volume) / Math.log(2));
        }
        return new TranslationTextComponent("text.interspace.volume." + power);
    }

    public long getId() {
        return id;
    }
}
