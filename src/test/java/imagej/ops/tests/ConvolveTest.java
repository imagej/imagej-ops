/*
 * #%L
 * A framework for reusable algorithms.
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

package imagej.ops.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import imagej.module.Module;
import imagej.ops.OpService;
import imagej.ops.convolve.ConvolveFourier;
import imagej.ops.convolve.ConvolveNaive;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scijava.Context;

public class ConvolveTest {

    private Context context;

    private OpService ops;

    @Before
    public void setUp() {
        context = new Context(OpService.class);
        ops = context.getService(OpService.class);
        assertTrue(ops != null);
    }

    @After
    public synchronized void cleanUp() {
        if (context != null) {
            context.dispose();
            context = null;
        }
    }

    @Test
    public void testConvolveMethodSelection() {

        Img<ByteType> in =
                new ArrayImgFactory<ByteType>().create(new int[]{20, 20},
                        new ByteType());
        Img<ByteType> out = in.copy();

        // testing for a small kernel
        Img<ByteType> kernel =
                new ArrayImgFactory<ByteType>().create(new int[]{3, 3},
                        new ByteType());
        Module module = ops.lookup("convolve", in, kernel, out);
        assertEquals(module.getInfo().getDelegateClassName(),
                ConvolveNaive.class.getName());

        // testing for a 'bigger' kernel
        kernel =
                new ArrayImgFactory<ByteType>().create(new int[]{10, 10},
                        new ByteType());
        module = ops.lookup("convolve", in, kernel, out);
        assertEquals(module.getInfo().getDelegateClassName(),
                ConvolveFourier.class.getName());

    }
}
