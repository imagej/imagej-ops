
package net.imagej.ops.filter.variance;

import net.imagej.ops.ComputerOp;
import net.imagej.ops.Ops;
import net.imagej.ops.filter.AbstractNeighborhoodBasedFilter;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of {@link VarianceFilterOp}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * @param <T> type
 */
@Plugin(type = Ops.Filter.Variance.class, name = Ops.Filter.Variance.NAME,
	priority = Priority.LOW_PRIORITY)
public class DefaultVarianceFilter<T extends RealType<T>> extends
	AbstractNeighborhoodBasedFilter<T, T> implements
	VarianceFilterOp<RandomAccessibleInterval<T>>
{

	@SuppressWarnings("unchecked")
	@Override
	protected ComputerOp<Iterable<T>, T> getComputer(Class<?> inClass,
		Class<?> outClass)
	{
		return (ComputerOp<Iterable<T>, T>) ops.op(Ops.Stats.Variance.class, outClass,
			Iterable.class);
	}

}
