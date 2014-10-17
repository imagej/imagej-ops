package net.imagej.ops.operation.complex.binary;
 
import net.imagej.ops.Op;
import net.imagej.ops.arithmetic.add.Add;
import net.imagej.ops.condition.AbstractCondition;
import net.imagej.ops.condition.And;
import net.imglib2.type.numeric.NumericType;
 
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
 
public class ComplexPower<T extends NumericType<T>> extends ComplexBinaryOperation<T> implements Add {
 
    @Parameter
    private int power;
     
    @Override
    public T compute(T val) {
     
    for(int i = 0; i < power; i++)
    {
        val.mul(val);
    }
     
    return val;
    }
 
}