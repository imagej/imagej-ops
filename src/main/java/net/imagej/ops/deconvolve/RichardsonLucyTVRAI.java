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

import java.util.concurrent.atomic.AtomicInteger;

import net.imagej.ops.Ops;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.multithreading.SimpleMultiThreading;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.Context;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

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
	name = Ops.Deconvolve.RichardsonLucyTV.NAME,
	priority = Priority.HIGH_PRIORITY)
public class RichardsonLucyTVRAI<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends RichardsonLucyRAI<I, O, K, C>
{

	@Parameter
	Context ctx;

	@Parameter
	private float regularizationFactor = 0.2f;

	private Img<O> variation;

	protected RandomAccessibleInterval<O> raiExtendedVariation;

	@Override
	protected void initialize() {
		super.initialize();

		Type<O> outType = Util.getTypeFromInterval(getOutput());

		variation =
			getImgFactory()
				.create(getRAIExtendedEstimate(), outType.createVariable());

		// assemble the extended view of the variation buffer
		raiExtendedVariation =
			Views.interval(Views.extend(variation, getObfOutput()),
				getImgConvolutionInterval());
	}

	@Override
	public void ComputeEstimate() {

		long start = System.currentTimeMillis();

		div_unit_grad_fast_thread();
		long fasttime = System.currentTimeMillis() - start;

		final Cursor<O> cursorCorrelation =
			Views.iterable(getRAIExtendedReblurred()).cursor();

		final Cursor<O> cursorDV_estimate = variation.cursor();

		final Cursor<O> cursorEstimate =
			Views.iterable(getRAIExtendedEstimate()).cursor();

		while (cursorEstimate.hasNext()) {
			cursorCorrelation.fwd();
			cursorDV_estimate.fwd();
			cursorEstimate.fwd();

			cursorEstimate.get().mul(cursorCorrelation.get());
			cursorEstimate.get().mul(
				1f / (1f - regularizationFactor *
					cursorDV_estimate.get().getRealFloat()));
		}
	}

	static double hypot3(double a, double b, double c) {
		// return net.jafama.FastMath.sqrtQuick(a * a + b * b + c * c);
		return java.lang.Math.sqrt(a * a + b * b + c * c);
	}

	static double m(double a, double b) {
		if (a < 0 && b < 0) {
			if (a >= b) return a;
			return b;
		}
		if (a > 0 && b > 0) {
			if (a < b) return a;
			return b;
		}
		return 0.0;
	}

	final double FLOAT32_EPS = 0.0;

	/**
	 * Efficient multithreaded version of div_unit_grad adapted from IOCBIOS,
	 * Pearu Peterson https://code.google.com/p/iocbio/
	 */
	void div_unit_grad_fast_thread() {
		final int Nx, Ny, Nz;

		final RandomAccessibleInterval<O> raiExtendedEstimate =
			getRAIExtendedEstimate();

		Nx = (int) raiExtendedEstimate.dimension(0);
		Ny = (int) raiExtendedEstimate.dimension(1);

		if (raiExtendedEstimate.numDimensions() > 2) {
			Nz = (int) raiExtendedEstimate.dimension(2);
		}
		else {
			Nz = 1;
		}

		final AtomicInteger ai = new AtomicInteger(0);
		final int numThreads = 4;

		// TODO proper thread handling
		final Thread[] threads = SimpleMultiThreading.newThreads(numThreads);

		final int zChunkSize = Nz / threads.length;

		for (int ithread = 0; ithread < threads.length; ++ithread) {
			threads[ithread] = new Thread(new Runnable() {

				@Override
				public void run() {
					long starttime = System.currentTimeMillis();

					final RandomAccess<O> outRandom = variation.randomAccess();

					// Thread ID
					final int myNumber = ai.getAndIncrement();

					int start = myNumber * zChunkSize;

					int end;
					if (myNumber < numThreads - 1) {
						end = Math.min(start + zChunkSize, Nz);
					}
					else {
						end = Nz;
					}

					int i, j, k, im1, ip1, jm1, jp1, km1, kp1;

					double hx, hy, hz;
					double hx2, hy2, hz2;

					double fip, fim, fjp, fjm, fkp, fkm, fijk;
					double fimkm, fipkm, fjmkm, fjpkm, fimjm, fipjm, fimkp, fjmkp, fimjp;
					double aim, bjm, ckm, aijk, bijk, cijk;
					double Dxpf, Dxmf, Dypf, Dymf, Dzpf, Dzmf;
					double Dxma, Dymb, Dzmc;

					hx = 1;
					hy = 1;
					hz = 3;
					hx2 = 2 * hx;
					hy2 = 2 * hy;
					hz2 = 2 * hz;

					// i minus 1 cursors
					Cursor<O> fimjmCursor = Views.iterable(raiExtendedEstimate).cursor();
					Cursor<O> fimCursor = Views.iterable(raiExtendedEstimate).cursor();
					Cursor<O> fimkmCursor = Views.iterable(raiExtendedEstimate).cursor();
					Cursor<O> fimkpCursor = Views.iterable(raiExtendedEstimate).cursor();
					Cursor<O> fimjpCursor = Views.iterable(raiExtendedEstimate).cursor();

					// i cursors
					Cursor<O> fjmkmCursor = Views.iterable(raiExtendedEstimate).cursor();
					Cursor<O> fjmCursor = Views.iterable(raiExtendedEstimate).cursor();
					Cursor<O> fjmkpCursor = Views.iterable(raiExtendedEstimate).cursor();
					Cursor<O> fkmCursor = Views.iterable(raiExtendedEstimate).cursor();
					Cursor<O> fijkCursor = Views.iterable(raiExtendedEstimate).cursor();
					Cursor<O> fkpCursor = Views.iterable(raiExtendedEstimate).cursor();
					Cursor<O> fjpkmCursor = Views.iterable(raiExtendedEstimate).cursor();
					Cursor<O> fjpCursor = Views.iterable(raiExtendedEstimate).cursor();

					// i plus 1 cursors
					Cursor<O> fipjmCursor = Views.iterable(raiExtendedEstimate).cursor();
					Cursor<O> fipkmCursor = Views.iterable(raiExtendedEstimate).cursor();
					Cursor<O> fipCursor = Views.iterable(raiExtendedEstimate).cursor();

					for (k = start; k < end; k++) {
						km1 = (k > 0 ? k - 1 : 0);
						kp1 = (k + 1 == Nz ? k : k + 1);

						for (j = 0; j < Ny; j++) {
							jm1 = (j > 0 ? j - 1 : 0);
							jp1 = (j + 1 == Ny ? j : j + 1);

							// im1 cursors
							fimjmCursor.reset();
							fimjmCursor.jumpFwd(k * Nx * Ny + jm1 * Nx);
							fimjmCursor.fwd();

							fimkmCursor.reset();
							fimkmCursor.jumpFwd(km1 * Nx * Ny + j * Nx);
							fimkmCursor.fwd();

							fimCursor.reset();
							fimCursor.jumpFwd(k * Nx * Ny + j * Nx);
							fimCursor.fwd();

							fimkpCursor.reset();
							fimkpCursor.jumpFwd(kp1 * Nx * Ny + j * Nx);
							fimkpCursor.fwd();

							fimjpCursor.reset();
							fimjpCursor.jumpFwd(k * Nx * Ny + jp1 * Nx);
							fimjpCursor.fwd();

							// i cursors
							fjmkmCursor.reset();
							fjmkmCursor.jumpFwd(km1 * Nx * Ny + jm1 * Nx);
							fjmkmCursor.fwd();

							fjmCursor.reset();
							fjmCursor.jumpFwd(k * Nx * Ny + jm1 * Nx);
							fjmCursor.fwd();

							fjmkpCursor.reset();
							fjmkpCursor.jumpFwd(kp1 * Nx * Ny + jm1 * Nx);
							fjmkpCursor.fwd();

							fkmCursor.reset();
							fkmCursor.jumpFwd(km1 * Nx * Ny + j * Nx);
							fkmCursor.fwd();

							fijkCursor.reset();
							fijkCursor.jumpFwd(k * Nx * Ny + j * Nx);
							fijkCursor.fwd();

							fkpCursor.reset();
							fkpCursor.jumpFwd(kp1 * Nx * Ny + j * Nx);
							fkpCursor.fwd();

							fjpkmCursor.reset();
							fjpkmCursor.jumpFwd(km1 * Nx * Ny + jp1 * Nx);
							fjpkmCursor.fwd();

							fjpCursor.reset();
							fjpCursor.jumpFwd(k * Nx * Ny + jp1 * Nx);
							fjpCursor.fwd();

							// ip1 cursors
							fipjmCursor.reset();
							fipjmCursor.jumpFwd(k * Nx * Ny + jm1 * Nx);
							fipjmCursor.fwd();

							fipkmCursor.reset();
							fipkmCursor.jumpFwd(km1 * Nx * Ny + j * Nx);
							fipkmCursor.fwd();

							fipCursor.reset();
							fipCursor.jumpFwd(k * Nx * Ny + j * Nx);
							fipCursor.fwd();

							for (i = 0; i < Nx; i++) {

								im1 = (i > 0 ? i - 1 : 0);
								ip1 = (i + 1 == Nx ? i : i + 1);

								if (i > 1) {
									fimjmCursor.fwd();
									fimCursor.fwd();
									fimkmCursor.fwd();
									fimkpCursor.fwd();
									fimjpCursor.fwd();
								}

								if (i > 0) {
									fjmkmCursor.fwd();
									fjmCursor.fwd();
									fjmkpCursor.fwd();
									fkmCursor.fwd();
									fijkCursor.fwd();
									fkpCursor.fwd();
									fjpkmCursor.fwd();
									fjpCursor.fwd();
								}

								if (i < Nx - 1) {
									fipjmCursor.fwd();
									fipkmCursor.fwd();
									fipCursor.fwd();
								}

								try {

									fimjm = fimjmCursor.get().getRealFloat();
									fim = fimCursor.get().getRealFloat();
									fimkm = fimkmCursor.get().getRealFloat();
									fimkp = fimkpCursor.get().getRealFloat();
									fimjp = fimjpCursor.get().getRealFloat();
									fjmkm = fjmkmCursor.get().getRealFloat();
									fjm = fjmCursor.get().getRealFloat();
									fjmkp = fjmkpCursor.get().getRealFloat();
									fkm = fkmCursor.get().getRealFloat();
									fijk = fijkCursor.get().getRealFloat();
									fkp = fkpCursor.get().getRealFloat();
									fjpkm = fjpkmCursor.get().getRealFloat();
									fjp = fjpCursor.get().getRealFloat();
									fipjm = fipjmCursor.get().getRealFloat();
									fipkm = fipkmCursor.get().getRealFloat();
									fip = fipCursor.get().getRealFloat();

									Dxpf = (fip - fijk) / hx;
									Dxmf = (fijk - fim) / hx;
									Dypf = (fjp - fijk) / hy;
									Dymf = (fijk - fjm) / hy;
									Dzpf = (fkp - fijk) / hz;
									Dzmf = (fijk - fkm) / hz;
									aijk = hypot3(Dxpf, m(Dypf, Dymf), m(Dzpf, Dzmf));
									bijk = hypot3(Dypf, m(Dxpf, Dxmf), m(Dzpf, Dzmf));
									cijk = hypot3(Dzpf, m(Dypf, Dymf), m(Dxpf, Dxmf));

									aijk = (aijk > FLOAT32_EPS ? Dxpf / aijk : 0.0);
									bijk = (bijk > FLOAT32_EPS ? Dypf / bijk : 0.0);
									cijk = (cijk > FLOAT32_EPS ? Dzpf / cijk : 0.0);

									Dxpf = (fijk - fim) / hx;
									Dypf = (fimjp - fim) / hy;
									Dymf = (fim - fimjm) / hy;
									Dzpf = (fimkp - fim) / hz;
									Dzmf = (fim - fimkm) / hz;
									aim = hypot3(Dxpf, m(Dypf, Dymf), m(Dzpf, Dzmf));

									aim = (aim > FLOAT32_EPS ? Dxpf / aim : 0.0);

									Dxpf = (fipjm - fjm) / hx;
									Dxmf = (fjm - fimjm) / hx;
									Dypf = (fijk - fjm) / hy;
									Dzmf = (fjm - fjmkm) / hz;
									bjm = hypot3(Dypf, m(Dxpf, Dxmf), m(Dzpf, Dzmf));

									bjm = (bjm > FLOAT32_EPS ? Dypf / bjm : 0.0);

									Dxpf = (fipkm - fkm) / hx;
									Dxmf = (fjm - fimkm) / hx;
									Dypf = (fjpkm - fkm) / hy;
									Dymf = (fkm - fjmkm) / hy;
									Dzpf = (fijk - fkm) / hz;
									ckm = hypot3(Dzpf, m(Dypf, Dymf), m(Dxpf, Dxmf));

									ckm = (ckm > FLOAT32_EPS ? Dzpf / ckm : 0.0);

									Dxma = (aijk - aim) / hx;
									Dymb = (bijk - bjm) / hy;
									Dzmc = (cijk - ckm) / hz;

									outRandom.setPosition(new int[] { i, j, k });
									outRandom.get().setReal(Dxma + Dymb + Dzmc);
									// outRandom.get().setReal(1);

								}
								catch (java.lang.ArrayIndexOutOfBoundsException ex) {
									System.out.println("ERROR at: " + i + " " + j + " " + k);
									int stop = 5;
								}

							} // end i
						} // end j
					} // end k
					long totaltime = System.currentTimeMillis() - starttime;

				}// end run
			});
		}

		SimpleMultiThreading.startAndJoin(threads);
	}

	// TODO: replace this function with divide op
	@Override
	protected void inPlaceDivide2(RandomAccessibleInterval<O> denominator,
		RandomAccessibleInterval<O> numeratorOutput)
	{

		final Cursor<O> cursorDenominator = Views.iterable(denominator).cursor();
		final Cursor<O> cursorNumeratorOutput =
			Views.iterable(numeratorOutput).cursor();

		while (cursorDenominator.hasNext()) {
			cursorDenominator.fwd();
			cursorNumeratorOutput.fwd();

			float num = cursorNumeratorOutput.get().getRealFloat();
			float div = cursorDenominator.get().getRealFloat();
			float res = 0;

			if (div > 0) {
				res = num / div;
			}
			else {
				res = 0;
			}

			cursorNumeratorOutput.get().setReal(res);
		}
	}

}
