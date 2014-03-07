package imagej.ops.map;

import imagej.ops.Contingent;
import imagej.ops.Op;
import imagej.ops.UnaryFunction;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Applies a {@link UnaryFunction} to each pixel of an {@link IterableInterval}
 * and writes it into another {@link IterableInterval}.
 */
@Plugin(type = Op.class, name = "map")
public class MapPixII<S extends RealType<S>, T extends RealType<T>> implements
        Contingent, Op {

    @Parameter
    private IterableInterval<S> inImg;

    @Parameter
    UnaryFunction<S, T> function;

    @Parameter(type = ItemIO.BOTH)
    private IterableInterval<T> outImg;

    @Override
    public void run() {

        Cursor<S> inCur = inImg.cursor();
        Cursor<T> outCur = outImg.cursor();

        while (inCur.hasNext()) {
            inCur.next();
            outCur.next();
            function.compute(inCur.get(), outCur.get());
        }
    }

    @Override
    public boolean conforms() {
        return inImg.iterationOrder().equals(outImg.iterationOrder());
    }

}
