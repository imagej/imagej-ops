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

package net.imagej.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Test;
import org.scijava.Context;
import org.scijava.plugin.Plugin;
import org.scijava.script.ScriptModule;
import org.scijava.script.ScriptService;
import org.scijava.service.AbstractService;
import org.scijava.service.Service;
import org.scijava.util.FileUtils;

/**
 * A test to ensure that the example in the <code>README.md</code> file works.
 * 
 * @author Johannes Schindelin
 */
public class ReadmeExampleTest {

	@Test
	public void testReadmesExample() throws Exception {
		// extract the example script
		final File readme = new File("README.md");
		final String contents = new String(FileUtils.readFile(readme), "UTF-8");
		final String telltale = String.format("```python%n");
		final int begin = contents.indexOf(telltale) + telltale.length();
		assertTrue(begin > telltale.length());
		assertTrue(contents.indexOf(telltale, begin) < 0);
		final int end = contents.indexOf(String.format("```%n"), begin);
		assertTrue(end > 0);
		final String snippet = contents.substring(begin, end);
		assertTrue(snippet.startsWith("# @ImageJ ij"));

		final Context context = new Context();
		final ScriptService script = context.getService(ScriptService.class);

		// create mock ImageJ gateway
		script.addAlias("ImageJ", Mock.class);
		final ScriptModule module =
			script.run("op-example.py", snippet, true).get();
		assertNotNull(module);
		module.run();

		final Mock ij = context.getService(Mock.class);
		assertEquals(3, ij.images.size());
		assertEquals(11.906, ij.getPixel("sinusoid", 50, 50), 1e-3);
		assertEquals(100, ij.getPixel("gradient", 50, 50), 1e-3);
		assertEquals(111.906, ij.getPixel("composite", 50, 50), 1e-3);
	}

	@Plugin(type = Service.class)
	public static class Mock extends AbstractService {
		protected final Map<String, Img<DoubleType>> images =
				new HashMap<String, Img<DoubleType>>();

		public OpService op() {
			return getContext().getService(OpService.class);
		}

		public Object ui() {
			return this;
		}

		public void show(final String name, final Img<DoubleType> img) {
			images.put(name, img);
		}

		public double getPixel(final String imageName, int x, int y) {
			final Img<DoubleType> img = images.get(imageName);
			final RandomAccess<DoubleType> access = img.randomAccess();
			access.setPosition(new int[] { x, y });
			return access.get().get();
		}
	}
}
