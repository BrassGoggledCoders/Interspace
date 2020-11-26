package xyz.brassgoggledcoders.interspace.api.task.interspace;

import xyz.brassgoggledcoders.interspace.api.interspace.IInterspaceClient;
import xyz.brassgoggledcoders.interspace.api.sql.ISQLClient;
import xyz.brassgoggledcoders.interspace.api.task.ITaskRunner;

import javax.annotation.Nonnull;

public interface IInterspaceTaskRunner extends ITaskRunner {
    @Nonnull
    ISQLClient getSQLClient();

    @Nonnull
    IInterspaceClient getInterspaceClient();
}
