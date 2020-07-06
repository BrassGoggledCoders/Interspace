package xyz.brassgoggledcoders.interspace.event;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;
import xyz.brassgoggledcoders.interspace.Interspace;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatialWorld;
import xyz.brassgoggledcoders.interspace.capability.SpatialItemHandler;
import xyz.brassgoggledcoders.interspace.spatial.SpatialClient;
import xyz.brassgoggledcoders.interspace.spatial.capability.SpatialChunk;
import xyz.brassgoggledcoders.interspace.spatial.capability.SpatialProvider;
import xyz.brassgoggledcoders.interspace.spatial.capability.SpatialWorld;

import java.sql.SQLException;

@EventBusSubscriber(modid = Interspace.ID)
public class EventHandler {
    @SubscribeEvent
    public static void worldCapability(AttachCapabilitiesEvent<World> worldAttachCapabilitiesEvent) {
        worldAttachCapabilitiesEvent.addCapability(Interspace.rl("interspace"),
                new SpatialProvider<>(InterspaceAPI.SPATIAL_WORLD, LazyOptional.of(() ->
                        new SpatialWorld(worldAttachCapabilitiesEvent.getObject()))));
    }

    @SubscribeEvent
    public static void chunkCapability(AttachCapabilitiesEvent<Chunk> chunkAttachCapabilitiesEvent) {
        chunkAttachCapabilitiesEvent.addCapability(Interspace.rl("interspace"),
                new SpatialProvider<>(InterspaceAPI.SPATIAL_CHUNK, LazyOptional.of(() ->
                        new SpatialChunk(chunkAttachCapabilitiesEvent.getObject().getWorld(),
                                chunkAttachCapabilitiesEvent.getObject().getPos()))));
    }

    @SubscribeEvent
    public static void tileCapability(AttachCapabilitiesEvent<TileEntity> tileEntityAttachCapabilitiesEvent) {
        tileEntityAttachCapabilitiesEvent.addCapability(Interspace.rl("obelisk_item"),
                new SpatialProvider<>(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                        LazyOptional.of(() -> new SpatialItemHandler<>(5))));
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent worldTickEvent) {
        worldTickEvent.world.getCapability(InterspaceAPI.SPATIAL_WORLD)
                .ifPresent(ISpatialWorld::tick);
    }

    @SubscribeEvent
    public static void chunkUnload(ChunkEvent.Unload chunkEvent) {
        IWorld world = chunkEvent.getChunk().getWorldForge();
        if (world instanceof ICapabilityProvider) {
            ((ICapabilityProvider) world).getCapability(InterspaceAPI.SPATIAL_WORLD)
                    .ifPresent(interspace -> interspace.onChunkUnload(chunkEvent.getChunk()));
        }
    }

    @SubscribeEvent
    public static void chunkLoad(ChunkEvent.Load chunkEvent) {
        IWorld world = chunkEvent.getChunk().getWorldForge();
        if (world instanceof ICapabilityProvider) {
            ((ICapabilityProvider) world).getCapability(InterspaceAPI.SPATIAL_WORLD)
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
            Interspace.interspaceClient = new SpatialClient(dataSource.getConnection());
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
