
package net.imagej.ops.morphology;

import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Shape;

import org.scijava.plugin.Parameter;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich (University of Konstanz)
 * @param <T>
 */
public abstract class AbstractMorphologyOp<T> extends
	AbstractUnaryHybridCF<RandomAccessibleInterval<T>, IterableInterval<T>>
	implements MorphologyOp<T>
{

	@Parameter
	private Shape shape;

	@Override
	public Shape getShape() {
		return shape;
	}

	@Override
	public void setShape(final Shape shape) {
		this.shape = shape;
	}
}
