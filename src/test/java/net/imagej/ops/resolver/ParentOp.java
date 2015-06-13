
package net.imagej.ops.resolver;

import net.imagej.ops.Op;
import net.imagej.ops.OutputOp;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class)
public class ParentOp implements OutputOp<DoubleType> {

	@Parameter
	private ChildOne inOne;

	@Parameter
	private ChildTwo inTwo;

	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType output;

	@Override
	public void run() {
		if (output == null) output = new DoubleType();

		output.set(inOne.getOutput());
		output.add(inTwo.getOutput());
	}

	@Override
	public DoubleType getOutput() {
		return output;
	}

	@Override
	public void setOutput(DoubleType output) {
		this.output = output;
	}
}
