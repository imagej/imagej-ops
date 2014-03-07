package imagej.ops.convolve;

import imagej.ops.Op;

import java.util.Arrays;

import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.gauss3.Gauss3;
import net.imglib2.algorithm.gauss3.SeparableSymmetricConvolution;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory.Boundary;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "gauss")
public class Gauss<T extends RealType<T>> implements Op {

    @Parameter
    private RandomAccessibleInterval<T> in;

    @Parameter
    private RandomAccessibleInterval<T> out;

    @Parameter
    private double sigma;

    // TODO: make that selectable by the user/programmer
    private OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds =
            new OutOfBoundsMirrorFactory(Boundary.SINGLE);

    @Override
    public void run() {
        final RandomAccessible<FloatType> eIn =
                (RandomAccessible<FloatType>)Views.extend(in, outOfBounds);

        double[] sigmas = new double[in.numDimensions()];
        Arrays.fill(sigmas, sigma);

        try {
            SeparableSymmetricConvolution.convolve(Gauss3.halfkernels(sigmas),
                    eIn, out, Runtime.getRuntime().availableProcessors());

        } catch (final IncompatibleTypeException e) {
            // TODO: better error handling
            throw new RuntimeException(e);
        }

    }
}
