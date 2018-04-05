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

package net.imagej.ops.deconvolve;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Richardson Lucy with total variation function op that operates on (@link
 * RandomAccessibleInterval) (Richardson-Lucy algorithm with total variation
 * regularization for 3D confocal microscope deconvolution Microsc Res Rech 2006
 * Apr; 69(4)- 260-6)
 * 
 * @author bnorthan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
@Plugin(type = Ops.Deconvolve.RichardsonLucyTV.class,
	priority = Priority.HIGH)
public class RichardsonLucyTVF<I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>, C extends ComplexType<C> & NativeType<C>>
	extends RichardsonLucyF<I, O, K, C> implements
	Ops.Deconvolve.RichardsonLucyTV
{

	@Parameter
	private float regularizationFactor = 0.01f;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected
		UnaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>>
		getComputeEstimateOp()
	{
		// create Richardson Lucy TV update op, this will override the base RL
		// Update.
		return ((UnaryComputerOp) Computers.unary(ops(),
			RichardsonLucyTVUpdate.class, RandomAccessibleInterval.class,
			RandomAccessibleInterval.class, regularizationFactor));
	}
}
