
package net.imagej.ops.create;

import net.imagej.ops.Op;
import net.imagej.ops.OutputOp;
import net.imagej.ops.create.CreateOps.CreateNativeType;
import net.imagej.ops.create.CreateOps.CreateType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of the {@link CreateType} interface.
 *
 * @author Daniel Seebacher, University of Konstanz.
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */
@Plugin(type = Op.class)
public class DefaultCreateNativeType implements
	CreateNativeType, OutputOp<DoubleType>
{

	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType output;

	@Override
	public void run() {
		output = new DoubleType();
	}

	@Override
	public DoubleType getOutput() {
		return output;
	}

	@Override
	public void setOutput(final DoubleType output) {
		this.output = output;
	}

}
