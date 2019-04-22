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
import net.imagej.ops.thread.chunker.CursorBasedChunk;
import net.imagej.ops.thread.chunker.DefaultChunker;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.real.DoubleType;

import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.Views;
import org.scijava.plugin.Plugin;

/**
 * An Op which calculates the euler characteristic (χ) of the given 3D binary image.<br>
 * Here Euler characteristic is defined as χ = β_0 - β_1 + β_2, where β_i are so called Betti numbers
 * <ul>
 * <li>β_0 = number of separate particles</li>
 * <li>β_1 = number of handles</li>
 * <li>β_2 = number enclosed cavities</li>
 * </ul>
 * <p>
 * The Op calculates χ by using the triangulation algorithm described by Toriwaki {@literal &} Yonekura (see below).<br>
 * There it's calculated X = ∑Δχ(V), where V is a 2x2x2 neighborhood around each point in the 3D space.<br>
 * We are using the 26-neighborhood version of the algorithm. The Δχ(V) values here are predetermined.
 * </p><p>
 * For the algorithm see<br>
 * Toriwaki J, Yonekura T (2002)<br>
 * Euler Number and Connectivity Indexes of a Three Dimensional Digital Picture<br>
 * Forma 17: 183-209<br>
 * <a href="http://www.scipress.org/journals/forma/abstract/1703/17030183.html">
 * http://www.scipress.org/journals/forma/abstract/1703/17030183.html</a>
 * </p><p>
 * For the Betti number definition of Euler characteristic see<br>
 * Odgaard A, Gundersen HJG (1993)<br>
 * Quantification of connectivity in cancellous bone, with special emphasis on 3-D reconstructions<br>
 * Bone 14: 173-182<br>
 * <a href="http://dx.doi.org/10.1016/8756-3282(93)90245-6">doi:10.1016/8756-3282(93)90245-6</a>
 * </p>
 * @author Richard Domander (Royal Veterinary College, London)
 * @author David Legland  - original MatLab implementation
 */
@Plugin(type = Ops.Topology.EulerCharacteristic26N.class)
public class EulerCharacteristic26N<B extends BooleanType<B>>
        extends AbstractUnaryHybridCF<RandomAccessibleInterval<B>, DoubleType>
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
    public boolean conforms() { return in().numDimensions() == 3; }

    @Override
    public void compute(RandomAccessibleInterval<B> interval, DoubleType output) {
        long size = (interval.dimension(0) - 1) * (interval.dimension(1) - 1) * (interval.dimension(2) - 1);
        size = Math.max(1, size);
        ops().run(DefaultChunker.class, new EulerCharacteristic26N.EulerChunk<>(interval, output), size);
        output.set(output.get() / 8.0);
    }

    static  <B extends BooleanType<B>> int neighborhoodEulerIndex(RandomAccess<B> access, long[] position) {
        int index = 0;
        index += getAtLocation(access, position, 0, 0, 0);
        index += getAtLocation(access, position, 1, 0, 0) << 1;
        index += getAtLocation(access, position, 0, 1, 0) << 2;
        index += getAtLocation(access, position, 1, 1, 0) << 3;
        index += getAtLocation(access, position, 0, 0, 1) << 4;
        index += getAtLocation(access, position, 1, 0, 1) << 5;
        index += getAtLocation(access, position, 0, 1, 1) << 6;
        index += getAtLocation(access, position, 1, 1, 1) << 7;
        return index;
    }

    private static <B extends BooleanType<B>> int getAtLocation(final RandomAccess<B> access,
                                                                final long[] position,
                                                                final long... offsets) {
        long x = position[0] + offsets[0];
        long y = position[1] + offsets[1];
        long z = position[2] + offsets[2];
        access.setPosition(x, 0);
        access.setPosition(y, 1);
        access.setPosition(z, 2);
        return (int) access.get().getRealDouble();
    }

    @Override
    public DoubleType createOutput(RandomAccessibleInterval<B> input) { return new DoubleType(0.0); }

    private static class EulerChunk<B extends BooleanType<B>> extends CursorBasedChunk {

        private final RandomAccessibleInterval<B> input;

        private final DoubleType output;

        private EulerChunk(RandomAccessibleInterval<B> in,
                           DoubleType out) {
            input = in;
            output = out;
        }

        @Override
        public void execute(int startIndex, int stepSize, int numSteps) {
            final long[] position = new long[3];
            final RandomAccess<B> access = input.randomAccess();
            final Cursor<B> cursor = Views.interval(input, new long[]{0, 0, 0},
                    new long[]{input.max(0) - 1, input.max(1) - 1, input.max(2) - 1}).cursor();
            int steps = 0;
            double sumDeltaEuler = 0;

            setToStart(cursor, startIndex);
            while (steps < numSteps) {
                cursor.localize(position);
                final int index = neighborhoodEulerIndex(access, position);
                sumDeltaEuler += EULER_LUT[index];
                cursor.jumpFwd(stepSize);
                steps++;
            }

            synchronized (this) {
                // Tbe final answer is the sum of Δχ of all the subspaces
                output.set(output.get() + sumDeltaEuler);
            }
        }
    }
}
