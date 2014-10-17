package net.imagej.ops.operation.complex.binary;
 
import net.imagej.ops.Op;
import net.imagej.ops.arithmetic.add.Add;
import net.imagej.ops.condition.AbstractCondition;
import net.imagej.ops.condition.And;
import net.imglib2.type.numeric.NumericType;
 
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
 
public class ComplexSubtract<T extends NumericType<T>> extends
        ComplexBinaryOperation<T> implements Substract {
 
    @Parameter
    T input;
 
    @Override
    public T compute(T val) {
 
        T zero = null;
 
        val.sub(input);
 
        return val;
    }
 
}