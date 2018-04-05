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

package net.imagej.ops.create;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.create.nativeType.CreateNativeTypeFromClass;
import net.imagej.ops.create.nativeType.DefaultCreateNativeType;
import net.imglib2.type.numeric.complex.ComplexDoubleType;
import net.imglib2.type.numeric.complex.ComplexFloatType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Test;

/**
 * Tests creating different NativeTypes.
 *
 * @author Brian Northan
 */
public class CreateNativeTypeTest extends AbstractOpTest {

	@Test
	public void testCreateNativeType() {

		// default
		Object type = ops.run(DefaultCreateNativeType.class);
		assertEquals(type.getClass(), DoubleType.class);

		// FloatType
		type = ops.run(CreateNativeTypeFromClass.class, FloatType.class);
		assertEquals(type.getClass(), FloatType.class);

		// ComplexFloatType
		type = ops.run(CreateNativeTypeFromClass.class, ComplexFloatType.class);
		assertEquals(type.getClass(), ComplexFloatType.class);

		// DoubleType
		type = ops.run(CreateNativeTypeFromClass.class, DoubleType.class);
		assertEquals(type.getClass(), DoubleType.class);

		// ComplexDoubleType
		type = ops.run(CreateNativeTypeFromClass.class, ComplexDoubleType.class);
		assertEquals(type.getClass(), ComplexDoubleType.class);

	}

}
