package xyz.brassgoggledcoders.interspace.api.interspace;

import xyz.brassgoggledcoders.interspace.api.task.interspace.InterspaceTask;
import xyz.brassgoggledcoders.interspace.api.task.world.WorldTask;

public interface IInterspaceManager {
    boolean submitTask(InterspaceTask task);

    boolean submitTask(WorldTask task);
}
