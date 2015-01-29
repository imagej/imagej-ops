package net.imagej.ops.features;

import java.util.Set;

import net.imagej.ops.Op;
import net.imagej.ops.OpRef;

import org.scijava.service.Service;

public interface OpResolverService extends Service {

	<I> ResolvedOpSet<I> build(final I input, final Set<OpRef<?>> opPool);

	<I, OP extends Op> ResolvedOpSet<I> build(final I input, final OpRef<OP> ref);

	<I, OP extends Op> ResolvedOpSet<I> build(final I input,
			final Class<OP> type, final Object... args);

	<I, OP extends Op> ResolvedOpSet<I> build(final I input, OpRef<?> ... refs);

}
