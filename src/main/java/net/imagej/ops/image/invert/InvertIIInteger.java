/*
  * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 Board of Regents of the University of
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

package net.imagej.ops.image.invert;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.types.UnboundedIntegerType;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.IntegerType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.AbstractIntegerType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.integer.Unsigned128BitType;
import net.imglib2.type.numeric.integer.UnsignedLongType;
import net.imglib2.type.numeric.integer.UnsignedVariableBitLengthType;
import net.imglib2.util.Pair;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * @author Gabe Selzer
 */
@Plugin(type = Ops.Image.Invert.class, priority = Priority.HIGH_PRIORITY)
public class InvertIIInteger<I extends IntegerType<I>, O extends IntegerType<O>> extends
	AbstractUnaryComputerOp<IterableInterval<I>, IterableInterval<O>> implements
	Ops.Image.Invert
{

	@Parameter(required = false)
	private I min;

	@Parameter(required = false)
	private I max;

	private UnaryComputerOp<IterableInterval<I>, IterableInterval<O>> mapper;

	@Override
	public void compute(final IterableInterval<I> input,
		final IterableInterval<O> output)
	{
		if (mapper == null) {
			final BigInteger minValue = min == null ? minValue(input.firstElement()).getBigInteger() : min.getBigInteger();
			final BigInteger maxValue = max == null ? maxValue(input.firstElement()).getBigInteger() : max.getBigInteger();
			final BigInteger minMax = minValue.add(maxValue);
			mapper = Computers.unary(ops(), Ops.Map.class, output, input,
				new AbstractUnaryComputerOp<I, O>()
			{

					@Override
					public void compute(I in, O out) {
						BigInteger inverted = minMax.subtract(in.getBigInteger());	
						
						if(in.getBigInteger().compareTo(maxValue(out).getBigInteger()) >= 0 || inverted.compareTo(minValue(out).getBigInteger()) <= 0) out.set(minValue(out));
						else if(in.getBigInteger().compareTo(minValue(out).getBigInteger()) <= 0 || (inverted.compareTo(maxValue(out).getBigInteger())) >= 0) out.set(maxValue(out));
						else out.setBigInteger(inverted);
						
						
					}
				});
		}
		mapper.compute(input, output);
	}

	public static <T extends RealType<T>> T minValue(T type) {
		if (type instanceof UnboundedIntegerType){
			UnboundedIntegerType t = new UnboundedIntegerType();
			t.setReal(0);
			return (T) t;
		}

		T min = type.createVariable();
		min.setReal(min.getMinValue());
		return (T) min;

	}
	
	public static <T extends RealType<T>> T maxValue(T type) {
		if (type instanceof Unsigned128BitType) {
			Unsigned128BitType t = new Unsigned128BitType();
			t.set(t.getMaxBigIntegerValue());
			return (T) t;
		}
		else if (type instanceof UnsignedLongType) {
			UnsignedLongType t = new UnsignedLongType();
			t.set(t.getMaxBigIntegerValue());
			return (T) t;
		}
		else if (type instanceof UnboundedIntegerType) {
			UnboundedIntegerType t = new UnboundedIntegerType();
			t.setReal(0);
			return (T) t;

		}
						
			T t = type.createVariable();
			t.setReal(type.getMaxValue());
			return (T) t;

	}
	
}

