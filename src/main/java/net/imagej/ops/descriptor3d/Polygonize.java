package net.imagej.ops.descriptor3d;

import net.imagej.ops.FunctionOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.Type;

public interface Polygonize<I extends Type<I>, O extends Facets> extends
		FunctionOp<RandomAccessibleInterval<I>, O> {
	// NB: Marker Interface
}