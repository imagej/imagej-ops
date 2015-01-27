package net.imagej.ops.functionbuilder;

import java.util.Set;

import net.imagej.ops.OpRef;

import org.scijava.service.Service;

public interface OutputOpBuilderService extends Service {

	<I, O> UpdatableOutputOpSet<I, O> build(final OutputOpRef<O> outOp, final O output,
			final I input, final OpRef... opPool);

	<I, O> UpdatableOutputOpSet<I, O> build(final Set<OutputOpRef<O>> outOps, final O output,
			final I input, final OpRef... opPool);
}
