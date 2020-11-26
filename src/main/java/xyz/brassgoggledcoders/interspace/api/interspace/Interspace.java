package xyz.brassgoggledcoders.interspace.api.interspace;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.NonNullLazy;

public class Interspace {
    private final long id;
    private final ResourceLocation name;
    private final int volume;
    private final ChunkPos chunkPos;
    private final NonNullLazy<ITextComponent> loadAdjective;

    public Interspace(long id, ResourceLocation world, int volume, ChunkPos chunkPos) {
        this.id = id;
        this.name = world;
        this.volume = volume;
        this.chunkPos = chunkPos;
        this.loadAdjective = NonNullLazy.concurrentOf(this::loadAdjective);
    }

    public ResourceLocation getName() {
        return name;
    }

    public int getVolume() {
        return volume;
    }

    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    public ITextComponent getAdjective() {
        return loadAdjective.get();
    }

    private ITextComponent loadAdjective() {
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
