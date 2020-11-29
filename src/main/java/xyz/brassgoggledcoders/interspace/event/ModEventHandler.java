package xyz.brassgoggledcoders.interspace.event;

import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryBuilder;
import xyz.brassgoggledcoders.interspace.InterspaceMod;
import xyz.brassgoggledcoders.interspace.api.mail.IMailBoxStorage;
import xyz.brassgoggledcoders.interspace.api.mail.MailType;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;
import xyz.brassgoggledcoders.interspace.capability.CapabilityNBTStorage;
import xyz.brassgoggledcoders.interspace.capability.MailboxStorage;
import xyz.brassgoggledcoders.interspace.content.InterspaceFeatures;

@EventBusSubscriber(modid = InterspaceMod.ID, bus = Bus.MOD)
public class ModEventHandler {
    @SubscribeEvent
    public static void onFeatureRegistry(RegistryEvent.Register<Feature<?>> event) {
        InterspaceFeatures.registerFeatures();
    }

    @SubscribeEvent
    public static void newRegistry(RegistryEvent.NewRegistry event) {
        new RegistryBuilder<MailType>()
                .setType(MailType.class)
                .setName(InterspaceMod.rl("mail_type"))
                .create();

        new RegistryBuilder<TaskType>()
                .setType(TaskType.class)
                .setName(InterspaceMod.rl("task_type"))
                .create();
    }
}
