/*-
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2022 ImageJ2 developers.
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

import java.util.List;

import net.imagej.ops.search.OpSearchResult;
import net.imagej.ops.search.OpSearcher;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.scijava.Context;
import org.scijava.app.AppService;
import org.scijava.cache.CacheService;
import org.scijava.module.Module;
import org.scijava.module.ModuleException;
import org.scijava.module.ModuleInfo;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.PluginInfo;
import org.scijava.plugin.PluginService;
import org.scijava.plugin.SciJavaPlugin;
import org.scijava.search.SearchResult;
import org.scijava.search.SearchService;

/**
 * Tests {@link OpSearcher}
 *
 * @author Gabriel Selzer
 */
public class OpSearcherTest extends AbstractOpTest {

	private OpSearcher searcher;

	@Override
	@Before
	public void setUp() {
		super.setUp();

		final List<PluginInfo<SciJavaPlugin>> searchers = context.getService(
			PluginService.class).getPluginsOfClass(OpSearcher.class);
		Assert.assertEquals(1, searchers.size());
		searcher = (OpSearcher) context.getService(PluginService.class)
			.createInstance(searchers.get(0));
	}

	@Override
	protected Context createContext() {
		return new Context(AppService.class, PluginService.class, OpService.class,
			OpMatchingService.class, CacheService.class, SearchService.class);
	}

	@Plugin(type = Op.class, name = "test.opSearcher")
	public static class OpSearcherRegressionOp extends
		AbstractUnaryFunctionOp<Img<UnsignedByteType>, String>
	{

		@Parameter
		public UnsignedByteType value;

		@Override
		public String calculate(final Img<UnsignedByteType> adsflkjsfdjlk) {
			return "You found me!";
		}
	}

	@Test
	public void testRegression() throws ModuleException {
		final List<SearchResult> results = searcher.search("test.opSearcher",
			false);

		final String sig = results.get(0).name();

		Assert.assertEquals("test.opSearcher(img \"in\", number \"value\") -> (string \"out\")",
			sig);
		final ModuleInfo info = ((OpSearchResult) results.get(0)).info();
		final Module module = info.createModule();
		module.setInput("in", ArrayImgs.unsignedBytes(4, 4));
		module.resolveInput("in");
		module.setInput("value", new UnsignedByteType(4));
		module.resolveInput("value");

		module.run();
		Assert.assertEquals("You found me!", module.getOutput("out"));

	}

	@Plugin(type = Op.class, name = "test.opSearcherEither")
	public static class OpSearcherUnsignedByte extends
		AbstractUnaryFunctionOp<Img<UnsignedByteType>, String>
	{

		@Parameter
		public UnsignedByteType value;

		@Override
		public String calculate(final Img<UnsignedByteType> adsflkjsfdjlk) {
			return "This is the UnsignedByte Op";
		}
	}

	@Plugin(type = Op.class, name = "test.opSearcherEither")
	public static class OpSearcherDouble extends
		AbstractUnaryFunctionOp<Img<DoubleType>, String>
	{

		@Parameter
		public DoubleType value;

		@Override
		public String calculate(final Img<DoubleType> adsflkjsfdjlk) {
			return "This is the Double Op";
		}
	}

	@Test
	public void testFlexibility() throws ModuleException {
		final List<SearchResult> results = searcher.search("test.opSearcherEither",
			false);
		Assert.assertEquals(1, results.size());

		final ModuleInfo info = ((OpSearchResult) results.get(0)).info();
		final Module module = info.createModule();
		// run the Op with unsigned bytes
		module.setInput("in", ArrayImgs.unsignedBytes(4, 4));
		module.resolveInput("in");
		module.setInput("value", new UnsignedByteType(4));
		module.resolveInput("value");

		module.run();
		Assert.assertEquals("This is the UnsignedByte Op", module.getOutput("out"));

		// run the Op with doubles
		module.setInput("in", ArrayImgs.doubles(4, 4));
		module.resolveInput("in");
		module.setInput("value", new DoubleType(4.4));
		module.resolveInput("value");

		module.run();
		Assert.assertEquals("This is the Double Op", module.getOutput("out"));

	}

	@Plugin(type = Op.class, name = "test.search")
	public static class FullSearchOp extends
		AbstractUnaryFunctionOp<Img<DoubleType>, String>
	{

		@Parameter
		public DoubleType value;

		@Override
		public String calculate(final Img<DoubleType> adsflkjsfdjlk) {
			return "This is the Double Op";
		}
	}

	@Plugin(type = Op.class, name = "test.search.priority")
	public static class PartialSearchOp extends
		AbstractUnaryFunctionOp<Img<DoubleType>, String>
	{

		@Parameter
		public DoubleType value;

		@Override
		public String calculate(final Img<DoubleType> adsflkjsfdjlk) {
			return "This is the Double Op";
		}
	}

	@Plugin(type = Op.class, name = "foo.test.search")
	public static class SubSearchOp extends
		AbstractUnaryFunctionOp<Img<DoubleType>, String>
	{

		@Parameter
		public DoubleType value;

		@Override
		public String calculate(final Img<DoubleType> adsflkjsfdjlk) {
			return "This is the Double Op";
		}
	}

	@Test
	public void testSearchResultsPriority() {
		final List<SearchResult> results = searcher.search("test.search", false);
		Assert.assertEquals(3, results.size());

		Assert.assertTrue(results.get(0).name().startsWith("test.search"));
		Assert.assertTrue(results.get(1).name().startsWith("test.search.priority"));
		Assert.assertTrue(results.get(2).name().startsWith("foo.test.search"));

	}

}
