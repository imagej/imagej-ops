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

package imagej.ops.slice;

import static org.junit.Assert.assertTrue;
import imagej.ops.AbstractOpTest;
import imagej.ops.crop.Crop;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.labeling.Labeling;
import net.imglib2.labeling.NativeImgLabeling;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.view.Views;

import org.junit.Before;
import org.junit.Test;
import org.python.antlr.ast.Slice;

/**
 * Tests the {@link Slice}.
 * 
 * @author Christian Dietz
 */
public class SliceTest extends AbstractOpTest {

	private Img<ByteType> in;

	@Override
	@Before
	public void setUp() {
		super.setUp();
		in = ArrayImgs.bytes(20, 20, 20);
	}

	/**
	 * Verifies that the types of the objects returned by the {@link Slice} are
	 * correct.
	 */
	@Test
	public void testSlicerTypes() {

		// Set-up interval
		final Interval defInterval =
			new FinalInterval(new long[] { 0, 0, 0 }, new long[] { 19, 19, 19 });

		final Interval smallerInterval =
			new FinalInterval(new long[] { 0, 0, 0 }, new long[] { 19, 19, 18 });

		// check if result is ImgView
		assertTrue(ops.run(Crop.class, in, defInterval) instanceof Img);

		// check if result is LabelingView
		assertTrue(ops.run(Crop.class, new NativeImgLabeling<String, ByteType>(in),
			defInterval) instanceof Labeling);

		// check if result is ImgPlus
		assertTrue(ops.run(Crop.class, new ImgPlus<ByteType>(in), defInterval) instanceof ImgPlus);

		// check if result is RandomAccessibleInterval
		final Object run =
			ops.run("slicer", Views.interval(in, smallerInterval), smallerInterval);
		assertTrue(run instanceof RandomAccessibleInterval && !(run instanceof Img));
	}

	/** Tests the result of the slicing. */
	@SuppressWarnings("unchecked")
	@Test
	public void testSlicerResults() {

		// Case 1: fix one dimension
		Img<ByteType> res =
			(Img<ByteType>) ops.run(Crop.class, in, new FinalInterval(new long[] { 0,
				0, 5 }, new long[] { 19, 19, 5 }));

		assertTrue(res.numDimensions() == 2);
		assertTrue(res.min(0) == 0);
		assertTrue(res.max(0) == 19);

		// Case B: Fix one dimension and don't start at zero
		res =
			(Img<ByteType>) ops.run(Crop.class, in, new FinalInterval(new long[] { 0,
				0, 5 }, new long[] { 19, 0, 10 }));

		assertTrue(res.numDimensions() == 2);
		assertTrue(res.min(0) == 0);
		assertTrue(res.max(1) == 5);

		// Case C: fix two dimensions
		res =
			(Img<ByteType>) ops.run(Crop.class, in, new FinalInterval(new long[] { 0,
				0, 0 }, new long[] { 0, 15, 0 }));

		assertTrue(res.numDimensions() == 1);
		assertTrue(res.max(0) == 15);
	}
}
