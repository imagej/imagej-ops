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

package net.imagej.ops.cached;

import java.util.Collection;

import net.imagej.ops.AbstractOp;
import net.imagej.ops.CustomOpEnvironment;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.HybridOp;
import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import net.imagej.ops.OpInfo;

import org.scijava.Priority;
import org.scijava.cache.CacheService;
import org.scijava.plugin.Parameter;

/**
 * Creates {@link CachedFunctionOp}s which know how to cache their outputs.
 * 
 * @author Christian Dietz, University of Konstanz
 */
public class CachedOpEnvironment extends CustomOpEnvironment {

	@Parameter
	private CacheService cs;

	public CachedOpEnvironment(final OpEnvironment parent) {
		this(parent, null);
	}

	public CachedOpEnvironment(final OpEnvironment parent,
		final Collection<? extends OpInfo> prioritizedInfos)
	{
		super(parent, prioritizedInfos);
		for (final OpInfo info : prioritizedInfos) {
			info.cInfo().setPriority(Priority.FIRST_PRIORITY);
		}
	}

	@Override
	public <I, O, OP extends Op> FunctionOp<I, O> function(final Class<OP> opType,
		final Class<O> outType, final Class<I> inType, Object... otherArgs)
	{
		final CachedFunctionOp<I, O> cached = new CachedFunctionOp<I, O>(
			super.function(opType, outType, inType, otherArgs), otherArgs);
		getContext().inject(cached);
		return cached;
	}

	@Override
	public <I, O, OP extends Op> FunctionOp<I, O> function(final Class<OP> opType,
		final Class<O> outType, I in, Object... otherArgs)
	{
		final CachedFunctionOp<I, O> cached = new CachedFunctionOp<I, O>(
			super.function(opType, outType, in, otherArgs), otherArgs);
		getContext().inject(cached);
		return cached;
	}

	@Override
	public <I, O, OP extends Op> HybridOp<I, O> hybrid(Class<OP> opType,
		Class<O> outType, Class<I> inType, Object... otherArgs)
	{
		final CachedHybridOp<I, O> cached = new CachedHybridOp<I, O>(super.hybrid(
			opType, outType, inType, otherArgs), otherArgs);
		getContext().inject(cached);
		return cached;
	}

	@Override
	public <I, O, OP extends Op> HybridOp<I, O> hybrid(Class<OP> opType,
		Class<O> outType, I in, Object... otherArgs)
	{
		final CachedHybridOp<I, O> cached = new CachedHybridOp<I, O>(super.hybrid(
			opType, outType, in, otherArgs), otherArgs);
		getContext().inject(cached);
		return cached;
	}

	/**
	 * Wraps a {@link FunctionOp} and caches the results. New inputs will result
	 * in re-computation of the result.
	 * 
	 * @author Christian Dietz, University of Konstanz
	 * @param <I>
	 * @param <O>
	 */
	class CachedFunctionOp<I, O> extends AbstractOp implements FunctionOp<I, O> {

		@Parameter
		private CacheService cache;

		private final FunctionOp<I, O> delegate;

		private final Object[] args;

		public CachedFunctionOp(final FunctionOp<I, O> delegate,
			final Object[] args)
		{
			this.delegate = delegate;
			this.args = args;
		}

		@Override
		public O compute(final I input) {

			final Hash hash = new Hash(input, delegate.getClass(), args);

			@SuppressWarnings("unchecked")
			O output = (O) cache.get(hash);

			if (output == null) {
				output = delegate.compute(input);
				cache.put(hash, output);
			}
			return output;
		}

		@Override
		public void run() {
			delegate.run();
		}

		@Override
		public I in() {
			return delegate.in();
		}

		@Override
		public void setInput(I input) {
			delegate.setInput(input);
		}

		@Override
		public O out() {
			return delegate.out();
		}

		@Override
		public void initialize() {
			delegate.initialize();
		}

		@Override
		public CachedFunctionOp<I, O> getIndependentInstance() {
			return this;
		}

	}

	/**
	 * Wraps a {@link HybridOp} and caches the results. New inputs will result in
	 * re-computation if {@link HybridOp} is used as {@link FunctionOp}.
	 * 
	 * @author Christian Dietz, University of Konstanz
	 * @param <I>
	 * @param <O>
	 */
	class CachedHybridOp<I, O> extends CachedFunctionOp<I, O> implements
		HybridOp<I, O>
	{

		@Parameter
		private CacheService cache;

		private final HybridOp<I, O> delegate;

		private final Object[] args;

		public CachedHybridOp(final HybridOp<I, O> delegate, final Object[] args) {
			super(delegate, args);
			this.delegate = delegate;
			this.args = args;
		}

		@Override
		public O compute(final I input) {

			final Hash hash = new Hash(input, delegate.getClass(), args);

			@SuppressWarnings("unchecked")
			O output = (O) cache.get(hash);

			if (output == null) {
				output = createOutput(input);
				compute(input, output);
				cache.put(hash, output);
			}
			return output;
		}

		@Override
		public O createOutput(I input) {
			return delegate.createOutput(input);
		}

		@Override
		public void compute(final I input, final O output) {
			delegate.compute(input, output);
		}

		@Override
		public CachedHybridOp<I, O> getIndependentInstance() {
			return this;
		}
	}

	/**
	 * Simple utility class to wrap two objects and an array of objects in a
	 * single object which combines their hashes.
	 */
	private class Hash {

		private final int hash;

		private Class<?> delegate;

		public Hash(final Object o1, final Class<?> delegate, final Object[] args) {
			this.delegate = delegate;

			// Implement hash joining algorithm from Jon Skeet on SO:
			// http://stackoverflow.com/questions/263400/what-is-the-best-algorithm-for-an-overridden-system-object-gethashcode
			long hash = 17;

			hash = hash * 23 + o1.hashCode();
			hash = hash * 23 + delegate.getSimpleName().hashCode();

			for (final Object o : args) {
				hash = hash * 23 + o.hashCode();
			}

			this.hash = (int) hash;
		}

		@Override
		public int hashCode() {
			return hash;
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj == this) return true;
			if (obj instanceof Hash) return ((Hash) obj).delegate == delegate &&
				((Hash) obj).hash == hash;
			return false;
		}
	}
}
