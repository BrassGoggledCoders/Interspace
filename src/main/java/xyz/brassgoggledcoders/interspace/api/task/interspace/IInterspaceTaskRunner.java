package xyz.brassgoggledcoders.interspace.api.task.interspace;

import xyz.brassgoggledcoders.interspace.api.sql.ISQLClient;
import xyz.brassgoggledcoders.interspace.api.task.ITaskRunner;

public interface IInterspaceTaskRunner extends ITaskRunner {
    ISQLClient getSQLClient();
}
