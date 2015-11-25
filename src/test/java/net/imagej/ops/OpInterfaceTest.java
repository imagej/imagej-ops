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

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.scijava.InstantiableException;
import org.scijava.plugin.PluginInfo;

/**
 * Test for marker interface of each Op.
 * 
 * @author Leon Yang
 */
public class OpInterfaceTest extends AbstractOpTest {

	/**
	 * Tests if each Op implements the marker interface that the it uses as its
	 * plug-in type.
	 */
	@Test
	public void testOpInterface() {
		List<PluginInfo<Op>> infos = ops.getPlugins();
		for (PluginInfo<Op> info : infos) {
			final Class<Op> type = info.getPluginType();
			try {
				info.loadClass();
			}
			catch (InstantiableException e) {
				// do something?
				e.printStackTrace();
				continue;
			}
			final Class<? extends Op> clazz = info.getPluginClass();
			final String msg = type + " is not assignable from " + clazz;
			assertTrue(msg, type.isAssignableFrom(clazz));
		}
	}
}
