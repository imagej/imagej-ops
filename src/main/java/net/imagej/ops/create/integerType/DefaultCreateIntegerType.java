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

package net.imagej.ops.create.integerType;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractNullaryFunctionOp;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.IntegerType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.integer.ShortType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedIntType;
import net.imglib2.type.numeric.integer.UnsignedShortType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Create an IntegerType with at least {@code maxValue} maximum.
 *
 * @author Christian Dietz (University of Konstanz)
 */
@SuppressWarnings("rawtypes")
@Plugin(type = Ops.Create.IntegerType.class)
public class DefaultCreateIntegerType extends
	AbstractNullaryFunctionOp<IntegerType> implements Ops.Create.IntegerType
{

	@Parameter(required = false)
	private long maxValue;

	@Override
	public IntegerType calculate() {
		if (maxValue <= 0L) return new IntType();
		if (maxValue <= 1L) return new BitType();
		if (maxValue <= 0x7fL) return new ByteType();
		if (maxValue <= 0xffL) return new UnsignedByteType();
		if (maxValue <= 0x7fffL) return new ShortType();
		if (maxValue <= 0xffffL) return new UnsignedShortType();
		if (maxValue <= 0x7fffffffL) return new IntType();
		if (maxValue <= 0xffffffffL) return new UnsignedIntType();
		return new LongType();
	}

}
