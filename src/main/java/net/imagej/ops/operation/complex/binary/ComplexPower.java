package net.imagej.ops.operation.complex.binary;
 
import net.imagej.ops.AbstractFunction;
import net.imagej.ops.Op;
import net.imagej.ops.arithmetic.add.Add;
import net.imagej.ops.condition.AbstractCondition;
import net.imagej.ops.condition.And;
import net.imglib2.type.numeric.NumericType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
 
@Plugin(type = Op.class, name = Difference.NAME, priority = Priority.LOW_PRIORITY)
public class ComplexPower<T extends NumericType<T>> extends
AbstractFunction<T, T> implements Exponent {
 
    @Parameter
    private int value;
     


	@Override
	public T compute(T input, T output) {
		output.set(input);
		
		for(int i = 0; i < value; i++)
	    {
	        output.mul(input);
	    }

		return output;
	}
 
}