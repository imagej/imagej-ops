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

package net.imagej.ops;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.scijava.InstantiableException;
import org.scijava.plugin.PluginInfo;

/**
 * Tests that all op plugins follow the core rules and conventions.
 *
 * @author Leon Yang
 * @author Curtis Rueden
 */
public class OpConformanceTest extends AbstractOpTest {

	/**
	 * Tests that each op implements the marker interface that it uses as its
	 * plugin type.
	 */
	@Test
	public void testOpInterface() throws InstantiableException {
		for (final PluginInfo<Op> info : ops.getPlugins()) {
			final Class<Op> type = info.getPluginType();
			final Class<? extends Op> c = info.loadClass();
			final String msg =
				type.getName() + " is not assignable from " + c.getName();
			assertTrue(msg, type.isAssignableFrom(c));
		}
	}

	@Test
	public void testOpPackages() {
		int bad = 0, total = 0;
		for (final OpInfo info : ops.infos()) {
			final String opName = info.getName();
			if (opName == null) continue;
			String opNamespace = beforeDot(opName);
			if ("test".equals(opNamespace)) continue; // skip test ops

			final String className = info.cInfo().getClassName();
			final String packageName = beforeDot(className);
			final String simpleName = afterDot(className);
			final boolean inner = simpleName.contains("$");

			final String expected;
			if (inner) {
				// inner classes use namespace package
				// e.g.: net.imagej.ops.math.sec == net.imagej.ops.math.UnaryRealTypeMath$Sec
				expected = "net.imagej.ops." + opNamespace;
			}
			else {
				// regular op classes use full op name package
				// e.g.: net.imagej.ops.morphology.close == net.imagej.ops.morphology.close.ListClose
				expected = "net.imagej.ops." + opName;
			}
			if (!packageName.equals(expected)) {
				System.err.println("[ERROR] " + //
					className + " should reside in package " + expected);
				bad++;
			}
			total++;
		}
		assertTrue(bad + "/" + total + " ops with non-matching packages", bad == 0);
	}

	// -- Helper methods --

	private String beforeDot(final String s) {
		final int dot = s.lastIndexOf(".");
		return dot < 0 ? "" : s.substring(0, dot);
	}

	private String afterDot(final String s) {
		return s.substring(s.lastIndexOf(".") + 1);
	}

}
