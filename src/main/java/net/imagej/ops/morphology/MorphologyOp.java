package net.imagej.ops.morphology;

import net.imagej.ops.special.hybrid.UnaryHybridCF;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Shape;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich (University of Konstanz)
 */
public interface MorphologyOp<T> extends UnaryHybridCF<RandomAccessibleInterval<T>, IterableInterval<T>> {

	/**
	 * 
	 * @return
	 */
	public Shape getShape();
	
	/**
	 * 
	 */
	public void setShape(Shape shape);

}
