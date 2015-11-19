package net.imagej.ops;

public interface ComputerWrapper<I,O> {
	
	public void compute(OpEnvironment ops, Class<? extends Op> op, I input, O output);

}
