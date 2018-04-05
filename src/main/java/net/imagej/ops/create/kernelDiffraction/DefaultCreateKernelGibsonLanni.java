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

package net.imagej.ops.create.kernelDiffraction;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imglib2.Dimensions;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ComplexType;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.special.BesselJ;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Creates a Gibson Lanni Kernel.
 *
 * @author Jizhou Li
 * @author Brian Northan
 * @param <T>
 */
@Plugin(type = Ops.Create.KernelDiffraction.class)
public class DefaultCreateKernelGibsonLanni<T extends ComplexType<T> & NativeType<T>>
	extends AbstractUnaryFunctionOp<Dimensions, Img<T>> implements
	Ops.Create.KernelDiffraction
{

	private BinaryFunctionOp<Dimensions, T, Img<T>> createOp;

	// //////// physical parameters /////////////

	@Parameter
	private double NA = 1.4; // numerical aperture

	@Parameter
	private double lambda = 610E-09; // wavelength

	@Parameter
	private double ns = 1.33; // specimen refractive index

	@Parameter
	private double ni = 1.5; // immersion refractive index, experimental

	@Parameter
	private double resLateral = 100E-9; // lateral pixel size

	@Parameter
	private double resAxial = 250E-9; // axial pixel size

	@Parameter
	private double pZ = 2000E-9D; // position of particle

	@Parameter
	// HACK: Must declare class as ComplexType rather than T,
	// to avoid current Ops generics matching limitations.
	// Without this, the ConvertService returns null when
	// attempting to convert an input type instance to T.
	private ComplexType<T> type; // pixel type of created kernel

	private double ng0 = 1.5; // coverslip refractive index, design value
	private double ng = 1.5; // coverslip refractive index, experimental
	private double ni0 = 1.5; // immersion refractive index, design
	private double ti0 = 150E-06; // working distance of the objective,
	// desig
	// a bit precision lost if use 170*1.0E-6
	private double tg0 = 170E-6; // coverslip thickness, design value
	private double tg = 170E-06; // coverslip thickness, experimental value

	// ////////approximation parameters /////////////
	private int numBasis = 100; // number of basis functions
	private int numSamp = 1000; // number of sampling
	private int overSampling = 2; // overSampling

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		createOp = (BinaryFunctionOp) Functions.binary(ops(), Ops.Create.Img.class,
			Img.class, Dimensions.class, NativeType.class);

		ni0 = ni;
	}

	@Override
	public Img<T> calculate(Dimensions size) {
		int nx = -1; // psf size
		int ny = -1;
		int nz = -1;

		if (size.numDimensions() == 2) {
			nx = (int) size.dimension(0);
			ny = (int) size.dimension(1);
			nz = 1;
		}
		else if (size.numDimensions() == 3) {
			nx = (int) size.dimension(0);
			ny = (int) size.dimension(1);
			nz = (int) size.dimension(2);
		}

		// compute the distance between the particle position (pZ) and the center
		int distanceFromCenter = (int) Math.abs(Math.ceil(pZ / resAxial));

		// increase z size so that the PSF is large enough so that we can later
		// recrop a centered psf
		nz = nz + 2 * distanceFromCenter;

		double x0 = (nx - 1) / 2.0D;
		double y0 = (ny - 1) / 2.0D;

		double xp = x0;
		double yp = y0;

		int maxRadius = (int) Math.round(Math.sqrt((nx - x0) * (nx - x0) + (ny -
			y0) * (ny - y0))) + 1;
		double[] r = new double[maxRadius * this.overSampling];
		double[][] h = new double[nz][r.length];

		double a = 0.0D;
		double b = Math.min(1.0D, this.ns / this.NA);

		double k0 = 2 * Math.PI / this.lambda;
		double factor1 = 545 * 1.0E-9 / this.lambda;
		double factor = factor1 * this.NA / 1.4;
		double deltaRho = (b - a) / (this.numSamp - 1);

		// basis construction
		double rho = 0.0D;
		double am = 0.0;
		double[][] Basis = new double[this.numSamp][this.numBasis];

		BesselJ bj0 = new BesselJ(0);
		BesselJ bj1 = new BesselJ(1);

		for (int m = 0; m < this.numBasis; m++) {
			// am = (3 * m + 1) * factor;
			am = (3 * m + 1);
			for (int rhoi = 0; rhoi < this.numSamp; rhoi++) {
				rho = rhoi * deltaRho;
				Basis[rhoi][m] = bj0.value(am * rho);
			}
		}

		// compute the function to be approximated

		double ti = 0.0D;
		double OPD = 0;
		double W = 0;

		double[][] Coef = new double[nz][this.numBasis * 2];
		double[][] Ffun = new double[this.numSamp][nz * 2];

		double rhoNA2;

		for (int z = 0; z < nz; z++) {
			ti = (this.ti0 + this.resAxial * (z - (nz - 1.0D) / 2.0D));

			for (int rhoi = 0; rhoi < this.numSamp; rhoi++) {
				rho = rhoi * deltaRho;
				rhoNA2 = rho * rho * this.NA * this.NA;

				OPD = this.pZ * Math.sqrt(this.ns * this.ns - rhoNA2);
				OPD += this.tg * Math.sqrt(this.ng * this.ng - rhoNA2) - this.tg0 * Math
					.sqrt(this.ng0 * this.ng0 - rhoNA2);
				OPD += ti * Math.sqrt(this.ni * this.ni - rhoNA2) - this.ti0 * Math
					.sqrt(this.ni0 * this.ni0 - rhoNA2);

				W = k0 * OPD;

				Ffun[rhoi][z] = Math.cos(W);
				Ffun[rhoi][z + nz] = Math.sin(W);
			}
		}

		// solve the linear system
		// begin....... (Using Common Math)

		RealMatrix coefficients = new Array2DRowRealMatrix(Basis, false);
		RealMatrix rhsFun = new Array2DRowRealMatrix(Ffun, false);
		DecompositionSolver solver = new SingularValueDecomposition(coefficients)
			.getSolver(); // slower
		// but
		// more
		// accurate
		// DecompositionSolver solver = new
		// QRDecomposition(coefficients).getSolver(); // faster, less accurate

		RealMatrix solution = solver.solve(rhsFun);
		Coef = solution.getData();

		// end.......

		double[][] RM = new double[this.numBasis][r.length];
		double beta = 0.0D;

		double rm = 0.0D;
		for (int n = 0; n < r.length; n++) {
			r[n] = (n * 1.0 / this.overSampling);
			beta = k0 * this.NA * r[n] * this.resLateral;

			for (int m = 0; m < this.numBasis; m++) {
				// am = (3 * m + 1) * factor;
				am = (3 * m + 1);
				rm = am * bj1.value(am * b) * bj0.value(beta * b) * b;
				rm = rm - beta * b * bj0.value(am * b) * bj1.value(beta * b);
				RM[m][n] = rm / (am * am - beta * beta);

			}
		}

		// obtain one component
		double maxValue = 0.0D;
		for (int z = 0; z < nz; z++) {
			for (int n = 0; n < r.length; n++) {
				double realh = 0.0D;
				double imgh = 0.0D;
				for (int m = 0; m < this.numBasis; m++) {
					realh = realh + RM[m][n] * Coef[m][z];
					imgh = imgh + RM[m][n] * Coef[m][z + nz];

				}
				h[z][n] = realh * realh + imgh * imgh;
			}
		}

		// assign

		double[][] Pixel = new double[nz][nx * ny];

		for (int z = 0; z < nz; z++) {

			for (int x = 0; x < nx; x++) {
				for (int y = 0; y < ny; y++) {
					double rPixel = Math.sqrt((x - xp) * (x - xp) + (y - yp) * (y - yp));
					int index = (int) Math.floor(rPixel * this.overSampling);

					double value = h[z][index] + (h[z][(index + 1)] - h[z][index]) *
						(rPixel - r[index]) * this.overSampling;
					Pixel[z][(x + nx * y)] = value;
					if (value > maxValue) {
						maxValue = value;
					}
				}
			}
			//

		}

		// create an RAI to store the PSF
		@SuppressWarnings("unchecked")
		final Img<T> psf3d = createOp.calculate(size, (T) type);

		// use a RandomAccess to access pixels
		RandomAccess<T> ra = psf3d.randomAccess();

		int start, finish;

		// if the particle position, pZ is negative crop the bottom part of the
		// larger PSF
		if (pZ < 0) {
			start = 2 * distanceFromCenter;
			finish = nz;
		}
		// if the particle position, pZ is positive crop the top part of the larger
		// PSF
		else {
			start = 0;
			finish = nz - 2 * distanceFromCenter;
		}

		// loop and copy pixel values from the PSF array to the output PSF RAI
		for (int z = start; z < finish; z++) {

			for (int x = 0; x < nx; x++) {
				for (int y = 0; y < ny; y++) {

					double value = Pixel[z][(x + nx * y)] / maxValue;

					ra.setPosition(new int[] { x, y, z - start });
					ra.get().setReal(value);
				}
			}
		}

		return psf3d;
	}

}
