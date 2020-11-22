package xyz.brassgoggledcoders.interspace.event;

import com.google.common.collect.Sets;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
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
import xyz.brassgoggledcoders.interspace.capability.SingleCapabilityProvider;
import xyz.brassgoggledcoders.interspace.mail.MailboxStorage;
import xyz.brassgoggledcoders.interspace.manager.InterspaceManager;
import xyz.brassgoggledcoders.interspace.manager.InterspacePostOffice;
import xyz.brassgoggledcoders.interspace.task.interspace.SetupInterspaceTask;

import java.util.Set;

@EventBusSubscriber(modid = InterspaceMod.ID, bus = Bus.FORGE)
public class ForgeEventHandler {
    private static final Set<RegistryKey<World>> HANDLED = Sets.newHashSet();

    private static final ResourceLocation MAILBOXES = InterspaceMod.rl("mailboxes");

    private static InterspacePostOffice interspacePost;
    private static InterspaceManager interspaceManager;

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent commandEvent) {
        commandEvent.getDispatcher().register(InterspaceCommand.create());
    }

    @SubscribeEvent
    public static void worldCapabilities(AttachCapabilitiesEvent<World> attachCapabilitiesEvent) {
        SingleCapabilityProvider<IMailBoxStorage> mailBoxProvider = new SingleCapabilityProvider<>(
                InterspaceCapabilities.MAILBOXES, new MailboxStorage()
        );
        attachCapabilitiesEvent.addCapability(MAILBOXES, mailBoxProvider);
        attachCapabilitiesEvent.addListener(mailBoxProvider::invalidate);
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load worldLoadEvent) {
        if (!worldLoadEvent.getWorld().isRemote() && worldLoadEvent.getWorld() instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) worldLoadEvent.getWorld();
            if (HANDLED.add(world.getDimensionKey())) {
                InterspaceAPI.getManager().submitTask(SetupInterspaceTask.create(world.getDimensionKey()));
            }
        }
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload worldEvent) {
        if (interspacePost != null && worldEvent.getWorld() instanceof World) {
            interspacePost.removeStorage(((World) worldEvent.getWorld()).getDimensionKey().getLocation());
        }
    }

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event) {
        if (interspaceManager != null) {
            interspaceManager.start();
        }
    }

    @SubscribeEvent
    public static void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        close();
        interspaceManager = InterspaceManager.create(event.getServer());
        InterspaceAPI.setManager(interspaceManager);
        interspacePost = new InterspacePostOffice(event.getServer());
        InterspaceAPI.setPost(interspacePost);
    }

    @SubscribeEvent
    public static void onServerStopped(FMLServerStoppingEvent event) {
        close();
    }

    private static void close() {
        HANDLED.clear();

        if (interspaceManager != null) {
            interspaceManager.close();
            interspaceManager = null;
            InterspaceAPI.setManager(null);
        }
        if (interspacePost != null) {
            interspacePost = null;
            InterspaceAPI.setPost(null);
        }
    }
}
