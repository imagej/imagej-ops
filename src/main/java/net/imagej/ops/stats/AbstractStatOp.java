
package net.imagej.ops.stats;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

import net.imagej.ops.AbstractHybridOp;
import net.imagej.ops.OpService;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

public abstract class AbstractStatOp<I, O extends RealType<O>> extends
	AbstractHybridOp<I, O>
{

	@Parameter(type = ItemIO.INPUT)
	protected OpService ops;

	@SuppressWarnings("unchecked")
	@Override
	public O createOutput(I input) {
		
		if(getOutput() != null){
			return getOutput();
		}
		
		return (O) new DoubleType();
	}

}
