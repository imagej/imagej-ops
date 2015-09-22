
package net.imagej.ops.geometric;

import net.imagej.ops.AbstractHybridOp;
import net.imagej.ops.Ops.Geometric2D;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Abstract class for {@link Geometric2D} Features.
 * 
 * @author Daniel Seebacher, University of Konstanz
 */
public abstract class AbstractGeometricFeature<I, O> extends
	AbstractHybridOp<I, O> implements GeometricOp<I, O>
{

	@SuppressWarnings("unchecked")
	@Override
	public O createOutput(I in) {
		return (O) new DoubleType();
	}
}
