package net.imagej.ops.operation.complex.binary;
 
import net.imagej.ops.Op;
import net.imagej.ops.arithmetic.add.Add;
import net.imagej.ops.condition.AbstractCondition;
import net.imagej.ops.condition.And;
import net.imglib2.type.numeric.NumericType;
 
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
 
public class ComplexDifference<T extends NumericType<T>> extends
        ComplexBinaryOperation<T> implements Difference {
 
    @Parameter
    T input;
 
    @Override
    public T compute(T val) {
 
        T zero = val.copy();
 
        zero.setZero();
        String z = zero.toString();
 
        val.sub(input);
        String s = val.toString();
 
        int value = s.compareTo(z);
 
        if (value < 0) {
            val.mul(-1);
        }
 
        return val;
    }
 
}