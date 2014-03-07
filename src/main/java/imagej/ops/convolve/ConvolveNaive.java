package imagej.ops.convolve;

import imagej.ops.Contingent;
import imagej.ops.Op;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Convolves an image naively.
 */
@Plugin(type = Op.class, name = "convolve")
public class ConvolveNaive<I extends RealType<I>, K extends RealType<K>, O extends RealType<O>>
        implements Op, Contingent {

    @Parameter
    private RandomAccessibleInterval<I> in;

    @Parameter
    private RandomAccessibleInterval<K> kernel;

    @Parameter(type = ItemIO.BOTH)
    private RandomAccessibleInterval<O> out;

    @Override
    public void run() {
        // TODO: try a decomposition of the kernel into n 1-dim kernels

        long[] min = new long[in.numDimensions()];
        long[] max = new long[in.numDimensions()];

        for (int d = 0; d < kernel.numDimensions(); d++) {
            min[d] = -kernel.dimension(d);
            max[d] = kernel.dimension(d) + out.dimension(d);
        }

        final RandomAccess<I> inRA =
                in.randomAccess(new FinalInterval(min, max));

        final Cursor<K> kernelC = Views.iterable(kernel).localizingCursor();

        final Cursor<O> outC = Views.iterable(out).localizingCursor();

        final long[] pos = new long[in.numDimensions()];
        final long[] kernelRadius = new long[kernel.numDimensions()];
        for (int i = 0; i < kernelRadius.length; i++) {
            kernelRadius[i] = kernel.dimension(i) / 2;
        }

        float val;

        while (outC.hasNext()) {
            // image
            outC.fwd();
            outC.localize(pos);

            // kernel inlined version of the method convolve
            val = 0;
            inRA.setPosition(pos);

            kernelC.reset();
            while (kernelC.hasNext()) {
                kernelC.fwd();

                for (int i = 0; i < kernelRadius.length; i++) {
                    if (kernelRadius[i] > 0) { // dimension
                                               // can
                                               // have
                                               // zero
                                               // extension
                                               // e.g.
                                               // vertical
                                               // 1d
                                               // kernel
                        inRA.setPosition(pos[i] + kernelC.getLongPosition(i)
                                - kernelRadius[i], i);
                    }
                }

                val +=
                        inRA.get().getRealDouble()
                                * kernelC.get().getRealDouble();
            }

            outC.get().setReal(val);
        }
    }

    @Override
    public boolean conforms() {
        // conforms only if the kernel is sufficiently small
        return Intervals.numElements(kernel) <= 9;
    }

}
