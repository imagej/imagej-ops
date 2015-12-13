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

import java.util.ArrayList;
import java.util.Collection;

import net.imagej.ops.AbstractOp;
import net.imagej.ops.CustomOpEnvironment;
import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import net.imagej.ops.OpInfo;
import net.imagej.ops.OpRef;
import net.imagej.ops.special.UnaryFunctionOp;
import net.imagej.ops.special.UnaryHybridOp;

import org.scijava.Priority;
import org.scijava.cache.CacheService;
import org.scijava.command.CommandInfo;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
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
		
		if (prioritizedInfos != null) 
			for (final OpInfo info : prioritizedInfos) {
			info.cInfo().setPriority(Priority.FIRST_PRIORITY);
		}
	}

	@Override
	public Op op(final OpRef<?> ref) {
		final Op op = super.op(ref);
		final Op cachedOp;
		if (op instanceof UnaryHybridOp) {
			cachedOp = wrapUnaryHybrid((UnaryHybridOp<?, ?>) op);
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
		final UnaryHybridOp<I, O> op)
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
	 * Wraps a {@link UnaryFunctionOp} and caches the results. New inputs will result
	 * in re-computation of the result.
	 * 
	 * @author Christian Dietz, University of Konstanz
	 * @param <I>
	 * @param <O>
	 */
	class CachedFunctionOp<I, O> extends AbstractOp implements UnaryFunctionOp<I, O> {

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
		public O compute1(final I input) {

			final Hash hash = new Hash(input, delegate.getClass(), args);

			@SuppressWarnings("unchecked")
			O output = (O) cache.get(hash);

			if (output == null) {
				output = delegate.compute1(input);
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
	 * Wraps a {@link UnaryHybridOp} and caches the results. New inputs will result in
	 * re-computation if {@link UnaryHybridOp} is used as {@link UnaryFunctionOp}.
	 * 
	 * @author Christian Dietz, University of Konstanz
	 * @param <I>
	 * @param <O>
	 */
	class CachedHybridOp<I, O> extends CachedFunctionOp<I, O> implements
		UnaryHybridOp<I, O>
	{

		@Parameter
		private CacheService cache;

		private final UnaryHybridOp<I, O> delegate;

		private final Object[] args;

		public CachedHybridOp(final UnaryHybridOp<I, O> delegate, final Object[] args) {
			super(delegate, args);
			this.delegate = delegate;
			this.args = args;
		}

		@Override
<<<<<<< HEAD
		public O compute(final I input) {

			final Hash hash = new Hash(input, delegate.getClass(), args);
=======
		public O compute1(final I input) {
			final Hash hash = new Hash(input, delegate, args);
>>>>>>> imagej/master

			@SuppressWarnings("unchecked")
			O output = (O) cache.get(hash);

			if (output == null) {
				output = createOutput(input);
				compute1(input, output);
				cache.put(hash, output);
			}
			return output;
		}

		@Override
		public O createOutput(I input) {
			return delegate.createOutput(input);
		}

		@Override
		public void compute1(final I input, final O output) {
			delegate.compute1(input, output);
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

<<<<<<< HEAD
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
=======
		public Hash(final Object o1, final Object o2, final Object[] args) {
			long h = o1.hashCode() ^ o2.getClass().getSimpleName().hashCode();

			for (final Object o : args) {
				h ^= o.hashCode();
>>>>>>> imagej/master
			}

			hash = (int) h;
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
