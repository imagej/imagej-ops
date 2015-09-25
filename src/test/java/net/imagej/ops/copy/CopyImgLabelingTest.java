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
package net.imagej.ops.copy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelingType;
import net.imglib2.type.numeric.integer.IntType;

import org.junit.Before;
import org.junit.Test;

/**
 * Test {@link CopyImgLabeling}
 * 
 * @author Christian Dietz, University of Konstanz
 */
public class CopyImgLabelingTest extends AbstractOpTest {

	private ImgLabeling<String, IntType> input;

	@SuppressWarnings("unchecked")
	@Before
	public void createData() {
		input = (ImgLabeling<String, IntType>) ops.create().imgLabeling(
				new long[] { 10, 10 }, new IntType());

		final Cursor<LabelingType<String>> inc = input.cursor();

		while (inc.hasNext()) {
			inc.next().add(Math.random() > 0.5 ? "A" : "B");
		}

		// and another loop to construct some ABs
		while (inc.hasNext()) {
			inc.next().add(Math.random() > 0.5 ? "A" : "B");
		}

	}

	@Test
	public void copyImgLabeling() {
		ImgLabeling<String, IntType> copy = (ImgLabeling<String, IntType>) ops
				.run(CopyImgLabeling.class, input);
		assertNotNull(copy);

		Cursor<LabelingType<String>> inCursor = input.cursor();
		for (final LabelingType<String> type : copy) {
			assertEquals(inCursor.next(), type);
		}
	}
}
