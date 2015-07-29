package net.imagej.ops.views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessible;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.view.Views;
import net.imglib2.view.composite.CompositeView;
import net.imglib2.view.composite.NumericComposite;

@Plugin(type = Ops.View.NumericCollapse.class, name = Ops.View.NumericCollapse.NAME)
public class DefaultCollapseNumeric2CompositeView<T extends NumericType<T>> extends
		AbstractFunctionOp<RandomAccessible<T>, CompositeView<T, NumericComposite<T>>>implements Ops.View.NumericCollapse {

	@Parameter(type = ItemIO.INPUT)
	private int numChannels;

	@Override
	public CompositeView<T, NumericComposite<T>> compute(RandomAccessible<T> input) {
		return Views.collapseNumeric(input, numChannels);
	}
}
