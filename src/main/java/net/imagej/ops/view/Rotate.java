package net.imagej.ops.view;

import net.imagej.ops.InputOp;
import net.imagej.ops.Op;
import net.imagej.ops.ViewOps.View;
import net.imglib2.type.Type;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = View.NAME)
public class Rotate<I extends Type<I>> implements InputOp<IntervalView<I>>, DefaultView<IntervalView<I>> {


	@Parameter(type = ItemIO.OUTPUT)
	private IntervalView<I> output;
	
	@Parameter(type = ItemIO.INPUT)
	private int fromAxis;
	
	@Parameter(type = ItemIO.INPUT)
	private int toAxis;
	

	@Parameter(type = ItemIO.INPUT)
	private IntervalView<I> input;


	@Override
	public IntervalView<I> getOutput() {
		return output;
	}


	@Override
	public void setOutput(IntervalView<I> output) {
		this.output = output;
	}


	@Override
	public void run() {
		output = Views.rotate(input, fromAxis, toAxis);
	}


	@Override
	public IntervalView<I> getInput() {
		return input;
	}


	@Override
	public void setInput(IntervalView<I> input) {
		this.input = input;
	}


}
