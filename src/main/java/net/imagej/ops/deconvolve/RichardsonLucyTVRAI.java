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

package net.imagej.ops.deconvolve;

import org.scijava.Priority;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.special.AbstractUnaryComputerOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;

/**
 * Richardson Lucy op that operates on (@link RandomAccessibleInterval)
 * Richardson-Lucy algorithm with total variation regularization for 3D confocal
 * microscope deconvolution Microsc Res Rech 2006 Apr; 69(4)- 260-6 The
 * div_unit_grad function has been adapted from IOCBIOS, Pearu Peterson
 * https://code.google.com/p/iocbio/
 * 
 * @author bnorthan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
@Plugin(type = Ops.Deconvolve.RichardsonLucyTV.class,
	priority = Priority.HIGH_PRIORITY)
public class RichardsonLucyTVRAI<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends RichardsonLucyRAI<I, O, K, C> implements
	Ops.Deconvolve.RichardsonLucyTV
{

	@Parameter
	OpService ops;

	@Parameter
	private LogService log;

	@Parameter
	private float regularizationFactor = 0.2f;

	private Img<O> variation;

	AbstractUnaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> computeEstimateOp;

	@Override
	public void initialize() {
		super.initialize();

		Type<O> outType = Util.getTypeFromInterval(out());

		variation = getImgFactory().create(getRAIExtendedEstimate(), outType
			.createVariable());

		computeEstimateOp = ops.op(RichardsonLucyTVUpdateRAI.class,
			getRAIExtendedEstimate(), getRAIExtendedReblurred(),
			regularizationFactor);
		
		
	}

	@Override
	public void ComputeEstimate() {

		System.out.println("advancing even more!");
		computeEstimateOp.compute1(getRAIExtendedReblurred(),
			getRAIExtendedEstimate());

		// ops().run(test, getRAIExtendedEstimate(), getRAIExtendedReblurred(),
		// regularizationFactor, variation);

	}
}
