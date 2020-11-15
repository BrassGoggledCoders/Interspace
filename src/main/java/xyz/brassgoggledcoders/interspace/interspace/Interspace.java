package xyz.brassgoggledcoders.interspace.interspace;

import xyz.brassgoggledcoders.interspace.api.interspace.IInterspace;
import xyz.brassgoggledcoders.interspace.api.task.Task;

public class Interspace implements IInterspace {
    @Override
    public Task insert() {
        return new Task() {
        };
    }
}
