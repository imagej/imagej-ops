package net.imagej.ops;

public interface Computer<I, O> {
	/**
	 * Compute the output given some input.
	 * 
	 * @param input
	 *            of the {@link Computer}
	 * 
	 * @return output
	 */
	O compute(I input);

}
