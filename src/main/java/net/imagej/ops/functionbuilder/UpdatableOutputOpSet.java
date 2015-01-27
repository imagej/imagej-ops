package net.imagej.ops.functionbuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.imagej.ops.Computer;
import net.imagej.ops.InputOp;
import net.imagej.ops.OpRef;
import net.imagej.ops.OutputOp;

import org.scijava.ItemIO;
import org.scijava.module.Module;
import org.scijava.plugin.Parameter;

public class UpdatableOutputOpSet<I, O> implements InputOp<I>,
		OutputOp<Map<OutputOpRef<O>, OutputOp<O>>>,
		Computer<I, Map<OutputOpRef<O>, OutputOp<O>>> {

	@Parameter
	private I input;

	@Parameter(type = ItemIO.OUTPUT)
	private Map<OutputOpRef<O>, OutputOp<O>> output;

	private SourceOp<I> source;
	private Map<OpRef, ? extends Module> globalSet;

	@SuppressWarnings("unchecked")
	public UpdatableOutputOpSet(final SourceOp<I> source,
			final Map<OpRef, ? extends Module> set,
			final Set<OutputOpRef<O>> outputOpRefs) {
		this.source = source;

		this.globalSet = set;
		this.output = new HashMap<OutputOpRef<O>, OutputOp<O>>();

		for (final OutputOpRef<O> ref : outputOpRefs) {
			output.put(ref, (OutputOp<O>) set.get(ref).getDelegateObject());
		}
	}

	public Map<OutputOpRef<O>, OutputOp<O>> get() {
		return output;
	}

	@Override
	public void run() {
		source.run();
		for (final OutputOpRef<O> op : output.keySet()) {
			globalSet.get(op).run();
		}
	}

	@Override
	public I getInput() {
		return input;
	}

	@Override
	public void setInput(I input) {
		if (this.input != input) {
			this.input = input;
			source.setInput(input);
		}
	}

	@Override
	public Map<OutputOpRef<O>, OutputOp<O>> compute(I input) {
		setInput(input);
		run();
		return getOutput();
	}

	@Override
	public Map<OutputOpRef<O>, OutputOp<O>> getOutput() {
		return output;
	}

	@Override
	public void setOutput(Map<OutputOpRef<O>, OutputOp<O>> output) {
		throw new UnsupportedOperationException("Not supported");
	}
}
