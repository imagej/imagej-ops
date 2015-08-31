
package net.imagej.ops.filter.mean;

import net.imagej.ops.ComputerOp;
import net.imagej.ops.Ops;
import net.imagej.ops.filter.AbstractNeighborhoodBasedFilter;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of {@link MeanFilterOp}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * @param <T> type
 */
@Plugin(type = Ops.Filter.Mean.class, name = Ops.Filter.Mean.NAME,
	priority = Priority.LOW_PRIORITY)
public class DefaultMeanFilter<T extends RealType<T>> extends
	AbstractNeighborhoodBasedFilter<T, T> implements
	MeanFilterOp<RandomAccessibleInterval<T>>
{

	@SuppressWarnings("unchecked")
	@Override
	protected ComputerOp<Iterable<T>, T> getComputer(Class<?> inClass,
		Class<?> outClass)
	{
		return (ComputerOp<Iterable<T>, T>) ops.op(Ops.Stats.Mean.class, Iterable.class,
			inClass);
	}

}
