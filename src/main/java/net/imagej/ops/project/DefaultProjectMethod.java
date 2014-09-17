package net.imagej.ops.project;

import net.imagej.ops.AbstractFunction;
import net.imagej.ops.Function;
import net.imagej.ops.Op;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = ProjectMethod.NAME)
public class DefaultProjectMethod<T, V> extends AbstractFunction<Iterable<T>, V>
		implements ProjectMethod<T, V> {

	@Parameter
	private Function<Iterable<T>, V> func;

	@Override
	public V compute(Iterable<T> input, V output) {
		return func.compute(input, output);
	}
}