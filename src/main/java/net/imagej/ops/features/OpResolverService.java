package net.imagej.ops.features;

import java.util.Set;

import net.imagej.ops.Op;
import net.imagej.ops.OpRef;
import net.imagej.ops.OutputOp;

import org.scijava.service.Service;

public interface OpResolverService extends Service {

	<I> ResolvedOpSet<I> resolve(final I input, final Set<OpRef<?>> opPool);

	<I, OP extends Op> ResolvedOpSet<I> resolve(final I input, final OpRef<OP> ref);

	<I, OP extends Op> ResolvedOpSet<I> resolve(final I input,
			final Class<OP> type, final Object... args);

	<I> ResolvedOpSet<I> resolve(final I input, OpRef<?>... refs);

	<I, O> ResolvedOp<I, O> resolve(final Class<O> outType,
			final I input,
			@SuppressWarnings("rawtypes") final Class<? extends OutputOp> type,
			final Object... args);

	<I, O> ResolvedOp<I, O> resolve(final Class<O> outType, final I input,
			@SuppressWarnings("rawtypes") final OpRef<? extends OutputOp> ref);
}
