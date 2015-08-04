
package net.imagej.ops.resolver;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = ChildOne.class)
public class ChildOneLowPriority implements ChildOne {

	@Parameter
	private RandomAccessibleInterval<?> rai;

	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType output;

	// for testing lazy behaviour
	private int ctr = 1;

	private RandomAccessibleInterval<?> curr;

	public ChildOneLowPriority() {
		//
	}

	@Override
	public void run() {
		if (curr == null) {
			curr = rai;
		}

		if (curr != rai) {
			ctr++;
		}
		output = new DoubleType(ctr);
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
