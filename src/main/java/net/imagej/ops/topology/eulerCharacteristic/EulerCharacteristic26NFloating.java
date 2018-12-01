/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package net.imagej.ops.topology.eulerCharacteristic;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.real.DoubleType;

import net.imglib2.view.Views;
import org.scijava.plugin.Plugin;

import java.util.stream.IntStream;

/**
 * An Op which calculates the euler characteristic (χ) of the given binary image. The object in the
 * image is handled as if it was floating freely in space. That is, elements outside the stack are
 * treated as zeros. Thus voxels touching the edges of the interval do not affect the result. Here
 * Euler characteristic is defined as χ = β_0 - β_1 + β_2, where β_i are so called Betti numbers.
 * <ul>
 * <li>β_0 = number of separate particles</li>
 * <li>β_1 = number of handles</li>
 * <li>β_2 = number enclosed cavities</li>
 * </ul>
 * <p>
 * The Op calculates χ by using the triangulation algorithm described by Toriwaki {@literal &}
 * Yonekura (see below).<br> There it's calculated X = ∑Δχ(V), where V is a 2x2x2 neighborhood
 * around each point in the 3D space.<br> We are using the 26-neighborhood version of the algorithm.
 * The Δχ(V) values here are predetermined.
 * </p><p>
 * For the algorithm see<br> Toriwaki J, Yonekura T (2002)<br> Euler Number and Connectivity Indexes
 * of a Three Dimensional Digital Picture<br> Forma 17: 183-209<br>
 * <a href="http://www.scipress.org/journals/forma/abstract/1703/17030183.html">
 * http://www.scipress.org/journals/forma/abstract/1703/17030183.html</a>
 * </p><p>
 * For the Betti number definition of Euler characteristic see<br> Odgaard A, Gundersen HJG
 * (1993)<br> Quantification of connectivity in cancellous bone, with special emphasis on 3-D
 * reconstructions<br> Bone 14: 173-182<br>
 * <a href="http://dx.doi.org/10.1016/8756-3282(93)90245-6">doi:10.1016/8756-3282(93)90245-6</a>
 * </p>
 *
 * @author Richard Domander (Royal Veterinary College, London)
 * @author Michael Doube (Royal Veterinary College, London)
 */
@Plugin(type = Ops.Topology.EulerCharacteristic26NFloating.class)
public class EulerCharacteristic26NFloating
        <B extends BooleanType<B>>
        extends AbstractUnaryHybridCF<RandomAccessibleInterval<B>, DoubleType>
        implements Ops.Topology.EulerCharacteristic26NFloating, Contingent {
    /**
     * Δχ(v) for all configurations of a 2x2x2 voxel neighborhood
     */
    private static final int[] EULER_LUT = new int[256];

    //region fill EULER_LUT
    static {
        EULER_LUT[1] = 1;
        EULER_LUT[7] = -1;
        EULER_LUT[9] = -2;
        EULER_LUT[11] = -1;
        EULER_LUT[13] = -1;

        EULER_LUT[19] = -1;
        EULER_LUT[21] = -1;
        EULER_LUT[23] = -2;
        EULER_LUT[25] = -3;
        EULER_LUT[27] = -2;

        EULER_LUT[29] = -2;
        EULER_LUT[31] = -1;
        EULER_LUT[33] = -2;
        EULER_LUT[35] = -1;
        EULER_LUT[37] = -3;

        EULER_LUT[39] = -2;
        EULER_LUT[41] = -1;
        EULER_LUT[43] = -2;
        EULER_LUT[47] = -1;
        EULER_LUT[49] = -1;

        EULER_LUT[53] = -2;
        EULER_LUT[55] = -1;
        EULER_LUT[59] = -1;
        EULER_LUT[61] = 1;
        EULER_LUT[65] = -2;

        EULER_LUT[67] = -3;
        EULER_LUT[69] = -1;
        EULER_LUT[71] = -2;
        EULER_LUT[73] = -1;
        EULER_LUT[77] = -2;

        EULER_LUT[79] = -1;
        EULER_LUT[81] = -1;
        EULER_LUT[83] = -2;
        EULER_LUT[87] = -1;
        EULER_LUT[91] = 1;

        EULER_LUT[93] = -1;
        EULER_LUT[97] = -1;
        EULER_LUT[103] = 1;
        EULER_LUT[105] = 4;
        EULER_LUT[107] = 3;

        EULER_LUT[109] = 3;
        EULER_LUT[111] = 2;
        EULER_LUT[113] = -2;
        EULER_LUT[115] = -1;
        EULER_LUT[117] = -1;

        EULER_LUT[121] = 3;
        EULER_LUT[123] = 2;
        EULER_LUT[125] = 2;
        EULER_LUT[127] = 1;
        EULER_LUT[129] = -6;

        EULER_LUT[131] = -3;
        EULER_LUT[133] = -3;
        EULER_LUT[137] = -3;
        EULER_LUT[139] = -2;
        EULER_LUT[141] = -2;

        EULER_LUT[143] = -1;
        EULER_LUT[145] = -3;
        EULER_LUT[151] = 3;
        EULER_LUT[155] = 1;
        EULER_LUT[157] = 1;

        EULER_LUT[159] = 2;
        EULER_LUT[161] = -3;
        EULER_LUT[163] = -2;
        EULER_LUT[167] = 1;
        EULER_LUT[171] = -1;

        EULER_LUT[173] = 1;
        EULER_LUT[177] = -2;
        EULER_LUT[179] = -1;
        EULER_LUT[181] = 1;
        EULER_LUT[183] = 2;

        EULER_LUT[185] = 1;
        EULER_LUT[189] = 2;
        EULER_LUT[191] = 1;
        EULER_LUT[193] = -3;
        EULER_LUT[197] = -2;

        EULER_LUT[199] = 1;
        EULER_LUT[203] = 1;
        EULER_LUT[205] = -1;
        EULER_LUT[209] = -2;
        EULER_LUT[211] = 1;

        EULER_LUT[213] = -1;
        EULER_LUT[215] = 2;
        EULER_LUT[217] = 1;
        EULER_LUT[219] = 2;
        EULER_LUT[223] = 1;

        EULER_LUT[227] = 1;
        EULER_LUT[229] = 1;
        EULER_LUT[231] = 2;
        EULER_LUT[233] = 3;
        EULER_LUT[235] = 2;

        EULER_LUT[237] = 2;
        EULER_LUT[239] = 1;
        EULER_LUT[241] = -1;
        EULER_LUT[247] = 1;
        EULER_LUT[249] = 2;

        EULER_LUT[251] = 1;
        EULER_LUT[253] = 1;
    }
    //endregion

    private static final int[][] NEIGHBOUR_VALUES = {
            {},
            {2},
            {4, 8},
            {8, 2, 4},
            {16, 32, 64, 128},
            {64, 16, 128, 32, 4},
            {32, 128, 16, 64, 2, 8},
            {128, 64, 32, 16, 8, 4, 2}
    };

    /**
     * The algorithm is defined only for 3D images
     */
    @Override
    public boolean conforms() {
        return in().numDimensions() == 3;
    }

    @Override
    public void compute(RandomAccessibleInterval<B> interval, DoubleType output) {
        long[] position = new long[3];
        int[] neighborhood = new int[8];

        int sumDeltaEuler = 0;
        RandomAccess<B> access = Views.extendZero(interval).randomAccess();
        final Cursor<B> cursor = createFloatingCursor(interval);

        while (cursor.hasNext()) {
            cursor.fwd();
            cursor.localize(position);
            fillNeighborhood(access, neighborhood, position);
            sumDeltaEuler += deltaEuler(neighborhood);
        }

        output.set(sumDeltaEuler / 8.0);
    }

    /**
     * Creates a cursor that traverses an interval one pixel per dimension bigger than the input.
     * <p>
     * Interval is only expanded in the positive direction, because neighborhoods are traversed in
     * the negative. Thus effectively the input interval is accessed "floating" two pixels wider in
     * each dimension.
     * </p>
     *
     * @param interval the input interval.
     * @return a larger "floating" interval.
     * @see #fillNeighborhood(RandomAccess, int[], long[])
     */
    private Cursor<B> createFloatingCursor(RandomAccessibleInterval<B> interval) {
        final long x = interval.dimension(0) + 1;
        final long y = interval.dimension(1) + 1;
        final long z = interval.dimension(2) + 1;
        return Views.offsetInterval(interval, new long[]{0, 0, 0}, new long[]{x, y, z}).cursor();
    }

    @Override
    public DoubleType createOutput(RandomAccessibleInterval<B> input) {
        return new DoubleType(0.0);
    }

    private static int deltaEuler(final int[] mask) {
        int mSN = findMostSignificantNeighbor(mask);
        if (mSN < 0) {
            return 0;
        }

        final int[] indexValues = NEIGHBOUR_VALUES[mSN];
        int index = 1;
        for (int i = 0; i < indexValues.length; i++) {
            index |= (mask[i] & 1) * indexValues[i];
        }

        return EULER_LUT[index];
    }

    private static int findMostSignificantNeighbor(int[] neighborhood) {
        return IntStream.range(0, neighborhood.length).map(i -> neighborhood.length - 1 - i)
                .filter(i -> neighborhood[i] == 1).findFirst().orElse(-1);
    }

    /**
     * Fills the eight voxel neighborhood around the given location.
     * <p>
     * Neighborhood is filled in the negative dimensions from the given position. If there's a voxel
     * at certain coordinates, then the neighbor is '1', '0'  if not.
     * </p>
     *
     * @param access       a zero extended access to input interval.
     * @param neighborhood an array for eight neighborhood values.
     * @param position     coordinates in the interval.
     * @param <B>          type of the elements in the interval.
     */
    private static <B extends BooleanType<B>> void fillNeighborhood(final RandomAccess<B> access,
                                                                    final int[] neighborhood,
                                                                    final long[] position) {
        neighborhood[0] = getAtLocation(access, position, -1, -1, -1);
        neighborhood[1] = getAtLocation(access, position, -1, 0, -1);
        neighborhood[2] = getAtLocation(access, position, 0, -1, -1);
        neighborhood[3] = getAtLocation(access, position, 0, 0, -1);
        neighborhood[4] = getAtLocation(access, position, -1, -1, 0);
        neighborhood[5] = getAtLocation(access, position, -1, 0, 0);
        neighborhood[6] = getAtLocation(access, position, 0, -1, 0);
        neighborhood[7] = getAtLocation(access, position, 0, 0, 0);
    }

    private static <B extends BooleanType<B>> int getAtLocation(RandomAccess<B> access,
                                                                long[] position, long... offsets) {
        long x = position[0] + offsets[0];
        long y = position[1] + offsets[1];
        long z = position[2] + offsets[2];
        access.setPosition(x, 0);
        access.setPosition(y, 1);
        access.setPosition(z, 2);
        return (int) access.get().getRealDouble();
    }
}