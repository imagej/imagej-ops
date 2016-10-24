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

package net.imagej.ops.pixml;

import net.imagej.ops.Op;
import net.imagej.ops.map.MapView;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.IntegerType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Default {@link HardClusterer} implementation.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @author Stefan Helfrich (University of Konstanz)
 * @param <I> type of input
 * @param <O> type of output
 */
@Plugin(type = Op.class)
public class DefaultHardClustererFunctionOp<I, O extends IntegerType<O>> extends
	AbstractUnaryFunctionOp<IterableInterval<I>, IterableInterval<O>> implements
	HardClusterer<I, O>
{

	@Parameter(required = true)
	private UnsupervisedLearner<I, O> learner;
	
	@Parameter(required = true)
	private O type;

	/** Maps the model (i.e. a {@code UnvaryComputerOp}) onto the input (lazy). */
	private MapView<I, O, IterableInterval<I>, IterableInterval<O>> mapView;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		// FIXME is there a way to do something like following: 
		// ops.create().typeFrom(genericOf) instead of passing the type as parameter?
		// @ctrueden?
		mapView = (MapView) Functions.unary(ops(), MapView.class,
			IterableInterval.class, IterableInterval.class, UnaryComputerOp.class, type);
	}

	@Override
	public IterableInterval<O> compute1(IterableInterval<I> input) {
		mapView.setOp(learner.compute1(input));
		return mapView.compute1(input);
	}

}
