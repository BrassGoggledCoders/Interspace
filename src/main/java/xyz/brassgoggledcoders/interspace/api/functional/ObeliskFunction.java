package xyz.brassgoggledcoders.interspace.api.functional;

import net.minecraftforge.common.util.NonNullFunction;
import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatial;
import xyz.brassgoggledcoders.interspace.api.spatial.query.Transaction;

public interface ObeliskFunction extends NonNullFunction<ISpatial, Transaction<?>> {
}
