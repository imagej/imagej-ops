package net.imagej.ops;

public interface ComputerWrapper<I,O> {
	
	public void compute(Class<? extends Op> op, I input, O output);

}
