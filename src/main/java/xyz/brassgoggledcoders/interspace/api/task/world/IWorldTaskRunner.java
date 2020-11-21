package xyz.brassgoggledcoders.interspace.api.task.world;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import xyz.brassgoggledcoders.interspace.api.task.ITaskRunner;

public interface IWorldTaskRunner extends ITaskRunner {
    MinecraftServer getMinecraftServer();
}
