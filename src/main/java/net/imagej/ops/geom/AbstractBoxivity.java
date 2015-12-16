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

package net.imagej.ops.geom;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.Ops;
import net.imagej.ops.RTs;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Generic implementation of
 * {@link net.imagej.ops.Ops.Geometric.Boxivity}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */
public abstract class AbstractBoxivity<I> extends
	AbstractFunctionOp<I, DoubleType> implements Ops.Geometric.Boxivity
{

	private FunctionOp<I, DoubleType> areaFunc;

	private FunctionOp<I, I> smallestEnclosingRectangleFunc;

	private Class<I> inType;

	public AbstractBoxivity(final Class<I> inType) {
		this.inType = inType;
	}

	@Override
	public void initialize() {
		areaFunc = RTs.function(ops(), Ops.Geometric.Size.class, in());
		smallestEnclosingRectangleFunc = ops().function(
			Ops.Geometric.SmallestEnclosingBoundingBox.class, inType, in());
	}

	@Override
	public DoubleType compute(final I input) {
		return new DoubleType(areaFunc.compute(input).getRealDouble() / areaFunc
			.compute(smallestEnclosingRectangleFunc.compute(input)).getRealDouble());
	}

}
