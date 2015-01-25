package net.imagej.ops.functionbuilder;

import net.imagej.ops.Computer;
import net.imagej.ops.InputOp;
import net.imagej.ops.OutputOp;

class OutputComputer<I, O> implements Computer<I, O> {

	private InputOp<I> source;
	private OutputOp<O> sink;

	public OutputComputer(final InputOp<I> source, final OutputOp<O> sink) {
		this.source = source;
		this.sink = sink;
	}

	@Override
	public O compute(I input) {
		source.setInput(input);
		source.run();
		sink.run();
		return sink.getOutput();
	}
}