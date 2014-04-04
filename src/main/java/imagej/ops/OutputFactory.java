
package imagej.ops;

/**
 * Factory which creates an object of type <O> given the input of type <I>
 * 
 * @author Christian Dietz
 */
public interface OutputFactory<I, O> {

	/**
	 * @param input which determines how to create the output
	 * @return output, depending on the input
	 */
	O create(I input);
}
