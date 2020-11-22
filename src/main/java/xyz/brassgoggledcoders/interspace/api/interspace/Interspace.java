package xyz.brassgoggledcoders.interspace.api.interspace;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class Interspace {
    private final ResourceLocation name;
    private final int volume;
    private final ChunkPos chunkPos;

    public Interspace(ResourceLocation name, int volume, ChunkPos chunkPos) {
        this.name = name;
        this.volume = volume;
        this.chunkPos = chunkPos;
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

    public ITextComponent getDisplayName() {
        int power = (int) Math.floor(Math.log10(volume));
        return new TranslationTextComponent("text.interspace.volume." + power);
    }
}
