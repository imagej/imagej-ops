package net.imagej.ops.topology.eulerCharacteristic;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.BooleanType;
import org.scijava.plugin.Plugin;

/**
 * An Op which calculates the euler characteristic (χ) of the given 3D binary image.
 * Here Euler characteristic is defined as χ = β_0 - β_1 + β_2, where β_i are so called Betti numbers.
 * β_0 = number of separate particles
 * β_1 = number of handles
 * β_2 = number enclosed cavities
 * <p>
 * The Op calculates χ by using the triangulation algorithm described by Toriwaki & Yonekura (see below).
 * There it's calculated X = ∑Δχ(V), where V is a 2x2x2 neighborhood around each point in the 3D space.
 * We are using the 26-neighborhood version of the algorithm. The Δχ(V) values here are predetermined.
 * <p>
 * For the algorithm see
 * Toriwaki J, Yonekura T (2002) Euler Number and Connectivity Indexes of a Three Dimensional Digital Picture.
 * Forma 17: 183-209.
 * <a href="http://www.scipress.org/journals/forma/abstract/1703/17030183.html">
 * http://www.scipress.org/journals/forma/abstract/1703/17030183.html</a>
 * <p>
 * For the Betti number definition of Euler characteristic see
 * Odgaard A, Gundersen HJG (1993) Quantification of connectivity in cancellous bone,
 * with special emphasis on 3-D reconstructions.
 * Bone 14: 173-182.
 * <a href="http://dx.doi.org/10.1016/8756-3282(93)90245-6">doi:10.1016/8756-3282(93)90245-6</a>
 *
 * @author Richard Domander (Royal Veterinary College, London)
 * @author David Legland  - original MatLab implementation
 */
@Plugin(type = Ops.Topology.EulerCharacteristic26N.class)
public class EulerCharacteristic26N<B extends BooleanType<B>>
        extends AbstractUnaryFunctionOp<RandomAccessibleInterval<B>, Double>
        implements Ops.Topology.EulerCharacteristic26N, Contingent {
    /** Δχ(v) for all configurations of a 2x2x2 voxel neighborhood */
    private static final int[] EULER_LUT = {
             0,  1,  1,  0,  1,  0, -2, -1,  1, -2,  0, -1,  0, -1, -1,  0,
             1,  0, -2, -1, -2, -1, -1, -2, -6, -3, -3, -2, -3, -2,  0, -1,
             1, -2,  0, -1, -6, -3, -3, -2, -2, -1, -1, -2, -3,  0, -2, -1,
             0, -1, -1,  0, -3, -2,  0, -1, -3,  0, -2, -1,  0,  1,  1,  0,
             1, -2, -6, -3,  0, -1, -3, -2, -2, -1, -3,  0, -1, -2, -2, -1,
             0, -1, -3, -2, -1,  0,  0, -1, -3,  0,  0,  1, -2, -1,  1,  0,
            -2, -1, -3,  0, -3,  0,  0,  1, -1,  4,  0,  3,  0,  3,  1,  2,
            -1, -2, -2, -1, -2, -1,  1,  0,  0,  3,  1,  2,  1,  2,  2,  1,
             1, -6, -2, -3, -2, -3, -1,  0,  0, -3, -1, -2, -1, -2, -2, -1,
            -2, -3, -1,  0, -1,  0,  4,  3, -3,  0,  0,  1,  0,  1,  3,  2,
             0, -3, -1, -2, -3,  0,  0,  1, -1,  0,  0, -1, -2,  1, -1,  0,
            -1, -2, -2, -1,  0,  1,  3,  2, -2,  1, -1,  0,  1,  2,  2,  1,
             0, -3, -3,  0, -1, -2,  0,  1, -1,  0, -2,  1,  0, -1, -1,  0,
            -1, -2,  0,  1, -2, -1,  3,  2, -2,  1,  1,  2, -1,  0,  2,  1,
            -1,  0, -2,  1, -2,  1,  1,  2, -2,  3, -1,  2, -1,  2,  0,  1,
             0, -1, -1,  0, -1,  0,  2,  1, -1,  2,  0,  1,  0,  1,  1,  0
    };

    /** The algorithm is defined only for 3D images */
    @Override
    public boolean conforms() {
        return in().numDimensions() == 3;
    }

    @Override
    public Double compute1(RandomAccessibleInterval<B> interval) {
        final RandomAccess<B> access = interval.randomAccess();
        long sumDeltaEuler = 0;

        for (long z = 0; z < interval.dimension(2) - 1; z++) {
            for (long y = 0; y < interval.dimension(1)  - 1; y++) {
                for (long x = 0; x < interval.dimension(0)  - 1; x++) {
                    int index = neighborhoodEulerIndex(access, x, y, z);
                    sumDeltaEuler += EULER_LUT[index];
                }
            }
        }

        return sumDeltaEuler / 8.0;
    }

    /**
     * Determines the LUT index for this 2x2x2 neighborhood
     *
     * @param access    The space where the neighborhood is
     * @param x         Location of the neighborhood in the 1st spatial dimension (x)
     * @param y         Location of the neighborhood in the 2nd spatial dimension (y)
     * @param z         Location of the neighborhood in the 3rd spatial dimension (z)
     * @return the index of the Δχ value for this configuration of voxels
     */
    public static <B extends BooleanType<B>> int neighborhoodEulerIndex(final RandomAccess<B> access, final long x,
                                                                        final long y, final long z) {
        int index = 0;

        index += getAtLocation(access, x, y, z);
        index += getAtLocation(access, x + 1, y, z) << 1;
        index += getAtLocation(access, x, y + 1, z) << 2;
        index += getAtLocation(access, x + 1, y + 1, z) << 3;
        index += getAtLocation(access, x, y, z + 1) << 4;
        index += getAtLocation(access, x + 1, y, z + 1) << 5;
        index += getAtLocation(access, x, y + 1, z + 1) << 6;
        index += getAtLocation(access, x + 1, y + 1, z + 1) << 7;

        return index;
    }

    private static <B extends BooleanType<B>> long getAtLocation(final RandomAccess<B> access, final long x,
                                                                 final long y, final long z) {
        access.setPosition(x, 0);
        access.setPosition(y, 1);
        access.setPosition(z, 2);
        return (long) access.get().getRealDouble();
    }
}
