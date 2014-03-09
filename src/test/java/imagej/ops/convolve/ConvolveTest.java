/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package imagej.ops.convolve;

import static org.junit.Assert.assertSame;
import imagej.ops.AbstractOpTest;
import imagej.ops.Op;
import imagej.ops.convolve.ConvolveFourier;
import imagej.ops.convolve.ConvolveNaive;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Test;

/**
 * Tests involving convolvers.
 */
public class ConvolveTest extends AbstractOpTest {

		/** Tests that the correct convolver is selected. */
    @Test
    public void testConvolveMethodSelection() {

        final Img<ByteType> in =
                new ArrayImgFactory<ByteType>().create(new int[]{20, 20},
                        new ByteType());
        final Img<ByteType> out = in.copy();

        // testing for a small kernel
        Img<ByteType> kernel =
                new ArrayImgFactory<ByteType>().create(new int[]{3, 3},
                        new ByteType());
        Op op = ops.op("convolve", out, in, kernel);
        assertSame(ConvolveNaive.class, op.getClass());

        // testing for a 'bigger' kernel
        kernel =
                new ArrayImgFactory<ByteType>().create(new int[]{10, 10},
                        new ByteType());
        op = ops.op("convolve", in, out, kernel);
        assertSame(ConvolveFourier.class, op.getClass());

    }
}
