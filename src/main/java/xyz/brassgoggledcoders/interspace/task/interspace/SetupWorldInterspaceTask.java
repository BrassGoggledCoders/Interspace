package xyz.brassgoggledcoders.interspace.task.interspace;

import com.google.common.collect.Sets;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.interspace.api.InterspaceAPI;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;
import xyz.brassgoggledcoders.interspace.api.task.interspace.IInterspaceTaskRunner;
import xyz.brassgoggledcoders.interspace.api.task.interspace.InterspaceTask;
import xyz.brassgoggledcoders.interspace.content.InterspaceTaskTypes;
import xyz.brassgoggledcoders.interspace.sql.SQLStatements;

import java.util.Set;
import java.util.concurrent.Future;

public class SetupWorldInterspaceTask extends InterspaceTask {
    private static final Set<RegistryKey<World>> SUBMITTED = Sets.newConcurrentHashSet();

    private Future<Boolean> setupTransaction = null;
    private RegistryKey<World> registryKey;

    public SetupWorldInterspaceTask(TaskType type) {
        super(type);
    }

    public void setRegistryKey(RegistryKey<World> registryKey) {
        this.registryKey = registryKey;
    }

    @Override
    public void run(IInterspaceTaskRunner taskRunner) {
        final String name = this.registryKey.getLocation().toString();
        this.setupTransaction = taskRunner.getSQLClient().inTransaction(sqlClient -> {
            sqlClient.blockingCall(String.format(SQLStatements.CHUNK_SQL, name));
            sqlClient.blockingCall(String.format(SQLStatements.CACHE_SQL, name));
            sqlClient.blockingCall(String.format(SQLStatements.ITEM_TABLE_SQL, name));
            sqlClient.blockingCall(String.format(SQLStatements.ITEM_CHECK_INVENTORY_TRIGGER, name));
            return true;
        });
    }

    @Override
    public boolean isBlocking() {
        return true;
    }

    @Override
    public boolean isDone() {
        return setupTransaction != null && setupTransaction.isDone();
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Override
    public int getPriority() {
        return 1000;
    }

    public static SetupWorldInterspaceTask create(RegistryKey<World> registryKey) {
        SetupWorldInterspaceTask setupWorldInterspaceTask = new SetupWorldInterspaceTask(InterspaceTaskTypes.SETUP_WORLD_INTERSPACE.get());
        setupWorldInterspaceTask.setRegistryKey(registryKey);
        return setupWorldInterspaceTask;
    }

    public static void submit(RegistryKey<World> registryKey) {
        if (SUBMITTED.add(registryKey)) {
            InterspaceAPI.getManager().submitTask(SetupWorldInterspaceTask.create(registryKey));
        }
    }

    public static void clearSubmitted() {
        SUBMITTED.clear();
    }

}
