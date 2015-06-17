package net.imagej.ops.viewOp;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.View;
import net.imglib2.Interval;
import net.imglib2.RandomAccessible;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.View.class, name = ViewOps.View.NAME)
public class DefaultView<T>
		extends
		AbstractView<RandomAccessible< T >, IntervalView< T >>
		implements
		View<RandomAccessible< T >, IntervalView< T >> {

	@Parameter(type = ItemIO.INPUT)
	private Interval interval;
	
	@Override
	public IntervalView< T > compute(RandomAccessible< T > input) {
		return Views.interval(input, interval);
	}

}
