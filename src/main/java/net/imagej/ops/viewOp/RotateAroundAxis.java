package net.imagej.ops.viewOp;

import net.imagej.ops.Op;
import net.imagej.ops.ViewOps.View;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.Type;
import net.imglib2.view.Views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = View.NAME)
public class RotateAroundAxis<I extends Type<I>> extends AbstractDefaultView<I>
		implements
		Rotate<RandomAccessibleInterval<I>, RandomAccessibleInterval<?>> {

	@Parameter(type = ItemIO.INPUT)
	private int fromAxis;

	@Parameter(type = ItemIO.INPUT)
	private int toAxis;

	@Override
	public RandomAccessibleInterval<?> compute(RandomAccessibleInterval<I> input) {
		return Views.rotate(input, fromAxis, toAxis);
	}

}
