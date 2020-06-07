package xyz.brassgoggledcoders.interspace.api.spacial.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public abstract class SpacialParameter<T> {

    public abstract String toQuery();

    public abstract Collection<T> getValues();

    public abstract void handleValue(PreparedStatement statement, int index, T value) throws SQLException;
}
