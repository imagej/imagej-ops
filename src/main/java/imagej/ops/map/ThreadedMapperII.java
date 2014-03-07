package imagej.ops.map;

import imagej.ops.Contingent;
import imagej.ops.Op;
import imagej.ops.UnaryFunction;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * @author Christian Dietz
 * @author Martin Horn
 * 
 * @param <A>
 * @param <B>
 */
@Plugin(type = Op.class, name = "map", priority = Priority.LOW_PRIORITY + 2)
public class ThreadedMapperII<A, B> extends AbstractThreadedMapper implements
        Contingent {

    @Parameter
    private IterableInterval<A> in;

    @Parameter
    private UnaryFunction<A, B> func;

    @Parameter(type = ItemIO.BOTH)
    private IterableInterval<B> out;

    @Override
    public void run() {
        runThreading(in.size());
    }

    @Override
    public boolean conforms() {
        return in.iterationOrder().equals(out.iterationOrder());
    }

    @Override
    protected void runThread(final int firstElement, final int lastElement) {
        final Cursor<A> inCursor = in.cursor();
        inCursor.jumpFwd(firstElement - 1);

        final Cursor<B> outCursor = out.cursor();
        final UnaryFunction<A, B> copy = func.copy();

        int ctr = 0;
        while (inCursor.hasNext() && ctr < lastElement + 1) {
            inCursor.fwd();
            outCursor.fwd();
            copy.compute(inCursor.get(), outCursor.get());
            ctr++;
        }
    }
}
