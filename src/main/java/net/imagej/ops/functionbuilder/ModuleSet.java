package net.imagej.ops.functionbuilder;

import java.util.Map;

import net.imagej.ops.InputOp;
import net.imagej.ops.OpRef;

import org.scijava.module.Module;

public class ModuleSet<I> implements InputOp<I> {

	private Map<OpRef, ? extends Module> set;
	private SourceOp<I> source;
	private I input;

	public ModuleSet(SourceOp<I> source, Map<OpRef, ? extends Module> set) {
		this.set = set;
		this.source = source;
	}

	public Module get(OpRef ref) {
		return set.get(ref);
	}

	@Override
	public void run() {
		source.run();
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
}
