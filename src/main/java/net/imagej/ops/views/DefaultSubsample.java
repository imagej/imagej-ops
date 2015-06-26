package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.Subsample;
import net.imglib2.RandomAccessible;
import net.imglib2.view.SubsampleView;
import net.imglib2.view.Views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.Subsample.class, name = ViewOps.Subsample.NAME)
public class DefaultSubsample<T> extends
		AbstractView<RandomAccessible<T>, SubsampleView<T>> implements
		Subsample<RandomAccessible<T>, SubsampleView<T>> {

	@Parameter(type = ItemIO.INPUT)
	private long step;

	@Override
	public SubsampleView<T> compute(RandomAccessible<T> input) {
		return Views.subsample(input, step);
	}

}
