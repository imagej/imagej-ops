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

package net.imagej.ops.logic;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.OpMethod;

/**
 * The logic namespace contains logical (i.e., boolean) operations.
 *
 * @author Curtis Rueden
 */
public class LogicNamespace extends AbstractNamespace {

	// -- Logic namespace ops --

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.BooleanAnd.class)
	public boolean and(final boolean a, final boolean b) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.PrimitiveLogic.BooleanAnd.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.BooleanEqual.class)
	public boolean equal(final boolean a, final boolean b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.BooleanEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.IntegerEqual.class)
	public boolean equal(final int a, final int b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.IntegerEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.LongEqual.class)
	public boolean equal(final long a, final long b) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.PrimitiveLogic.LongEqual.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.FloatEqual.class)
	public boolean equal(final float a, final float b) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.PrimitiveLogic.FloatEqual.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.PrimitiveLogic.DoubleEqual.class)
	public boolean equal(final double a, final double b) {
		final boolean result =
			(Boolean) ops().run(
				net.imagej.ops.logic.PrimitiveLogic.DoubleEqual.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.logic.ObjectsEqual.class)
	public boolean equal(final Object a, final Object b) {
		final boolean result =
			(Boolean) ops().run(net.imagej.ops.logic.ObjectsEqual.class, a, b);
		return result;
	}

	// -- Named methods --

	@Override
	public String getName() {
		return "logic";
	}

}
