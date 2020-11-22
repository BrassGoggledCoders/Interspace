package xyz.brassgoggledcoders.interspace.interspace;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import xyz.brassgoggledcoders.interspace.api.InterspaceCapabilities;
import xyz.brassgoggledcoders.interspace.api.interspace.IInterspacePostOffice;
import xyz.brassgoggledcoders.interspace.api.mail.IMailBoxStorage;
import xyz.brassgoggledcoders.interspace.api.mail.Mail;
import xyz.brassgoggledcoders.interspace.api.mail.MailBoxStatus;
import xyz.brassgoggledcoders.interspace.capability.WeakNonNullConsumer;

import javax.annotation.Nonnull;
import java.util.*;

public class InterspacePostOffice implements IInterspacePostOffice {
    private final MinecraftServer minecraftServer;
    private final Map<ResourceLocation, LazyOptional<IMailBoxStorage>> mailboxStorage;

    public InterspacePostOffice(MinecraftServer minecraftServer) {
        this.minecraftServer = minecraftServer;
        this.mailboxStorage = Maps.newHashMap();
    }

    @Override
    public boolean sendMail(@Nonnull UUID address, @Nonnull Mail mail) {
        return this.getMailBoxStorage(World.OVERWORLD)
                .map(storage -> storage.sendMail(address, mail))
                .orElse(false);
    }

    @Override
    public boolean sendMail(@Nonnull RegistryKey<World> world, @Nonnull UUID address, @Nonnull Mail mail) {
        return this.getMailBoxStorage(world)
                .map(storage -> storage.sendMail(address, mail))
                .orElse(false);
    }

    @Nonnull
    @Override
    public Collection<Mail> receiveMail(@Nonnull RegistryKey<World> world, @Nonnull UUID address, int maxReceive) {
        return this.getMailBoxStorage(world)
                .map(storage -> storage.getMail(address, maxReceive))
                .orElseGet(Queues::newArrayDeque);
    }

    @Override
    public MailBoxStatus createMailBox(@Nonnull RegistryKey<World> world, @Nonnull UUID address) {
        return this.getMailBoxStorage(world)
                .map(storage -> storage.createMailBox(address))
                .orElse(MailBoxStatus.FAILED);
    }

    private LazyOptional<IMailBoxStorage> getMailBoxStorage(@Nonnull RegistryKey<World> world) {
        LazyOptional<IMailBoxStorage> storage = mailboxStorage.get(world.getLocation());
        if (storage == null) {
            ServerWorld serverWorld = minecraftServer.getWorld(world);
            if (serverWorld != null) {
                storage = serverWorld.getCapability(InterspaceCapabilities.MAILBOXES);
                mailboxStorage.put(world.getLocation(), storage);
                if (storage.isPresent()) {
                    storage.addListener(this.createInvalidationHandler(world));
                }
            } else {
                storage = LazyOptional.empty();
            }
        }
        return storage;
    }

    private NonNullConsumer<LazyOptional<IMailBoxStorage>> createInvalidationHandler(RegistryKey<World> world) {
        return new WeakNonNullConsumer<>(this, this.removeByKey(world.getLocation()));
    }

    private <T> NonNullBiConsumer<InterspacePostOffice, LazyOptional<T>> removeByKey(ResourceLocation world) {
        return ((interspacePost, tLazyOptional) -> interspacePost.removeStorage(world));
    }

    public void removeStorage(ResourceLocation world) {
        this.mailboxStorage.remove(world);
    }
}
