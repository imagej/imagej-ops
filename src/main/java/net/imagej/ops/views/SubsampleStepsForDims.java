package net.imagej.ops.views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Contingent;
import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.Subsample;
import net.imglib2.RandomAccessible;
import net.imglib2.view.SubsampleView;
import net.imglib2.view.Views;

@Plugin(type = ViewOps.Subsample.class, name = ViewOps.Subsample.NAME)
public class SubsampleStepsForDims<T> extends AbstractView<RandomAccessible<T>, SubsampleView<T>> implements
		Subsample<RandomAccessible<T>, SubsampleView<T>>, Contingent {

	@Parameter(type = ItemIO.INPUT)
	private long[] steps;
	
	@Override
	public SubsampleView<T> compute(RandomAccessible<T> input) {
		return Views.subsample(input, steps);
	}

	@Override
	public boolean conforms() {
		return getInput().numDimensions() <= steps.length;
	}

}
