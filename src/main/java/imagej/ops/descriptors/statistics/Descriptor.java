
package imagej.ops.descriptors.statistics;

import imagej.ops.Op;

public interface Descriptor extends Op {

	/**
	 * Reference passed.
	 * 
	 * @return
	 */
	double[] getOutput();

	/**
	 * pass reference
	 * 
	 * @param output
	 */
	void setOutput(double[] output);

}
