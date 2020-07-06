package xyz.brassgoggledcoders.interspace.api.functional;

import xyz.brassgoggledcoders.interspace.api.spatial.capability.ISpatial;
import xyz.brassgoggledcoders.interspace.api.spatial.query.Transaction;

import java.util.function.Function;

public interface ObeliskFunction extends Function<ISpatial, Transaction<?>> {
}
