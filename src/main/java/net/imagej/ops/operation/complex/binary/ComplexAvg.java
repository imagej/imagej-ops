package net.imagej.ops.operation.complex.binary;
 
import net.imagej.ops.Op;
import net.imagej.ops.arithmetic.add.Add;
import net.imagej.ops.condition.AbstractCondition;
import net.imagej.ops.condition.And;
import net.imglib2.type.numeric.NumericType;
 
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
 
public class ComplexAvg<T extends NumericType<T>> extends ComplexBinaryOperation<T> implements Add {
 
    @Parameter
    T input;
     
    @Override
    public T compute(T val) {
     
    val.add(input);
    val.mul(.5);
     
    return val;
    }
 
}