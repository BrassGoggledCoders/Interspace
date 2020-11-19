package xyz.brassgoggledcoders.interspace.task.interspace;

import xyz.brassgoggledcoders.interspace.api.source.Source;
import xyz.brassgoggledcoders.interspace.api.source.SourceType;
import xyz.brassgoggledcoders.interspace.api.source.WorldSource;
import xyz.brassgoggledcoders.interspace.api.task.TaskType;
import xyz.brassgoggledcoders.interspace.api.task.interspace.IInterspaceTaskRunner;
import xyz.brassgoggledcoders.interspace.api.task.interspace.InterspaceTask;
import xyz.brassgoggledcoders.interspace.content.InterspaceTaskTypes;
import xyz.brassgoggledcoders.interspace.sql.SQLStatements;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class SetupInterspaceTask extends InterspaceTask {
    private Future<Boolean> setupTransaction = null;

    public SetupInterspaceTask(WorldSource source) {
        super(InterspaceTaskTypes.SETUP_INTERSPACE.get(), source);
    }

    public SetupInterspaceTask(TaskType type, Source<?> source) {
        super(type, source);
    }

    @Override
    public void run(IInterspaceTaskRunner taskRunner) {
        final String name = this.getName();
        if (name != null) {
            this.setupTransaction = taskRunner.getSQLClient().inTransaction(sqlClient -> {
                sqlClient.blockingCall(String.format(SQLStatements.ITEM_TABLE_SQL, name));
                sqlClient.blockingCall(String.format(SQLStatements.TRANSACTION_TABLE_SQL, name));
                sqlClient.blockingCall(String.format(SQLStatements.TRANSACTION_TRIGGER_SQL, name));
                sqlClient.blockingCall(String.format(SQLStatements.ITEM_CHECK_INVENTORY_TRIGGER, name));
                return true;
            });
        } else {
            this.setupTransaction = CompletableFuture.completedFuture(false);
        }

    }

    private String getName() {
        if (this.getSource() instanceof WorldSource) {
            WorldSource worldSource = (WorldSource) this.getSource();
            if (worldSource.getRegistryKey() != null) {
                return worldSource.getRegistryKey().getLocation().toString();
            }
        }
        return null;
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
}
