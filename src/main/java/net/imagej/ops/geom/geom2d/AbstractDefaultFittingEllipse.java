package net.imagej.ops.geom.geom2d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Geometric.FittingEllipse;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.roi.EllipseRegionOfInterest;
import net.imglib2.type.numeric.real.DoubleType;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.scijava.plugin.Plugin;

/*
Authors: Andrew Fitzgibbon, Maurizio Pilu, Bob Fisher
Reference: "Direct Least Squares Fitting of Ellipses", IEEE T-PAMI, 1999

 @Article{Fitzgibbon99,
  author = "Fitzgibbon, A.~W.and Pilu, M. and Fisher, R.~B.",
  title = "Direct least-squares fitting of ellipses",
  journal = pami,
  year = 1999,
  volume = 21,
  number = 5,
  month = may,
  pages = "476--480"
 }

This is a more bulletproof version than that in the paper, incorporating
scaling to reduce roundoff error, correction of behaviour when the input 
data are on a perfect hyperbola, and returns the geometric parameters
of the ellipse, rather than the coefficients of the quadratic form.


Copyright (c) 1999, Andrew Fitzgibbon, Maurizio Pilu, Bob Fisher


Permission is hereby granted, free of charge, to any person obtaining a
copy of this software and associated documentation files (the "Software"),
to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
DEALINGS IN THE SOFTWARE.
*/

/**
 * This implementation is based on the Matlab implementation from Andrew
 * Fitzgibbon, Maurizio Pilu, Bob Fisher.
 * 
 * More information can be found here:
 * http://research.microsoft.com/en-us/um/people/awf/ellipse/ellipse-pami.pdf
 * 
 * @author Tim-Oliver buchholz, University of Konstanz
 */
public abstract class AbstractDefaultFittingEllipse<I> extends AbstractUnaryFunctionOp<I, EllipseRegionOfInterest>
		implements FittingEllipse, Contingent {
	
	protected EllipseRegionOfInterest getEllipse(final Iterable<? extends RealLocalizable> it) {
		final DoubleType meanX = new DoubleType(0);
		final DoubleType sx = new DoubleType(0);
		final DoubleType meanY = new DoubleType(0);
		final DoubleType sy = new DoubleType(0);

		final BlockRealMatrix D = buildDesignMatrix(getNormalizedData(it, meanX, sx, meanY, sy));
		final BlockRealMatrix S = D.transpose().multiply(D);
		final BlockRealMatrix C = buildConstraintMatrix();

		final double[] ellipse = convertToGeometricRadiiAndCenter(
				denormalize(solveEigenSystem(S, C), sx.get(), sy.get(), meanX.get(), meanY.get()));

		// Unfortunately we lose the orientation of the ellipse.
		return new EllipseRegionOfInterest(new RealPoint(ellipse[0], ellipse[1]),
				new double[] { ellipse[2], ellipse[3] });
	}

	/**
	 * 
	 * @param par
	 *            the denormalized parameters of ax^2 + bxy + cy^2 + dx + ey + f
	 *            = 0
	 * @return [centerU, centerV, Ru, Rv, thetaRad]
	 */
	private double[] convertToGeometricRadiiAndCenter(double[] par) {
		final double thetaRad = 0.5 * Math.atan2(par[1], par[0] - par[2]);
		final double cost = Math.cos(thetaRad);
		final double sint = Math.sin(thetaRad);
		final double sin_squared = sint * sint;
		final double cos_squared = cost * cost;
		final double cos_sin = sint * cost;

		final double ao = par[5];
		final double au = par[3] * cost + par[4] * sint;
		final double av = -par[3] * sint + par[4] * cost;
		final double auu = par[0] * cos_squared + par[2] * sin_squared + par[1] * cos_sin;
		final double avv = par[0] * sin_squared + par[2] * cos_squared - par[1] * cos_sin;

		final double tuCenter = -au / (2 * auu);
		final double tvCenter = -av / (2 * avv);
		final double wCenter = ao - auu * tuCenter * tuCenter - avv * tvCenter * tvCenter;

		final double uCenter = tuCenter * cost - tvCenter * sint;
		final double vCenter = tuCenter * sint + tvCenter * cost;

		double Ru = -wCenter / auu;
		double Rv = -wCenter / avv;

		Ru = Math.sqrt(Math.abs(Ru)) * Math.signum(Ru);
		Rv = Math.sqrt(Math.abs(Rv)) * Math.signum(Rv);

		return new double[] { uCenter, vCenter, Ru, Rv, thetaRad };
	}

	/**
	 * Denormalize the parameters of the ellipse.
	 * 
	 * @param a
	 *            parameters of ax^2 + bxy + cy^2 + dx + ey + f = 0
	 * @return denormalized parameters of ax^2 + bxy + cy^2 + dx + ey + f = 0
	 */
	private double[] denormalize(final double[] a, final double sx, final double sy, final double mx, final double my) {
		return new double[] { 	a[0] * sy * sy, 
								a[1] * sx * sy, 
								a[2] * sx * sx,
								-2 * a[0] * sy * sy * mx - a[1] * sx * sy * my + a[3] * sx * sy * sy,
								-a[1] * sx * sy * mx - 2 * a[2] * sx * sx * my + a[4] * sx * sx * sy,
								a[0] * sy * sy * mx * mx + a[1] * sx * sy * mx * my + a[2] * sx * sx * my * my
								- a[3] * sx * sy * sy * mx - a[4] * sx * sx * sy * my 
								+ a[5] * sx * sx * sy * sy };
	}

	/**
	 * Solve the Eigen-System to find the parameters of ax^2 + bxy + cy^2 + dx +
	 * ey + f = 0
	 * 
	 * @param s
	 *            scatter matrix
	 * @param c
	 *            constraint matrix (ellipse constraint is b^2 - 4ac < 0)
	 * @return the found parameters [a b c d e f]
	 */
	private double[] solveEigenSystem(BlockRealMatrix s, BlockRealMatrix c) {

		final BlockRealMatrix tmpA = s.getSubMatrix(0, 2, 0, 2);
		final BlockRealMatrix tmpB = s.getSubMatrix(0, 2, 3, 5);
		final BlockRealMatrix tmpC = s.getSubMatrix(3, 5, 3, 5);
		final BlockRealMatrix tmpD = c.getSubMatrix(0, 2, 0, 2);

		final RealMatrix tmpE = MatrixUtils.inverse(tmpC).multiply(tmpB.transpose());

		final EigenDecomposition ed = new EigenDecomposition(
				MatrixUtils.inverse(tmpD).multiply(tmpA.subtract(tmpB.multiply(tmpE))));
		final double[] eval = ed.getRealEigenvalues();
		int idx = -1;
		for (int i = 0; i < eval.length; i++) {
			if (eval[i] < 0 && Double.isFinite(eval[i])) {
				idx = i;
				break;
			}
		}

		final RealVector vec = ed.getEigenvector(idx);

		return vec.append(tmpE.scalarMultiply(-1).operate(vec)).toArray();
	}

	/**
	 * Build constraint matrix for direct ellipse fitting Equality constraint
	 * 4ac - b^2 = 1.
	 * 
	 * @return constraint matrix
	 */
	private BlockRealMatrix buildConstraintMatrix() {
		final BlockRealMatrix c = new BlockRealMatrix(6, 6);
		c.setEntry(0, 2, -2);
		c.setEntry(1, 1, 1);
		c.setEntry(2, 0, -2);
		return c;
	}

	/**
	 * 
	 * @param normalizedInput
	 *            the normalized input points
	 * @return the design matrix
	 */
	private BlockRealMatrix buildDesignMatrix(final List<RealLocalizable> normalizedInput) {

		final BlockRealMatrix d = new BlockRealMatrix(normalizedInput.size(), 6);

		for (int i = 0; i < normalizedInput.size(); i++) {
			final double x = normalizedInput.get(i).getDoublePosition(0);
			final double y = normalizedInput.get(i).getDoublePosition(1);
			d.setRow(i, new double[] { x * x, x * y, y * y, x, y, 1 });
		}

		return d;
	}

	/**
	 * Normalize the data.
	 * 
	 * @param input
	 *            input points
	 * @param meanX
	 *            output
	 * @param sx
	 *            output
	 * @param meanY
	 *            output
	 * @param sy
	 *            output
	 * @return normalizedInput
	 *            the normalizedInput as output
	 */
	private List<RealLocalizable> getNormalizedData(final Iterable<? extends RealLocalizable> input, DoubleType meanX, DoubleType sx, DoubleType meanY, DoubleType sy) {
		final List<RealLocalizable> normalizedInput = new ArrayList<>();
		final Iterator<? extends RealLocalizable> it = input.iterator();
		double minX = 0;
		double mx = 0;
		double maxX = 0;
		double minY = 0;
		double my = 0;
		double maxY = 0;
		double size = 0;
		if (it.hasNext()) {
			RealLocalizable p = it.next();
			minX = mx = maxX = p.getDoublePosition(0);
			minY = my = maxY = p.getDoublePosition(1);
			size++;
		}

		while (it.hasNext()) {
			RealLocalizable p = it.next();
			double pX = p.getDoublePosition(0);
			double pY = p.getDoublePosition(1);

			mx += pX;
			my += pY;

			minX = minX > pX ? pX : minX;
			maxX = maxX < pX ? pX : maxX;

			minY = minY > pY ? pY : minY;
			maxY = maxY < pY ? pY : maxY;
			
			size++;
		}
		sx.set((maxX - minX) / 2);
		sy.set((maxY - minY) / 2);
		meanX.set(mx / size);
		meanY.set(my / size);

		final double xs = sx.get();
		final double ys = sy.get();
		final double xm = meanX.get();
		final double ym = meanY.get();
		input.forEach(p -> {
			normalizedInput.add(new RealPoint((p.getDoublePosition(0) - xm) / xs, (p.getDoublePosition(1) - ym) / ys));
		});

		return normalizedInput;
	}

}
