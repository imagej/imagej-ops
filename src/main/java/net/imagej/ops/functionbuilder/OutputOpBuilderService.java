package net.imagej.ops.functionbuilder;

import java.util.Set;

import net.imagej.ops.OpRef;

import org.scijava.service.Service;

public interface ModuleBuilderService extends Service {

	<I, O> ModuleSet<I> build(final OpRef outOp, final O output, final I input,
			final OpRef... opPool);

	<I, O> ModuleSet<I> build(final Set<OpRef> outOps, final O output,
			final I input, final OpRef... opPool);
}
