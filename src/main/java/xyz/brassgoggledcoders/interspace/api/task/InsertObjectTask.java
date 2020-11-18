package xyz.brassgoggledcoders.interspace.api.task;

import javax.annotation.Nullable;

public class InsertObjectTask extends Task {
    @Nullable
    @Override
    public Object getResult() {
        return null;
    }

    @Override
    public boolean isDone() {
        return false;
    }
}
