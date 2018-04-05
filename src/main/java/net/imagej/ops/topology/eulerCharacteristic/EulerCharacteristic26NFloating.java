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
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * An Op which calculates the euler characteristic (χ) of the given binary image. The object in the image
 * is handled as if it was floating freely in space. That is, elements outside the stack are treated
 * as zeros. Thus voxels touching the edges of the interval do not affect the result.
 * Here Euler characteristic is defined as χ = β_0 - β_1 + β_2, where β_i are so called Betti numbers.
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
 *
 * @author Richard Domander (Royal Veterinary College, London)
 * @author Michael Doube (Royal Veterinary College, London)
 */
@Plugin(type = Ops.Topology.EulerCharacteristic26NFloating.class)
public class EulerCharacteristic26NFloating
        <B extends BooleanType<B>> extends AbstractUnaryHybridCF<RandomAccessibleInterval<B>, DoubleType>
        implements Ops.Topology.EulerCharacteristic26NFloating, Contingent {
    /** Δχ(v) for all configurations of a 2x2x2 voxel neighborhood */
    private static final int[] EULER_LUT = new int[256];

    //region fill EULER_LUT
    static {
        EULER_LUT[1] = 1;
        EULER_LUT[3] = 0;
        EULER_LUT[5] = 0;
        EULER_LUT[7] = -1;
        EULER_LUT[9] = -2;
        EULER_LUT[11] = -1;
        EULER_LUT[13] = -1;
        EULER_LUT[15] = 0;
        EULER_LUT[17] = 0;
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
        EULER_LUT[45] = 0;
        EULER_LUT[47] = -1;
        EULER_LUT[49] = -1;

        EULER_LUT[51] = 0;
        EULER_LUT[53] = -2;
        EULER_LUT[55] = -1;
        EULER_LUT[57] = 0;
        EULER_LUT[59] = -1;
        EULER_LUT[61] = 1;
        EULER_LUT[63] = 0;
        EULER_LUT[65] = -2;
        EULER_LUT[67] = -3;
        EULER_LUT[69] = -1;
        EULER_LUT[71] = -2;
        EULER_LUT[73] = -1;
        EULER_LUT[75] = 0;
        EULER_LUT[77] = -2;
        EULER_LUT[79] = -1;
        EULER_LUT[81] = -1;
        EULER_LUT[83] = -2;
        EULER_LUT[85] = 0;
        EULER_LUT[87] = -1;
        EULER_LUT[89] = 0;
        EULER_LUT[91] = 1;
        EULER_LUT[93] = -1;
        EULER_LUT[95] = 0;
        EULER_LUT[97] = -1;
        EULER_LUT[99] = 0;

        EULER_LUT[101] = 0;
        EULER_LUT[103] = 1;
        EULER_LUT[105] = 4;
        EULER_LUT[107] = 3;
        EULER_LUT[109] = 3;
        EULER_LUT[111] = 2;
        EULER_LUT[113] = -2;
        EULER_LUT[115] = -1;
        EULER_LUT[117] = -1;
        EULER_LUT[119] = 0;
        EULER_LUT[121] = 3;
        EULER_LUT[123] = 2;
        EULER_LUT[125] = 2;
        EULER_LUT[127] = 1;
        EULER_LUT[129] = -6;
        EULER_LUT[131] = -3;
        EULER_LUT[133] = -3;
        EULER_LUT[135] = 0;
        EULER_LUT[137] = -3;
        EULER_LUT[139] = -2;
        EULER_LUT[141] = -2;
        EULER_LUT[143] = -1;
        EULER_LUT[145] = -3;
        EULER_LUT[147] = 0;
        EULER_LUT[149] = 0;

        EULER_LUT[151] = 3;
        EULER_LUT[153] = 0;
        EULER_LUT[155] = 1;
        EULER_LUT[157] = 1;
        EULER_LUT[159] = 2;
        EULER_LUT[161] = -3;
        EULER_LUT[163] = -2;
        EULER_LUT[165] = 0;
        EULER_LUT[167] = 1;
        EULER_LUT[169] = 0;
        EULER_LUT[171] = -1;
        EULER_LUT[173] = 1;
        EULER_LUT[175] = 0;
        EULER_LUT[177] = -2;
        EULER_LUT[179] = -1;
        EULER_LUT[181] = 1;
        EULER_LUT[183] = 2;
        EULER_LUT[185] = 1;
        EULER_LUT[187] = 0;
        EULER_LUT[189] = 2;
        EULER_LUT[191] = 1;
        EULER_LUT[193] = -3;
        EULER_LUT[195] = 0;
        EULER_LUT[197] = -2;
        EULER_LUT[199] = 1;

        EULER_LUT[201] = 0;
        EULER_LUT[203] = 1;
        EULER_LUT[205] = -1;
        EULER_LUT[207] = 0;
        EULER_LUT[209] = -2;
        EULER_LUT[211] = 1;
        EULER_LUT[213] = -1;
        EULER_LUT[215] = 2;
        EULER_LUT[217] = 1;
        EULER_LUT[219] = 2;
        EULER_LUT[221] = 0;
        EULER_LUT[223] = 1;
        EULER_LUT[225] = 0;
        EULER_LUT[227] = 1;
        EULER_LUT[229] = 1;
        EULER_LUT[231] = 2;
        EULER_LUT[233] = 3;
        EULER_LUT[235] = 2;
        EULER_LUT[237] = 2;
        EULER_LUT[239] = 1;
        EULER_LUT[241] = -1;
        EULER_LUT[243] = 0;
        EULER_LUT[245] = 0;
        EULER_LUT[247] = 1;
        EULER_LUT[249] = 2;
        EULER_LUT[251] = 1;
        EULER_LUT[253] = 1;
        EULER_LUT[255] = 0;
    }
    //endregion

    /** The algorithm is defined only for 3D images */
    @Override
    public boolean conforms() {
        return in().numDimensions() == 3;
    }

    @Override
    public void compute(RandomAccessibleInterval<B> interval, DoubleType output) {
        final Octant<B> octant = new Octant<>(interval);
        int sumDeltaEuler = 0;

        for (int z = 0; z <= interval.dimension(2); z++) {
            for (int y = 0; y <= interval.dimension(1); y++) {
                for (int x = 0; x <= interval.dimension(0); x++) {
                    octant.setNeighborhood(x, y, z);
                    sumDeltaEuler += getDeltaEuler(octant);
                }
            }
        }

        output.set(sumDeltaEuler / 8.0);
    }

    @Override
    public DoubleType createOutput(RandomAccessibleInterval<B> input) { return new DoubleType(0.0); }

    /** Determines the Δχ from Toriwaki & Yonekura value for this 2x2x2 neighborhood */
    private static int getDeltaEuler(final Octant octant) {
        if (octant.isNeighborhoodEmpty()) {
            return 0;
        }

        int index = 1;
        if (octant.isNeighborForeground(8)) {
            if (octant.isNeighborForeground(1)) { index |= 128; }
            if (octant.isNeighborForeground(2)) { index |= 64; }
            if (octant.isNeighborForeground(3)) { index |= 32; }
            if (octant.isNeighborForeground(4)) { index |= 16; }
            if (octant.isNeighborForeground(5)) { index |= 8; }
            if (octant.isNeighborForeground(6)) { index |= 4; }
            if (octant.isNeighborForeground(7)) { index |= 2; }
        } else if (octant.isNeighborForeground(7)) {
            if (octant.isNeighborForeground(2)) { index |= 128; }
            if (octant.isNeighborForeground(4)) { index |= 64; }
            if (octant.isNeighborForeground(1)) { index |= 32; }
            if (octant.isNeighborForeground(3)) { index |= 16; }
            if (octant.isNeighborForeground(6)) { index |= 8; }
            if (octant.isNeighborForeground(5)) { index |= 2; }
        } else if (octant.isNeighborForeground(6)) {
            if (octant.isNeighborForeground(3)) { index |= 128; }
            if (octant.isNeighborForeground(1)) { index |= 64; }
            if (octant.isNeighborForeground(4)) { index |= 32; }
            if (octant.isNeighborForeground(2)) { index |= 16; }
            if (octant.isNeighborForeground(5)) { index |= 4; }
        } else if (octant.isNeighborForeground(5)) {
            if (octant.isNeighborForeground(4)) { index |= 128; }
            if (octant.isNeighborForeground(3)) { index |= 64; }
            if (octant.isNeighborForeground(2)) { index |= 32; }
            if (octant.isNeighborForeground(1)) { index |= 16; }
        } else if (octant.isNeighborForeground(4)) {
            if (octant.isNeighborForeground(1)) { index |= 8; }
            if (octant.isNeighborForeground(3)) { index |= 4; }
            if (octant.isNeighborForeground(2)) { index |= 2; }
        } else if (octant.isNeighborForeground(3)) {
            if (octant.isNeighborForeground(2)) { index |= 8; }
            if (octant.isNeighborForeground(1)) { index |= 4; }
        } else if (octant.isNeighborForeground(2)) {
            if (octant.isNeighborForeground(1)) { index |= 2; }
        }

        return EULER_LUT[index];
    }
}
