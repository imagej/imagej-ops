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
package net.imagej.ops.create;

import java.io.IOException;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Ops.CreateImg;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests several ways to create an image
 *
 * @author Christian Dietz (University of Konstanz)
 */
public class CreateImgTest extends AbstractOpTest {

	private Img<ByteType> m_asImg;
	private RandomAccessibleInterval<ByteType> m_asRAI;
	private Interval m_asInterval;

	@Before
	public void createData() {
		m_asImg = generateByteTestImg(false, new long[] { 100, 100 });
		m_asRAI = Views.rotate(m_asImg, 1, 0);
		m_asInterval = new FinalInterval(m_asImg);
	}

	@Test
	public void testSameTypeCreation() throws IOException {

		@SuppressWarnings("unchecked")
		Img<ByteType> a = (Img<ByteType>) ops.createimg(m_asImg);

		assertTrue(Intervals.equalDimensions(a, m_asImg));

		@SuppressWarnings("unchecked")
		Img<ByteType> b = (Img<ByteType>) ops.createimg(m_asRAI);

		assertTrue(Intervals.equalDimensions(b, m_asImg));

		@SuppressWarnings("unchecked")
		Img<ByteType> c = (Img<ByteType>) ops.createimg(m_asInterval);

		assertTrue(Intervals.equalDimensions(c, m_asImg));

	}
}
