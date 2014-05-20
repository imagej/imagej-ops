
package imagej.ops.descriptors.statistics;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

public abstract class AbstractDescriptor implements Descriptor {

	@Parameter(type = ItemIO.OUTPUT)
	private double[] output;

	@Override
	public double[] getOutput() {
		return output;
	}

	@Override
	public void setOutput(final double[] output) {
		this.output = output;
	}

	@Override
	public void run() {
		if (output == null) setOutput(initOutput());
		compute(output);
	}

	protected abstract void compute(double[] output);

	public abstract double[] initOutput();

}
