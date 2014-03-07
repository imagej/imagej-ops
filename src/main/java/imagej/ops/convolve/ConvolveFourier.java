package imagej.ops.convolve;

import imagej.ops.Contingent;
import imagej.ops.Op;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.complex.ComplexFloatType;
import net.imglib2.util.Intervals;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Convolves an image by transforming the kernel and the image into fourier
 * space, multiplying them and transforming the result back.
 */
@Plugin(type = Op.class, name = "convolve")
public class ConvolveFourier<I extends RealType<I>, K extends RealType<K>, O extends RealType<O>>
        implements Contingent, Op {

    @Parameter
    private RandomAccessibleInterval<I> in;

    @Parameter
    private RandomAccessibleInterval<K> kernel;

    @Parameter(type = ItemIO.BOTH)
    private RandomAccessibleInterval<O> out;

    // keep the last used image to avoid a newly fourier transformation if the
    // images has not been changed (only the kernel)
    private RandomAccessibleInterval<I> last = null;

    private FFTConvolution<I, K, O> fc = null;

    @Override
    public void run() {
        if (in.numDimensions() != kernel.numDimensions()) {
            // TODO: what is the right array handling??
            throw new IllegalStateException(
                    "Kernel dimensions do not match to Img dimensions in ImgLibImageConvolver!");
        }

        if (last != in) {
            last = in;
            fc =
                    FFTConvolution.create(last, out, kernel, kernel, out,
                            new ArrayImgFactory<ComplexFloatType>());
            fc.setKernel(kernel);
            fc.setKeepImgFFT(true);
        } else {
            fc.setKernel(kernel);
            fc.setOutput(out);
        }

        fc.run();

    }

    @Override
    public boolean conforms() {
        // TODO: only conforms if the kernel is sufficiently large (else the
        // naive approach should be used) -> what is a good heuristic??
        return Intervals.numElements(kernel) > 9;
    }

}
