package xyz.brassgoggledcoders.interspace.sql;

import net.minecraft.world.storage.FolderName;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;
import xyz.brassgoggledcoders.interspace.InterspaceMod;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

@EventBusSubscriber(modid = InterspaceMod.ID, bus = Bus.FORGE)
public class ConnectionHandler {
    private static final FolderName FOLDER = new FolderName("interspace");
    private static Connection connection;

    @Nullable
    public static Connection getConnection() {
        return connection;
    }

    @SubscribeEvent
    public static void serverStarting(FMLServerAboutToStartEvent event) {
        closeConnection();
        Path interspaceFolder = event.getServer().func_240776_a_(FOLDER);

        try {
            Files.createDirectories(interspaceFolder);
            Class.forName("org.sqlite.JDBC");

            SQLiteConnectionPoolDataSource dataSource = new SQLiteConnectionPoolDataSource();
            dataSource.setUrl("jdbc:sqlite:" + interspaceFolder.resolve("main.db").toString());
            connection = dataSource.getConnection();
        } catch (IOException | ClassNotFoundException | SQLException exception) {
            InterspaceMod.LOGGER.fatal("Failed to Create Connection", exception);
        }
    }

    @SubscribeEvent
    public static void serverStopping(FMLServerStoppingEvent event) {
        closeConnection();
    }

    private static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException exception) {
                InterspaceMod.LOGGER.warn("Failed to Close Connection", exception);
            }
        }
    }
}
