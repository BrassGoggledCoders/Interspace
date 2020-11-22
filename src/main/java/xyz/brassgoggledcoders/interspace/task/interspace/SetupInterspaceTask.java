package xyz.brassgoggledcoders.interspace.task.interspace;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;
import xyz.brassgoggledcoders.interspace.api.task.interspace.IInterspaceTaskRunner;
import xyz.brassgoggledcoders.interspace.api.task.interspace.InterspaceTask;
import xyz.brassgoggledcoders.interspace.content.InterspaceTaskTypes;
import xyz.brassgoggledcoders.interspace.sql.SQLStatements;

import java.util.concurrent.Future;

public class SetupInterspaceTask extends InterspaceTask {
    private Future<Boolean> setupTransaction = null;
    private RegistryKey<World> registryKey;

    public SetupInterspaceTask(TaskType type) {
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
            sqlClient.blockingCall(String.format(SQLStatements.ITEM_TABLE_SQL, name));
            sqlClient.blockingCall(String.format(SQLStatements.TRANSACTION_TABLE_SQL, name));
            sqlClient.blockingCall(String.format(SQLStatements.TRANSACTION_TRIGGER_SQL, name));
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

    public static SetupInterspaceTask create(RegistryKey<World> registryKey) {
        SetupInterspaceTask setupInterspaceTask = new SetupInterspaceTask(InterspaceTaskTypes.SETUP_INTERSPACE.get());
        setupInterspaceTask.setRegistryKey(registryKey);
        return setupInterspaceTask;
    }
}
