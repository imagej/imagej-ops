
package imagej.ops.descriptors.statistics;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

public abstract class AbstractDescriptor<O> implements Descriptor<O> {

	@Parameter(type = ItemIO.OUTPUT)
	private O output;

	@Override
	public O getOutput() {
		return output;
	}

	@Override
	public void setOutput(final O output) {
		this.output = output;
	}

	@Override
	public void run() {
		if (output == null) setOutput(initOutput());
		compute(output);
	}

	protected abstract void compute(O output);

	public abstract O initOutput();

}
