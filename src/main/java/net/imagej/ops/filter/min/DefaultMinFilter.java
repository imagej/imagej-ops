
package net.imagej.ops.filter.min;

import net.imagej.ops.ComputerOp;
import net.imagej.ops.Ops;
import net.imagej.ops.filter.AbstractNonLinearFilter;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of {@link MinFilterOp}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * @param <T> type
 */
@Plugin(type = Ops.Filter.Min.class, name = Ops.Filter.Min.NAME,
	priority = Priority.LOW_PRIORITY)
public class DefaultMinFilter<T extends RealType<T>> extends
	AbstractNonLinearFilter<T, T> implements
	MinFilterOp<RandomAccessibleInterval<T>>
{

	@SuppressWarnings("unchecked")
	@Override
	protected ComputerOp<Iterable<T>, T> getComputer(Class<?> inClass,
		Class<?> outClass)
	{
		return (ComputerOp<Iterable<T>, T>) ops.op(Ops.Stats.Min.class, Iterable.class,
			inClass);
	}

}
