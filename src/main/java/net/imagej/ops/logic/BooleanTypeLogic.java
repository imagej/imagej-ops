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

package net.imagej.ops.logic;

import net.imagej.ops.Ops;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCFI;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCFI;
import net.imglib2.type.BooleanType;

import org.scijava.plugin.Plugin;

/**
 * Logic operations with {@link BooleanType} as output.
 * 
 * @author Leon Yang
 */
public class BooleanTypeLogic {

	private BooleanTypeLogic() {
		// NB: Prevent instantiation of utility class.
	}

	/** Performs logical and (&&) between two {@link BooleanType}s. */
	@Plugin(type = Ops.Logic.And.class)
	public static class And<T extends BooleanType<T>> extends
		AbstractBinaryHybridCFI<T, T> implements Ops.Logic.And
	{

		@Override
		public T createOutput(final T in1, final T in2) {
			return in1.createVariable();
		}

		@Override
		public void compute2(final T in1, final T in2, final T out) {
			out.set(in1);
			out.and(in2);
		}

		@Override
		public void mutate1(final T arg, final T in) {
			compute2(arg, in, arg);
		}

		@Override
		public void mutate2(final T in, final T arg) {
			compute2(in, arg, arg);
		}
	}

	/** Performs logical not (!) on a {@link BooleanType}. */
	@Plugin(type = Ops.Logic.Not.class)
	public static class Not<T extends BooleanType<T>> extends
		AbstractUnaryHybridCFI<T, T> implements Ops.Logic.Not
	{

		@Override
		public T createOutput(final T in) {
			return in.createVariable();
		}

		@Override
		public void compute1(final T in, final T out) {
			out.set(in);
			out.not();
		}

		@Override
		public void mutate(final T arg) {
			arg.not();
		}
	}

	/** Performs logical or (||) between two {@link BooleanType}s. */
	@Plugin(type = Ops.Logic.Or.class)
	public static class Or<T extends BooleanType<T>> extends
		AbstractBinaryHybridCFI<T, T> implements Ops.Logic.Or
	{

		@Override
		public T createOutput(final T in1, final T in2) {
			return in1.createVariable();
		}

		@Override
		public void compute2(final T in1, final T in2, final T out) {
			out.set(in1);
			out.or(in2);
		}

		@Override
		public void mutate1(final T arg, final T in) {
			compute2(arg, in, arg);
		}

		@Override
		public void mutate2(final T in, final T arg) {
			compute2(in, arg, arg);
		}
	}

	/** Performs logical xor (^) between two {@link BooleanType}s. */
	@Plugin(type = Ops.Logic.Xor.class)
	public static class Xor<T extends BooleanType<T>> extends
		AbstractBinaryHybridCFI<T, T> implements Ops.Logic.Xor
	{

		@Override
		public T createOutput(final T in1, final T in2) {
			return in1.createVariable();
		}

		@Override
		public void compute2(final T in1, final T in2, final T out) {
			out.set(in1);
			out.xor(in2);
		}

		@Override
		public void mutate1(final T arg, final T in) {
			compute2(arg, in, arg);
		}

		@Override
		public void mutate2(final T in, final T arg) {
			compute2(in, arg, arg);
		}
	}
}
