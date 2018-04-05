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

import java.util.concurrent.atomic.AtomicInteger;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.multithreading.SimpleMultiThreading;
import net.imglib2.type.NativeType;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Implements update step for Richardson-Lucy algorithm with total variation
 * regularization for 3D confocal microscope deconvolution Microsc Res Rech 2006
 * Apr; 69(4)- 260-6 The div_unit_grad function has been adapted from IOCBIOS,
 * Pearu Peterson https://code.google.com/p/iocbio/
 * 
 * @author Brian Northan
 * @param <I> TODO Documentation
 * @param <T> TODO Documentation
 */
@Plugin(type = Ops.Deconvolve.RichardsonLucyUpdate.class,
	priority = Priority.HIGH)
public class RichardsonLucyTVUpdate<T extends RealType<T> & NativeType<T>, I extends RandomAccessibleInterval<T>>
	extends AbstractUnaryComputerOp<I, I> implements
	Ops.Deconvolve.RichardsonLucyUpdate
{

	@Parameter
	private float regularizationFactor;

	@Parameter(required = false)
	private RandomAccessibleInterval<T> variation;

	/**
	 * performs update step of the Richardson Lucy with Total Variation Algorithm
	 */
	@Override
	public void compute(I correction, I estimate) {

		if (variation == null) {
			Type<T> type = Util.getTypeFromInterval(correction);

			variation = ops().create().img(correction, type.createVariable());
		}

		divUnitGradFastThread(estimate);

		final Cursor<T> cursorCorrection = Views.iterable(correction).cursor();

		final Cursor<T> cursorVariation = Views.iterable(variation).cursor();

		final Cursor<T> cursorEstimate = Views.iterable(estimate).cursor();

		while (cursorEstimate.hasNext()) {
			cursorCorrection.fwd();
			cursorVariation.fwd();
			cursorEstimate.fwd();

			cursorEstimate.get().mul(cursorCorrection.get());
			cursorEstimate.get().mul(1f / (1f - regularizationFactor * cursorVariation
				.get().getRealFloat()));
		}

	}

	static double hypot3(double a, double b, double c) {
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
	void divUnitGradFastThread(RandomAccessibleInterval<T> estimate) {
		final int Nx, Ny, Nz;

		Nx = (int) estimate.dimension(0);
		Ny = (int) estimate.dimension(1);

		if (estimate.numDimensions() > 2) {
			Nz = (int) estimate.dimension(2);
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

					final RandomAccess<T> outRandom = variation.randomAccess();
					final Cursor<T> outCursor = Views.iterable(variation).cursor();

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

					int i, j, k, jm1, jp1, km1, kp1;

					double hx, hy, hz;

					double fip, fim, fjp, fjm, fkp, fkm, fijk;
					double fimkm, fipkm, fjmkm, fjpkm, fimjm, fipjm, fimkp, fimjp;
					double aim, bjm, ckm, aijk, bijk, cijk;
					double Dxpf, Dxmf, Dypf, Dymf, Dzpf, Dzmf;
					double Dxma, Dymb, Dzmc;

					hx = 1;
					hy = 1;
					hz = 3;
					// i minus 1 cursors
					Cursor<T> fimjmCursor = Views.iterable(estimate).cursor();
					Cursor<T> fimCursor = Views.iterable(estimate).cursor();
					Cursor<T> fimkmCursor = Views.iterable(estimate).cursor();
					Cursor<T> fimkpCursor = Views.iterable(estimate).cursor();
					Cursor<T> fimjpCursor = Views.iterable(estimate).cursor();

					// i cursors
					Cursor<T> fjmkmCursor = Views.iterable(estimate).cursor();
					Cursor<T> fjmCursor = Views.iterable(estimate).cursor();
					Cursor<T> fjmkpCursor = Views.iterable(estimate).cursor();
					Cursor<T> fkmCursor = Views.iterable(estimate).cursor();
					Cursor<T> fijkCursor = Views.iterable(estimate).cursor();
					Cursor<T> fkpCursor = Views.iterable(estimate).cursor();
					Cursor<T> fjpkmCursor = Views.iterable(estimate).cursor();
					Cursor<T> fjpCursor = Views.iterable(estimate).cursor();

					// i plus 1 cursors
					Cursor<T> fipjmCursor = Views.iterable(estimate).cursor();
					Cursor<T> fipkmCursor = Views.iterable(estimate).cursor();
					Cursor<T> fipCursor = Views.iterable(estimate).cursor();

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

							outCursor.reset();
							outCursor.jumpFwd(k * Nx * Ny + j * Nx);
							outCursor.fwd();

							for (i = 0; i < Nx; i++) {

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
									outRandom.setPosition(new int[] { i, j, k });

									fijkCursor.fwd();
									fkpCursor.fwd();
									fjpkmCursor.fwd();
									fjpCursor.fwd();

									outCursor.fwd();
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

									outCursor.get().setReal(Dxma + Dymb + Dzmc);

								}
								catch (final ArrayIndexOutOfBoundsException ex) {}

							} // end i
						} // end j
					} // end k

				}// end run
			});
		}

		SimpleMultiThreading.startAndJoin(threads);
	}

}
