package net.imagej.ops.features;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.imagej.ops.Computer;
import net.imagej.ops.InputOp;
import net.imagej.ops.Op;
import net.imagej.ops.OpRef;
import net.imagej.ops.OutputOp;

import org.scijava.ItemIO;
import org.scijava.module.Module;
import org.scijava.plugin.Parameter;

public class ResolvedOpSet<I> implements InputOp<I>,
		OutputOp<Map<OpRef<?>, Op>>, Computer<I, Map<OpRef<?>, Op>> {

	@Parameter
	private I input;

	@Parameter(type = ItemIO.OUTPUT)
	private Map<OpRef<?>, Op> output;

	private SourceOp<I> source;
	private Map<OpRef<?>, ? extends Module> globalSet;

	public ResolvedOpSet(final SourceOp<I> source,
			final Map<OpRef<?>, ? extends Module> set,
			final Set<OpRef<?>> outputOpRefs) {
		this.source = source;

		this.globalSet = set;
		this.output = new HashMap<OpRef<?>, Op>();

		for (final OpRef<?> ref : outputOpRefs) {
			output.put(ref, (Op) set.get(ref).getDelegateObject());
		}
	}

	public Map<OpRef<?>, Op> get() {
		return output;
	}

	@Override
	public void run() {
		source.run();
		for (final OpRef<?> op : output.keySet()) {
			globalSet.get(op).run();
		}
	}

	@Override
	public I getInput() {
		return input;
	}

	@Override
	public void setInput(final I input) {
			this.input = input;
			source.setInput(input);
	}

	@Override
	public Map<OpRef<?>, Op> compute(final I input) {
		setInput(input);
		run();
		return getOutput();
	}

	@Override
	public Map<OpRef<?>, Op> getOutput() {
		return output;
	}

	@Override
	public void setOutput(final Map<OpRef<?>, Op> output) {
		throw new UnsupportedOperationException("Not supported");
	}
}
