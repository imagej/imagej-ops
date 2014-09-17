package net.imagej.ops.project;

import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;

/**
 * Interface marking all {@link RandomAccessibleInterval} to
 * {@link IterableInterval} Projectors
 * 
 * @author Christian Dietz (University of Konstanz)
 */
public interface ProjectRAI2II<I, O> extends
		Project<RandomAccessibleInterval<I>, IterableInterval<O>> {
	// NB: Marker Interface
}
