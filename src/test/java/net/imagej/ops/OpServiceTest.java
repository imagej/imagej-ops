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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Test;
import org.scijava.InstantiableException;
import org.scijava.Priority;
import org.scijava.module.Module;
import org.scijava.plugin.Attr;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.PluginInfo;

/**
 * Tests {@link OpService}.
 * 
 * @author Curtis Rueden
 * @author Johannes Schindelin
 */
public class OpServiceTest extends AbstractOpTest {

	/** Tests {@link OpService#run(String, Object...)}. */
	@Test
	public void testRunByName() {
		final DoubleType value = new DoubleType(123.456);

		assertFalse(Double.isInfinite(value.get()));
		final Object result = ops.run("test.infinity", value);
		assertSame(value, result);
		assertTrue(Double.isInfinite(value.get()));
	}

	/** Tests {@link OpService#run(Class, Object...)}. */
	@Test
	public void testRunByType() {
		final DoubleType value = new DoubleType(123.456);

		assertFalse(Double.isInfinite(value.get()));
		final Object result = ops.run(InfinityOp.class, value);
		assertSame(value, result);
		assertTrue(Double.isInfinite(value.get()));
	}

	/** Tests {@link OpService#run(Op, Object...)}. */
	@Test
	public void testRunByOp() {
		final DoubleType value = new DoubleType(123.456);

		assertFalse(Double.isInfinite(value.get()));
		final Object result = ops.run(new InfinityOp(), value);
		assertSame(value, result);
		assertTrue(Double.isInfinite(value.get()));
	}

	/** Tests {@link OpService#op(String, Object...)}. */
	@Test
	public void testOpByName() {
		final DoubleType value = new DoubleType(123.456);

		final Op op = ops.op("test.infinity", value);
		assertSame(InfinityOp.class, op.getClass());

		assertFalse(Double.isInfinite(value.get()));
		op.run();
		assertTrue(Double.isInfinite(value.get()));
	}

	/** Tests {@link OpService#op(Class, Object...)}. */
	@Test
	public void testOpByType() {
		final DoubleType value = new DoubleType(123.456);

		final Op op = ops.op(InfinityOp.class, value);
		assertSame(InfinityOp.class, op.getClass());

		assertFalse(Double.isInfinite(value.get()));
		op.run();
		assertTrue(Double.isInfinite(value.get()));
	}

	/** Tests {@link OpService#module(Class, Object...)}. */
	@Test
	public void testModuleByName() {
		final DoubleType value = new DoubleType(123.456);

		final Module module = ops.module("test.infinity", value);
		assertSame(value, module.getInput("arg"));

		assertFalse(Double.isInfinite(value.get()));
		module.run();
		assertTrue(Double.isInfinite(value.get()));
	}

	/** Tests {@link OpService#module(Class, Object...)}. */
	@Test
	public void testModuleByType() {
		final DoubleType value = new DoubleType(123.456);

		final Module module = ops.module(InfinityOp.class, value);
		assertSame(value, module.getInput("arg"));

		assertFalse(Double.isInfinite(value.get()));
		module.run();
		assertTrue(Double.isInfinite(value.get()));
	}

	/** Tests {@link OpService#module(Op, Object...)}. */
	@Test
	public void testModuleByOp() {
		final DoubleType value = new DoubleType(123.456);

		final Module module = ops.module(new InfinityOp(), value);
		assertSame(InfinityOp.class, module.getDelegateObject().getClass());
		assertSame(value, module.getInput("arg"));

		assertFalse(Double.isInfinite(value.get()));
		module.run();
		assertTrue(Double.isInfinite(value.get()));
	}

	/** Tests {@link OpService#run(String, Object...)}. */
	@Test
	public void testAliases() {
		final DoubleType value = new DoubleType(123.456);

		assertFalse(Double.isInfinite(value.get()));
		final Object result = ops.run("infin", value);
		assertSame(value, result);
		assertTrue(Double.isInfinite(value.get()));

		value.set(0.0);
		assertFalse(Double.isInfinite(value.get()));
		final Object result2 = ops.run("inf", value);
		assertSame(value, result2);
		assertTrue(Double.isInfinite(value.get()));

		value.set(0.0);
		boolean noSuchAlias = false;
		try {
			ops.run("infini", value);
		}
		catch (final IllegalArgumentException exc) {
			noSuchAlias = true;
		}
		assertTrue(noSuchAlias);
	}

	/** Tests op lookups by class. */
	@Test
	public void testMatchingByClass() {
		Op op = ops.op("test.arrr!", (Object) null);
		assertSame(FirstMate.class, op.getClass());
		op = ops.op("test.arrr!", (EyePatch) null);
		assertSame(FirstMate.class, op.getClass());
		op = ops.op("test.arrr!", (Booty) null);
		assertSame(FirstMate.class, op.getClass());
		op = ops.op("test.arrr!", EyePatch.class);
		assertSame(FirstMate.class, op.getClass());
		op = ops.op("test.arrr!", Booty.class);
		assertSame(Captain.class, op.getClass());
	}

	@Test
	public void testInstantiation() {
		int errors = 0;
		for (final PluginInfo<Op> info : ops.getPlugins()) {
			try {
				info.createInstance();
			}
			catch (InstantiableException e) {
				errors++;
				System.err.println("Could not instantiate " + info.getClassName());
			}
		}
		assertEquals(0, errors);
	}

	/** A test {@link Op}. */
	@Plugin(type = Op.class, name = "test.infinity",
		attrs = { @Attr(name = "aliases", value = "inf, infin") })
	public static class InfinityOp extends AbstractInplaceOp<DoubleType> {

		@Override
		public void compute(final DoubleType arg) {
			arg.set(Double.POSITIVE_INFINITY);
		}
	}

	public static interface EyePatch {
		// NB: Marker interface.
	}

	@Plugin(type = Op.class, name = "test.arrr!",
		priority = Priority.HIGH_PRIORITY)
	public static class FirstMate extends NoOp {

		@Parameter(required = false)
		private EyePatch inventory;
	}

	public static interface Booty {
		// NB: Marker interface.
	}

	@Plugin(type = Op.class, name = "test.arrr!")
	public static class Captain extends NoOp {

		@Parameter
		private Booty inventory;
	}

}
