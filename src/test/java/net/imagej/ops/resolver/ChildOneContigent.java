
package net.imagej.ops.resolver;

import net.imagej.ops.Contingent;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = ChildOne.class, priority = Priority.VERY_HIGH_PRIORITY)
public class ChildOneContigent implements ChildOne, Contingent {

	@Parameter
	private RandomAccessibleInterval<?> rai;

	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType output;

	// for testing lazy behaviour
	private int ctr = (int) Priority.VERY_HIGH_PRIORITY;

	private RandomAccessibleInterval<?> curr;

	public ChildOneContigent() {
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

	@Override
	public boolean conforms() {
		// this is never true. However, we want to check if there is an
		// NullPointerException thrown.
		return rai.numDimensions() == 1337;
	}
}
