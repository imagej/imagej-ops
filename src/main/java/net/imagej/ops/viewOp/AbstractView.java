package net.imagej.ops.viewOp;

import net.imagej.ops.view.ViewOps.View;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

public abstract class AbstractView<I, O> implements
		View<I, O> {
	@Parameter(type = ItemIO.OUTPUT)
	private O output;

	@Parameter(type = ItemIO.INPUT)
	private I input;

	@Override
	public void run() {
		output = compute(getInput());
	}

	@Override
	public I getInput() {
		return input;
	}

	@Override
	public void setInput(I input) {
		this.input = input;
	}
}
