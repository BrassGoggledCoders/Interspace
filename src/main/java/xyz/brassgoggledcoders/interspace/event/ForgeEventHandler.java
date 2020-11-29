package xyz.brassgoggledcoders.interspace.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import xyz.brassgoggledcoders.interspace.InterspaceCommand;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.InterspaceCapabilities;
import xyz.brassgoggledcoders.interspace.api.mail.IMailBoxStorage;
import xyz.brassgoggledcoders.interspace.capability.MailboxStorage;
import xyz.brassgoggledcoders.interspace.capability.SingleCapabilityProvider;
import xyz.brassgoggledcoders.interspace.content.InterspaceFeatures;
import xyz.brassgoggledcoders.interspace.interspace.InterspaceManager;
import xyz.brassgoggledcoders.interspace.interspace.InterspacePostOffice;
import xyz.brassgoggledcoders.interspace.interspace.InterspaceVolumeManager;
import xyz.brassgoggledcoders.interspace.task.interspace.SetupChunkInterspaceTask;
import xyz.brassgoggledcoders.interspace.task.interspace.SetupWorldInterspaceTask;

@EventBusSubscriber(modid = InterspaceMod.ID, bus = Bus.FORGE)
public class ForgeEventHandler {
    private static final ResourceLocation MAILBOXES = InterspaceMod.rl("mailboxes");

    private static InterspacePostOffice interspacePost;
    private static InterspaceManager interspaceManager;

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent commandEvent) {
        commandEvent.getDispatcher().register(InterspaceCommand.create());
    }

    @SubscribeEvent
    public static void registerJsonListeners(AddReloadListenerEvent event) {
        InterspaceVolumeManager interspaceVolumeManager = new InterspaceVolumeManager();
        event.addListener(interspaceVolumeManager);
        InterspaceAPI.setVolumeManager(interspaceVolumeManager);
    }

    @SubscribeEvent
    public static void playerLoadFromFile(PlayerEvent.LoadFromFile loadFromFileEvent) {
        InterspaceAPI.getPostOffice().createMailBox(World.OVERWORLD, loadFromFileEvent.getPlayer().getUniqueID());
    }

    @SubscribeEvent
    public static void playerEvent(TickEvent.PlayerTickEvent playerTickEvent) {
        PlayerEntity playerEntity = playerTickEvent.player;
        if (playerTickEvent.phase == TickEvent.Phase.START && playerEntity.getEntityWorld().getGameTime() % 20 == 0) {
            InterspaceAPI.getPostOffice().receiveMail(World.OVERWORLD, playerEntity.getUniqueID(), 5)
                    .forEach(mail -> mail.receive(playerEntity));
        }
    }

    @SubscribeEvent
    public static void worldCapabilities(AttachCapabilitiesEvent<World> attachCapabilitiesEvent) {
        SingleCapabilityProvider<IMailBoxStorage, ListNBT> mailBoxProvider = new SingleCapabilityProvider<>(
                InterspaceCapabilities.MAILBOXES, new MailboxStorage()
        );
        attachCapabilitiesEvent.addCapability(MAILBOXES, mailBoxProvider);
        attachCapabilitiesEvent.addListener(mailBoxProvider::invalidate);
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load worldLoadEvent) {
        IWorld world = worldLoadEvent.getWorld();
        if (!world.isRemote() && world instanceof ServerWorld) {
            SetupWorldInterspaceTask.submit(((ServerWorld) world).getDimensionKey());
        }
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload worldEvent) {
        if (interspacePost != null && worldEvent.getWorld() instanceof World) {
            interspacePost.removeStorage(((World) worldEvent.getWorld()).getDimensionKey().getLocation());
        }
    }

    @SubscribeEvent
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        InterspaceFeatures.onBiomeLoad(event);
    }

    @SubscribeEvent
    public static void onChunkDataLoad(ChunkDataEvent.Load loadDataEvent) {
        IWorld world = loadDataEvent.getWorld();
        IChunk chunk = loadDataEvent.getChunk();
        if (chunk.getStatus() == ChunkStatus.FULL && world instanceof ServerWorld) {
            if (!loadDataEvent.getData().getBoolean(InterspaceMod.ID)) {
                SetupChunkInterspaceTask.submit(((ServerWorld) world).getDimensionKey(), world.getRandom(),
                        chunk.getPos());
            }
        }
    }

    @SubscribeEvent
    public static void onChunkDataSave(ChunkDataEvent.Save saveDataEvent) {
        if (saveDataEvent.getChunk().getStatus() == ChunkStatus.FULL) {
            saveDataEvent.getData().putBoolean(InterspaceMod.ID, true);
        }
    }

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event) {
        if (interspaceManager != null) {
            interspaceManager.start();
            event.getServer().getWorlds().forEach(world -> SetupWorldInterspaceTask.submit(world.getDimensionKey()));
        }
    }

    @SubscribeEvent
    public static void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        close();
        interspaceManager = InterspaceManager.create(event.getServer());
        InterspaceAPI.setManager(interspaceManager);
        interspacePost = new InterspacePostOffice(event.getServer());
        InterspaceAPI.setPostOffice(interspacePost);
    }

    @SubscribeEvent
    public static void onServerStopped(FMLServerStoppingEvent event) {
        close();
    }

    private static void close() {
        SetupWorldInterspaceTask.clearSubmitted();

        if (interspaceManager != null) {
            interspaceManager.close();
            interspaceManager = null;
            InterspaceAPI.setManager(null);
        }
        if (interspacePost != null) {
            interspacePost = null;
            InterspaceAPI.setPostOffice(null);
        }
    }
}
