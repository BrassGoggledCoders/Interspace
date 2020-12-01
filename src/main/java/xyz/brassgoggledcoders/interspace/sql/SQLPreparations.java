package xyz.brassgoggledcoders.interspace.sql;

import xyz.brassgoggledcoders.interspace.api.functional.ThrowingBiConsumer;
import xyz.brassgoggledcoders.interspace.api.interspace.InterspaceCache;
import xyz.brassgoggledcoders.interspace.api.interspace.InterspaceVolume;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.BiFunction;

public class SQLPreparations {
    public static final BiFunction<Long, InterspaceVolume, ThrowingBiConsumer<PreparedStatement, InterspaceCache,
            SQLException>> CACHE_PREPARATION = (chunkId, volume) -> (preparedStatement, interspaceCache) -> {
        preparedStatement.setLong(1, chunkId);
        preparedStatement.setString(2, interspaceCache.getName().toString());
        preparedStatement.setFloat(3, Math.max(volume.getCacheLuck(), interspaceCache.getLuck()));
    };

}
