package net.imagej.ops.views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessible;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import net.imglib2.view.composite.CompositeView;
import net.imglib2.view.composite.RealComposite;

@Plugin(type = Ops.View.RealCollapse.class, name = Ops.View.RealCollapse.NAME)
public class DefaultCollapseReal2CompositeView<T extends RealType<T>> extends
		AbstractFunctionOp<RandomAccessible<T>, CompositeView<T, RealComposite<T>>>implements Ops.View.RealCollapse {

	@Parameter(type = ItemIO.INPUT)
	private int numChannels;

	@Override
	public CompositeView<T, RealComposite<T>> compute(RandomAccessible<T> input) {
		return Views.collapseReal(input, numChannels);
	}

}
