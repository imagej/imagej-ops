package net.imagej.ops.viewOp;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.Type;

public abstract class AbstractDefaultView <I extends Type<I>> implements DefaultView<RandomAccessibleInterval<I>, RandomAccessibleInterval<?>> {
	@Parameter(type = ItemIO.OUTPUT)
	private RandomAccessibleInterval<?> output;
	
	@Parameter(type = ItemIO.INPUT)
	private RandomAccessibleInterval<I> input;
	
	@Override
	public void run() {
		output = compute(getInput());
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
