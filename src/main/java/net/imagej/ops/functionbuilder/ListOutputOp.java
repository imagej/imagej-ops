package net.imagej.ops.functionbuilder;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.OutputOp;

class ListOutputOp<O> implements OutputOp<List<O>> {

	private List<O> output;
	private CachedModule[] sinks;

	public ListOutputOp(final CachedModule... sinks) {
		this.sinks = sinks;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		output = new ArrayList<O>();
		for (final CachedModule op : sinks) {
			op.run();
			output.add(((OutputOp<O>)op.getDelegateObject()).getOutput());
		}
	}

	@Override
	public List<O> getOutput() {
		return output;
	}

	@Override
	public void setOutput(List<O> output) {
		this.output = output;
	}

}
