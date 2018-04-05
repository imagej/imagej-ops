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

package net.imagej.ops.cached;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import net.imagej.ops.AbstractOp;
import net.imagej.ops.CustomOpEnvironment;
import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import net.imagej.ops.OpInfo;
import net.imagej.ops.OpRef;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.UnaryHybridCF;

import org.scijava.Priority;
import org.scijava.cache.CacheService;
import org.scijava.command.CommandInfo;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Parameter;
import org.scijava.util.GenericUtils;

/**
 * Creates {@link CachedFunctionOp}s which know how to cache their outputs.
 * 
 * @author Christian Dietz (University of Konstanz)
 */
public class CachedOpEnvironment extends CustomOpEnvironment {

	@Parameter
	private CacheService cs;
	private Collection<Class<?>> ignoredOps;

	public CachedOpEnvironment(final OpEnvironment parent) {
		this(parent, null, new ArrayList<>());
	}

	public CachedOpEnvironment(final OpEnvironment parent,
		final Collection<? extends OpInfo> prioritizedInfos)
	{
		this(parent, prioritizedInfos, new ArrayList<>());
	}

	public CachedOpEnvironment(final OpEnvironment parent,
		final Collection<? extends OpInfo> prioritizedInfos,
		final Collection<Class<?>> ignoredOps)
	{
		super(parent, prioritizedInfos);

		if (prioritizedInfos != null) for (final OpInfo info : prioritizedInfos) {
			info.cInfo().setPriority(Priority.FIRST);
		}

		this.ignoredOps = ignoredOps;
	}

	@Override
	public Op op(final OpRef ref) {
		final Op op = super.op(ref);

		for (final Class<?> ignored : ignoredOps) {
			for (final Type t : ref.getTypes()) {
				// FIXME: Use generic assignability test, once it exists.
				final Class<?> raw = GenericUtils.getClass(t);
				if (ignored.isAssignableFrom(raw)) {
					return op;
				}
			}
		}

		final Op cachedOp;
		if (op instanceof UnaryHybridCF) {
			cachedOp = wrapUnaryHybrid((UnaryHybridCF<?, ?>) op);
		}
		else if (op instanceof UnaryFunctionOp) {
			cachedOp = wrapUnaryFunction((UnaryFunctionOp<?, ?>) op);
		}
		else return op;

		getContext().inject(cachedOp);
		return cachedOp;
	}

	// -- Helper methods --

	private <I, O> CachedFunctionOp<I, O> wrapUnaryFunction(
		final UnaryFunctionOp<I, O> op)
	{
		return new CachedFunctionOp<>(op, otherArgs(op, 1));
	}

	private <I, O> CachedHybridOp<I, O> wrapUnaryHybrid(
		final UnaryHybridCF<I, O> op)
	{
		return new CachedHybridOp<>(op, otherArgs(op, 2));
	}

	/**
	 * Gets the given {@link Op} instance's argument value, starting at the
	 * specified offset.
	 */
	private Object[] otherArgs(final Op op, final int offset) {
		final CommandInfo cInfo = info(op).cInfo();
		final Module module = cInfo.createModule(op);
		final ArrayList<Object> args = new ArrayList<>();
		int i = 0;
		for (final ModuleItem<?> input : cInfo.inputs()) {
			if (i++ >= offset) args.add(input.getValue(module));
		}
		return args.toArray();
	}

	// -- Helper classes --

	/**
	 * Wraps a {@link UnaryFunctionOp} and caches the results. New inputs will
	 * result in re-computation of the result.
	 * 
	 * @author Christian Dietz (University of Konstanz)
	 * @param <I>
	 * @param <O>
	 */
	class CachedFunctionOp<I, O> extends AbstractOp implements
		UnaryFunctionOp<I, O>
	{

		@Parameter
		private CacheService cache;

		private final UnaryFunctionOp<I, O> delegate;

		private final Object[] args;

		public CachedFunctionOp(final UnaryFunctionOp<I, O> delegate,
			final Object[] args)
		{
			this.delegate = delegate;
			this.args = args;
		}

		@Override
		public O calculate(final I input) {

			final Hash hash = new Hash(input, delegate, args);

			@SuppressWarnings("unchecked")
			O output = (O) cache.get(hash);

			if (output == null) {
				output = delegate.calculate(input);
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
	 * Wraps a {@link UnaryHybridCF} and caches the results. New inputs will
	 * result in re-computation if {@link UnaryHybridCF} is used as
	 * {@link UnaryFunctionOp}.
	 * 
	 * @author Christian Dietz (University of Konstanz)
	 * @param <I>
	 * @param <O>
	 */
	class CachedHybridOp<I, O> extends CachedFunctionOp<I, O> implements
		UnaryHybridCF<I, O>
	{

		@Parameter
		private CacheService cache;

		private final UnaryHybridCF<I, O> delegate;

		private final Object[] args;

		public CachedHybridOp(final UnaryHybridCF<I, O> delegate,
			final Object[] args)
		{
			super(delegate, args);
			this.delegate = delegate;
			this.args = args;
		}

		@Override
		public O calculate(final I input) {
			final Hash hash = new Hash(input, delegate, args);

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
		public void setOutput(final O output) {
			delegate.setOutput(output);
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

		public Hash(final Object o1, final Object o2, final Object[] args) {
			hash = Objects.hash(o1, o2.getClass(), Arrays.hashCode(args));
		}

		@Override
		public int hashCode() {
			return hash;
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj == this) return true;
			if (obj instanceof Hash) return hash == ((Hash) obj).hash;
			return false;
		}
	}
}
