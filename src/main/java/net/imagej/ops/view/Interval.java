package net.imagej.ops.view;

import net.imagej.ops.InputOp;
import net.imagej.ops.Op;
import net.imagej.ops.ViewOps.View;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.Type;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = View.NAME)
public class Interval<I extends Type<I>> implements InputOp<RandomAccessibleInterval<I>>, DefaultView<IntervalView<I>> {

	@Parameter(type = ItemIO.OUTPUT)
	private IntervalView<I> output;
	
	@Parameter(type = ItemIO.INPUT)
	private RandomAccessibleInterval<I> input;
	
	@Override
	public void run() {
		output = Views.interval(input, input);
	}

	@Override
	public IntervalView<I> getOutput() {
		return output;
	}

	@Override
	public void setOutput(IntervalView<I> output) {
		this.output = output;		
	}

	@Override
	public RandomAccessibleInterval<I> getInput() {
		return input;
	}

	@Override
	public void setInput(RandomAccessibleInterval<I> input) {
		this.input = input;
	}

}
