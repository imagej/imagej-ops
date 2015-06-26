package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.CollapseNumeric;
import net.imglib2.RandomAccessible;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.view.Views;
import net.imglib2.view.composite.CompositeView;
import net.imglib2.view.composite.NumericComposite;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.CollapseNumeric.class, name = ViewOps.CollapseNumeric.NAME)
public class DefaultCollapseNummeric2CompositeView<T extends NumericType<T>>
		extends
		AbstractView<RandomAccessible<T>, CompositeView<T, NumericComposite<T>>>
		implements
		CollapseNumeric<RandomAccessible<T>, CompositeView<T, NumericComposite<T>>> {

	@Parameter(type = ItemIO.INPUT)
	private int numChannels;

	@Override
	public CompositeView<T, NumericComposite<T>> compute(
			RandomAccessible<T> input) {
		return Views.collapseNumeric(input, numChannels);
	}

}
