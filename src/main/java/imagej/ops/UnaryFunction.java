
package imagej.ops;

/**
 * TODO
 * 
 * @author Christian Dietz
 * @author Martin Horn
 * @author NOT CURTIS RUEDEN
 */
public abstract class UnaryFunction<I, O> implements Op, Copyable {

	public abstract I getInput();

	public abstract O getOutput();

	public abstract void setInput(I input);

	public abstract void setOutput(O output);

	public abstract O compute(I input, O output);

	@Override
	public void run() {
		compute(getInput(), getOutput());
	}

	@Override
	public abstract UnaryFunction<I, O> copy();

}
