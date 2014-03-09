package imagej.ops.slicer;

import net.imglib2.RandomAccessibleInterval;
import imagej.ops.Function;

/**
 * Interface for mappers which perform a slice-wise mapping
 * 
 * @author Christian Dietz
 * 
 * @param <I>
 * @param <O>
 */
public interface SliceMapper<I, O> extends
		Function<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>> {
	// NB: Marker interface for Slice Mappers
}
