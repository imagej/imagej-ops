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

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Op;
import net.imglib2.type.logic.BoolType;

import org.junit.Test;

/**
 * Tests comparison operators in {@link net.imagej.ops.logic}, including:
 * <ul>
 * <li>{@link ComparableGreaterThanOrEqual}</li>
 * <li>{@link ComparableGreaterThan}</li>
 * <li>{@link ComparableLessThanOrEqual}</li>
 * <li>{@link ComparableLessThan}</li>
 * <li>{@link ObjectsEqual}</li>
 * <li>{@link ObjectsNotEqual}</li>
 * </ul>
 */
public class ComparisonTest extends AbstractOpTest {

	@Test
	public void testComparableGreaterThan() {
		assertEquals(true, run(ComparableGreaterThan.class, 2, 1));
		assertEquals(false, run(ComparableGreaterThan.class, 2, 2));
		assertEquals(false, run(ComparableGreaterThan.class, 2, 3));
	}

	@Test
	public void testComparableGreaterThanOrEqual() {
		assertEquals(true, run(ComparableGreaterThanOrEqual.class, 2, 1));
		assertEquals(true, run(ComparableGreaterThanOrEqual.class, 2, 2));
		assertEquals(false, run(ComparableGreaterThanOrEqual.class, 2, 3));
	}

	@Test
	public void testComparableLessThan() {
		assertEquals(false, run(ComparableLessThan.class, 2, 1));
		assertEquals(false, run(ComparableLessThan.class, 2, 2));
		assertEquals(true, run(ComparableLessThan.class, 2, 3));
	}

	@Test
	public void testComparableLessThanOrEqual() {
		assertEquals(false, run(ComparableLessThanOrEqual.class, 2, 1));
		assertEquals(true, run(ComparableLessThanOrEqual.class, 2, 2));
		assertEquals(true, run(ComparableLessThanOrEqual.class, 2, 3));
	}

	@Test
	public void testObjectsEqual() {
		assertEquals(false, run(ObjectsEqual.class, 2, 1));
		assertEquals(true, run(ObjectsEqual.class, 2, 2));
		assertEquals(false, run(ObjectsEqual.class, 2, 3));
	}

	@Test
	public void testObjectsNotEqual() {
		assertEquals(true, run(ObjectsNotEqual.class, 2, 1));
		assertEquals(false, run(ObjectsNotEqual.class, 2, 2));
		assertEquals(true, run(ObjectsNotEqual.class, 2, 3));
	}

	// -- Helper methods --

	private <OP extends Op> boolean run(Class<OP> type, Object... args) {
		final BoolType result = (BoolType) ops.run(type, args);
		return result.get();
	}

}
