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
package net.imagej.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;
import org.scijava.util.ClassUtils;

/**
 * Tests that the Groovy-driven template generator works as expected.
 *
 * @author Johannes Schindelin
 * @author Jonathan Hale (University of Konstanz)
 * @author Curtis Rueden
 */
public class TemplateTest {

	/** Tests that generation works for scalars, lists and maps. */
	@Test
	public void testTemplate() throws IOException {
		final String prefix = ClassUtils.getLocation(getClass()).getFile();
		final File testFile = new File(prefix
				+ "/../generated-test-sources/from-template/TestTemplate.txt");
		final FileReader fileReader = new FileReader(testFile);
		final BufferedReader reader = new BufferedReader(fileReader);
		try {
			assertEquals("Template test: Pinky", reader.readLine());
			assertEquals("Multi-line: Ten weary, footsore travelers,", reader.readLine());
			assertNotNull(reader.readLine());
			assertNotNull(reader.readLine());
			assertEquals("one dark and rainy night...!", reader.readLine());
			assertNotNull(reader.readLine());
			assertEquals("The list of maps:", reader.readLine());
			assertEquals("alice who is 123 years old", reader.readLine());
			assertEquals("bob who is 1.2 years old", reader.readLine());
			assertEquals("charly ", reader.readLine());
			assertNull(reader.readLine());
		} finally {
			reader.close();
		}
	}

	/** Tests that automagical type parsing works as intended. */
	@Test
	public void testTypeDetection() throws IOException {
		final String prefix = ClassUtils.getLocation(getClass()).getFile();
		final File testFile = new File(prefix
				+ "/../generated-test-sources/from-template/TestTypes.txt");
		final FileReader fileReader = new FileReader(testFile);
		final BufferedReader reader = new BufferedReader(fileReader);
		try {
			int i = 0;
			while (true) {
				i++;
				final String line = reader.readLine();
				if (line == null) break;
				final String[] tokens = line.split("\t");
				assertEquals(2, tokens.length);
				assertEquals("Failure on line " + i + ":", tokens[0], tokens[1]);
			}
		}
		finally {
			reader.close();
		}
	}

}
