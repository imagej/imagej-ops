package imagej.ops.threshold;

import imagej.ops.Op;
import imagej.ops.UnaryFunction;
import net.imglib2.type.logic.BitType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "applyThreshold")
public class PixThreshold<T extends Comparable<T>> extends
        UnaryFunction<T, BitType> {

    @Parameter
    private T threshold;

    @Parameter
    private T in;

    @Parameter(type = ItemIO.OUTPUT)
    private BitType out;

    @Override
    public T getInput() {
        return in;
    }

    @Override
    public BitType getOutput() {
        return out;
    }

    @Override
    public void setInput(T input) {
        in = input;
    }

    @Override
    public void setOutput(BitType output) {
        out = output;
    }

    @Override
    public BitType compute(T input, BitType output) {
        output.set(input.compareTo(threshold) > 0);
        return output;
    }

    @Override
    public UnaryFunction<T, BitType> copy() {
        PixThreshold<T> func = new PixThreshold<T>();
        func.threshold = threshold;
        return func;
    }
}
