package net.imagej.ops.create;

import net.imagej.ops.Op;
import net.imagej.ops.Ops.CreateType;
import net.imagej.ops.OutputOp;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of the {@link CreateType} interface.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 * @author Tim-Oliver Buchholz, University of Konstanz.
 * 
 * @param <T>
 */
@Plugin(type = Op.class)
public class DefaultCreateNativeType<T extends NativeType<T>> implements CreateType,
		OutputOp<T> {

	@Parameter(type = ItemIO.OUTPUT)
	private T output;

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		output = (T) new DoubleType();
	}

	@Override
	public T getOutput() {
		return output;

	}

	@Override
	public void setOutput(T output) {
		this.output = output;

	}

}
