package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.CollapseReal;
import net.imglib2.RandomAccessible;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import net.imglib2.view.composite.CompositeView;
import net.imglib2.view.composite.RealComposite;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.CollapseReal.class, name = ViewOps.CollapseReal.NAME)
public class DefaultRealCollapse2CompositeView<T extends RealType<T>> extends
		AbstractView<RandomAccessible<T>, CompositeView<T, RealComposite<T>>>
		implements
		CollapseReal<RandomAccessible<T>, CompositeView<T, RealComposite<T>>> {

	@Parameter(type = ItemIO.INPUT)
	private int numChannels;

	@Override
	public CompositeView<T, RealComposite<T>> compute(RandomAccessible<T> input) {
		return Views.collapseReal(input, numChannels);
	}

}
