package xyz.brassgoggledcoders.interspace.sql;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import xyz.brassgoggledcoders.interspace.api.functional.ThrowingFunction;
import xyz.brassgoggledcoders.interspace.api.interspace.Interspace;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

public class SQLResultSetTransformers {

    public static Function<ResourceLocation, ThrowingFunction<ResultSet, Interspace, SQLException>> INTERSPACE =
            world ->
                    resultSet -> new Interspace(
                            resultSet.getLong(1),
                            world,
                            resultSet.getInt(2),
                            new ChunkPos(
                                    resultSet.getInt(3),
                                    resultSet.getInt(4)
                            )
                    );
}
