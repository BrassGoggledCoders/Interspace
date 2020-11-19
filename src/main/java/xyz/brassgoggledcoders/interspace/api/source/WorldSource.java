package xyz.brassgoggledcoders.interspace.api.source;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import xyz.brassgoggledcoders.interspace.content.InterspaceSourceTypes;

import javax.annotation.Nullable;

public class WorldSource extends Source<World> {
    private RegistryKey<World> registryKey;

    public WorldSource(SourceType sourceType) {
        super(sourceType);
    }

    public WorldSource(RegistryKey<World> registryKey) {
        super(InterspaceSourceTypes.WORLD.get());
        this.registryKey = registryKey;
    }

    @Nullable
    public RegistryKey<World> getRegistryKey() {
        return registryKey;
    }

    @Override
    public World locate(ServerWorld serverWorld) {
        return serverWorld.getServer().getWorld(registryKey);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        if (this.getRegistryKey() != null) {
            nbt.putString("registryKey", this.getRegistryKey().getLocation().toString());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.registryKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(nbt.getString("registryKey")));
    }
}
