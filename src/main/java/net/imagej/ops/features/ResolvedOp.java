package net.imagej.ops.features;

import net.imagej.ops.Computer;
import net.imagej.ops.InputOp;
import net.imagej.ops.OpRef;
import net.imagej.ops.OutputOp;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

public class ResolvedOp<I, O> implements InputOp<I>, OutputOp<O>,
		Computer<I, O> {

	@Parameter(type = ItemIO.OUTPUT)
	private O output;

	@Parameter
	private I input;

	private OutputOp<O> outputOp;

	private ResolvedOpSet<I> build;

	@SuppressWarnings("unchecked")
	public ResolvedOp(final ResolvedOpSet<I> build, final OpRef<?> ref) {
		this.outputOp = (OutputOp<O>) build.getOutput().get(ref);
		this.build = build;
	}

	@Override
	public void run() {
		build.compute(getInput());
		build.run();
		output = outputOp.getOutput();
	}

	@Override
	public I getInput() {
		return input;
	}

	@Override
	public void setInput(final I input) {
		this.input = input;
	}

	@Override
	public O compute(I input) {
		setInput(input);
		run();
		return output;
	}

	@Override
	public O getOutput() {
		return output;
	}

	@Override
	public void setOutput(O output) {
		this.output = output;
	}

}
