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
package net.imagej.ops.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;
import net.imglib2.view.composite.CompositeIntervalView;
import net.imglib2.view.composite.CompositeView;
import net.imglib2.view.composite.GenericComposite;

/**
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 * This test only checks if the op call works with all parameters and that the 
 * result is equal to the Views.method() call. 
 * This is not a correctness test of {@linkplain net.imglib2.view.Views}.
 */
public class DefaultCollapseTest extends AbstractOpTest {

	@Test
	public void collapseRATest() {

		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10, 10 }, new DoubleType());

		CompositeView<DoubleType, ? extends GenericComposite<DoubleType>> il2 = Views
				.collapse((RandomAccessible<DoubleType>) img);

		CompositeView<DoubleType, ? extends GenericComposite<DoubleType>> opr = ops.view()
				.collapse((RandomAccessible<DoubleType>) img);

		assertEquals(il2.numDimensions(), opr.numDimensions());
	}
	
	@Test
	public void collapseRAITest() {

		Img<DoubleType> img = new ArrayImgFactory<DoubleType>().create(new int[] { 10, 10, 10 }, new DoubleType());

		CompositeIntervalView<DoubleType, ? extends GenericComposite<DoubleType>> il2 = Views
				.collapse((RandomAccessibleInterval<DoubleType>) img);

		CompositeIntervalView<DoubleType, ? extends GenericComposite<DoubleType>> opr = ops.view()
				.collapse((RandomAccessibleInterval<DoubleType>) img);

		assertEquals(il2.numDimensions(), opr.numDimensions());
	}
}
