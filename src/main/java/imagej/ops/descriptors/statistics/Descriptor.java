
package imagej.ops.descriptors.statistics;

import imagej.ops.Op;

public interface Descriptor<O> extends Op {

	/**
	 * Reference passed.
	 * 
	 * @return
	 */
	O getOutput();

	/**
	 * pass reference
	 * 
	 * @param output
	 */
	void setOutput(O output);

}
