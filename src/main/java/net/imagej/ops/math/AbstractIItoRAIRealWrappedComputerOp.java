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

package net.imagej.ops.math;

import net.imagej.ops.AbstractComputerOp;
import net.imagej.ops.ComputerOp;
import net.imagej.ops.ComputerWrapper;
import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

/**
 * Base class for {@link ComputerOp}<{@link IterableInterval}<I>, {@link 
 * RandomAccessibleInterval}<O>> that delegate to,
 * lower level {@link ComputerOp}< implementations.
 * 
 * Hopefully this provides Blitter-like capabilities (ImageJ1)
 * 
 * @author Jay Warrick
 */
public abstract class AbstractIItoRAIRealWrappedComputerOp<I extends RealType<I>, O extends RealType<O>> extends
AbstractComputerOp<IterableInterval<I>, RandomAccessibleInterval<O>> implements Contingent
{

	private Class<? extends Op> workerClass;
	
	private ComputerWrapper<I,O> wrapper;

	@Override
	public void initialize() {
		this.workerClass = getWorkerClass();
		this.wrapper = createWrapper();
		ops().context().inject(this.wrapper);;
	}

	@Override
	public void compute(final IterableInterval<I> input, final RandomAccessibleInterval<O> output) {

		final long[] pos = new long[input.numDimensions()];
		final Cursor<I> cursor = input.cursor();
		final RandomAccess<O> access = output.randomAccess();
		long curY = -1;
		while (cursor.hasNext()) {
			cursor.fwd();
			cursor.localize(pos);
			access.setPosition(pos);
			if(curY != pos[1])
			{
				System.out.println("x: "+pos[0]+ ", y: " + pos[1]);
				curY = pos[1];
			}
			this.wrapper.compute(this.workerClass, cursor.get(), access.get());
		}

	}

	@Override
	public boolean conforms() {
		int n = in().numDimensions();
		if (n != out().numDimensions()) return false;
		//		long[] dimsA = new long[n], dimsB = new long[n];
		//		in().dimensions(dimsA);
		//		out().dimensions(dimsB);
		//		for (int i = 0; i < n; i++) {
		//			if (dimsA[i] != dimsB[i]) return false;
		//		}
		return true;
	}

	protected abstract Class<? extends Op> getWorkerClass();
	
	protected abstract ComputerWrapper<I,O> createWrapper();

}
