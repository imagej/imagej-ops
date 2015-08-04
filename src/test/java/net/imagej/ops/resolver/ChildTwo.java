package net.imagej.ops.resolver;

import net.imagej.ops.Op;
import net.imagej.ops.OutputOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
	@Plugin(type = Op.class)
public class ChildTwo implements OutputOp<DoubleType> {

	@Parameter
	private RandomAccessibleInterval<?> rai;

	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType output;

	// for testing lazy behaviour
	private int ctr = 1;

	private RandomAccessibleInterval<?> curr;

	public ChildTwo() {
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