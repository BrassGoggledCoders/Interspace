package xyz.brassgoggledcoders.interspace.event;

import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spacial.IInterspace;
import xyz.brassgoggledcoders.interspace.spacial.capability.InterspaceWorldProvider;

@EventBusSubscriber(modid = Interspace.ID)
public class EventHandler {
    @SubscribeEvent
    public static void worldCapability(AttachCapabilitiesEvent<World> worldAttachCapabilitiesEvent) {
        worldAttachCapabilitiesEvent.addCapability(Interspace.rl("interspace"),
                new InterspaceWorldProvider(worldAttachCapabilitiesEvent.getObject()));
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent worldTickEvent) {
        worldTickEvent.world.getCapability(InterspaceAPI.INTERSPACE)
                .ifPresent(IInterspace::tick);
    }

    @SubscribeEvent
    public static void chunkUnload(ChunkEvent.Unload chunkEvent) {
        IWorld world = chunkEvent.getChunk().getWorldForge();
        if (world instanceof ICapabilityProvider) {
            ((ICapabilityProvider) world).getCapability(InterspaceAPI.INTERSPACE)
                    .ifPresent(interspace -> interspace.onChunkUnload(chunkEvent.getChunk()));
        }
    }

    @SubscribeEvent
    public static void chunkLoad(ChunkEvent.Load chunkEvent) {
        IWorld world = chunkEvent.getChunk().getWorldForge();
        if (world instanceof ICapabilityProvider) {
            ((ICapabilityProvider) world).getCapability(InterspaceAPI.INTERSPACE)
                    .ifPresent(interspace -> interspace.onChunkLoad(chunkEvent.getChunk()));
        }
    }
}
