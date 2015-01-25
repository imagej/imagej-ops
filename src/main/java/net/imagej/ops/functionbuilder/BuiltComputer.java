package net.imagej.ops.functionbuilder;

import java.util.List;

import net.imagej.ops.Computer;

class BuiltComputer<I, O> implements Computer<I, O> {

	private Computer<I, List<O>> computer;

	public BuiltComputer(final Computer<I, List<O>> computer) {
		this.computer = computer;
	}

	@Override
	public O compute(I input) {
		return computer.compute(input).get(0);
	}

}
