/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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
package net.imagej.ops.features.gabor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;

import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ImageProcessor;
import net.imagej.ops.features.AbstractFeatureTest;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * 
 * Test for {@Link Gabor} features
 * 
 * @author Daniel Seebacher, University of Konstanz
 *
 */
public class GaborFeatureTest extends AbstractFeatureTest {

	@Test
	public void testGaborFilterBank() {
		List<List<Img<DoubleType>>> filterBank = ops.gabor().filterBank(5, 8, 39, 39);
	}

	@Test
	public void testGaborFeature() throws IOException {
		List<List<Img<DoubleType>>> filterBank = ops.gabor().filterBank(5, 8, 39, 39);

		// read simple polygon image
		final BufferedImage read = ImageIO.read(AbstractFeatureTest.class.getResourceAsStream("cZgkFsK_expensive.png"));

		ImagePlus openImage = new Opener()
				.openImage(AbstractFeatureTest.class.getResource("cZgkFsK_expensive.png").getPath());

		Img<DoubleType> img = ops.create().img(new FinalInterval(openImage.getWidth(), openImage.getHeight()),
				new DoubleType());

		ImageProcessor processor = openImage.getProcessor();

		Cursor<DoubleType> cur = img.localizingCursor();

		while (cur.hasNext()) {

			cur.fwd();

			Color color = new Color(processor.getPixel(cur.getIntPosition(0), cur.getIntPosition(1)));

			int col = (int) Math.round(0.2989 * color.getRed() + 0.5870 * color.getGreen() + 0.1140 * color.getBlue());
			cur.get().setReal(col);
		}

		double[] res = ops.gabor().feature(img, filterBank, 4, 4);

		System.out.println(res.length);
		System.out.println(res[res.length - 1]);
	}

}
