/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
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

package net.imagej.ops.normalize;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Ops.Normalize;
import net.imagej.ops.statistics.FirstOrderOps.MinMax;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Test;

/**
 * @author Martin Horn
 */
public class NormalizeTest extends AbstractOpTest {

	@Test
	public void testNormalize() {

		Img<ByteType> in = generateByteTestImg(true, 5, 5);
		Img<ByteType> out = in.factory().create(in, new ByteType());

		// TODO: weird order of parameters
		ops.run(Normalize.class, out, in);

		List<ByteType> minmax1 = (List<ByteType>) ops.run(MinMax.class, in);
		List<ByteType> minmax2 = (List<ByteType>) ops.run(MinMax.class, out);

		assertEquals(minmax2.get(0).get(), Byte.MIN_VALUE);
		assertEquals(minmax2.get(1).get(), Byte.MAX_VALUE);

	}
}
