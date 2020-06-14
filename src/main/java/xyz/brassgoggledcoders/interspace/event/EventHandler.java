package xyz.brassgoggledcoders.interspace.event;

import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spacial.capability.IInterspaceWorld;
import xyz.brassgoggledcoders.interspace.spacial.InterspaceClient;
import xyz.brassgoggledcoders.interspace.spacial.capability.InterspaceChunk;
import xyz.brassgoggledcoders.interspace.spacial.capability.InterspaceProvider;
import xyz.brassgoggledcoders.interspace.spacial.capability.InterspaceWorld;

import java.sql.SQLException;

@EventBusSubscriber(modid = Interspace.ID)
public class EventHandler {
    @SubscribeEvent
    public static void worldCapability(AttachCapabilitiesEvent<World> worldAttachCapabilitiesEvent) {
        worldAttachCapabilitiesEvent.addCapability(Interspace.rl("interspace"),
                new InterspaceProvider<>(InterspaceAPI.INTERSPACE_WORLD, LazyOptional.of(() ->
                        new InterspaceWorld(worldAttachCapabilitiesEvent.getObject()))));
    }

    @SubscribeEvent
    public static void chunkCapability(AttachCapabilitiesEvent<Chunk> chunkAttachCapabilitiesEvent) {
        chunkAttachCapabilitiesEvent.addCapability(Interspace.rl("interspace"),
                new InterspaceProvider<>(InterspaceAPI.INTERSPACE_CHUNK, LazyOptional.of(() ->
                        new InterspaceChunk(chunkAttachCapabilitiesEvent.getObject().getWorldForge(),
                                chunkAttachCapabilitiesEvent.getObject().getPos()))));
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent worldTickEvent) {
        worldTickEvent.world.getCapability(InterspaceAPI.INTERSPACE_WORLD)
                .ifPresent(IInterspaceWorld::tick);
    }

    @SubscribeEvent
    public static void chunkUnload(ChunkEvent.Unload chunkEvent) {
        IWorld world = chunkEvent.getChunk().getWorldForge();
        if (world instanceof ICapabilityProvider) {
            ((ICapabilityProvider) world).getCapability(InterspaceAPI.INTERSPACE_WORLD)
                    .ifPresent(interspace -> interspace.onChunkUnload(chunkEvent.getChunk()));
        }
    }

    @SubscribeEvent
    public static void chunkLoad(ChunkEvent.Load chunkEvent) {
        IWorld world = chunkEvent.getChunk().getWorldForge();
        if (world instanceof ICapabilityProvider) {
            ((ICapabilityProvider) world).getCapability(InterspaceAPI.INTERSPACE_WORLD)
                    .ifPresent(interspace -> interspace.onChunkLoad(chunkEvent.getChunk()));
        }
    }

    @SubscribeEvent
    public static void handleServerStart(FMLServerAboutToStartEvent event) {
        Interspace.LOGGER.info("Creating Interspace Database for: " + event.getServer().getWorldName());
        if (Interspace.interspaceClient != null) {
            try {
                Interspace.interspaceClient.close();
            } catch (Exception e) {
                Interspace.LOGGER.error("Failed to Close Database Connection", e);
            }
        }

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException classNotFoundException) {
            Interspace.LOGGER.error("Failed to Load Database Connector", classNotFoundException);
            throw new IllegalStateException("Failed to Load Database Connector", classNotFoundException);
        }

        SQLiteConnectionPoolDataSource dataSource = new SQLiteConnectionPoolDataSource();
        dataSource.setUrl("jdbc:sqlite:" + event.getServer().getActiveAnvilConverter().getSavesDir()
                .resolve(event.getServer().getFolderName()).resolve("interspace.db").toString());

        try {
            Interspace.interspaceClient = new InterspaceClient(dataSource.getConnection());
        } catch (SQLException sqlException) {
            Interspace.LOGGER.error("Failed to Create Database Connection", sqlException);
            throw new IllegalStateException("Failed to Create Database Connection", sqlException);
        }
    }

    @SubscribeEvent
    public static void handleServerStop(FMLServerStoppingEvent event) {
        Interspace.LOGGER.info("Shutting down Interspace Database for: " + event.getServer().getWorldName());

        try {
            Interspace.interspaceClient.close();
            Interspace.interspaceClient = null;
        } catch (Exception e) {
            Interspace.LOGGER.error("Failed to Close Database Connection", e);
        }
    }
}
