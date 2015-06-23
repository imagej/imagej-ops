package net.imagej.ops.views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

import net.imagej.ops.OpService;
import net.imagej.ops.view.ViewOps.Offset;
import net.imagej.ops.view.ViewOps.View;
import net.imglib2.RandomAccessible;
import net.imglib2.view.IntervalView;

public class OffsetOriginSize<T> extends
		AbstractView<RandomAccessible<T>, IntervalView<T>> implements
		Offset<RandomAccessible<T>, IntervalView<T>> {

	@Parameter(type = ItemIO.INPUT)
	private long[] origin;
	
	@Parameter(type = ItemIO.INPUT)
	private long[] dimension;
	
	@Parameter
	private OpService ops;
	
	@Override
	public IntervalView<T> compute(RandomAccessible<T> input) {
		final int n = input.numDimensions();
		final long[] min = new long[ n ];
		final long[] max = new long[ n ];
		for ( int d = 0; d < n; ++d )
			max[ d ] = dimension[ d ] - 1;
		return (IntervalView<T>) ops.run(View.class, ops.run(Offset.class, origin), min, max);
	}

}
