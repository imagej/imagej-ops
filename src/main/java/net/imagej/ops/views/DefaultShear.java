package net.imagej.ops.views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessible;
import net.imglib2.view.TransformView;

@Plugin(type = Ops.View.Shear.class, name = Ops.View.Shear.NAME)
public class DefaultShear<T> extends AbstractFunctionOp<RandomAccessible<T>, TransformView<T>>
		implements Ops.View.Shear {

	@Parameter(type = ItemIO.INPUT)
	private int shearDimension;

	@Parameter(type = ItemIO.INPUT)
	private int referenceDimension;

	@Override
	public TransformView<T> compute(RandomAccessible<T> input) {
		throw new UnsupportedOperationException();
	}

}
