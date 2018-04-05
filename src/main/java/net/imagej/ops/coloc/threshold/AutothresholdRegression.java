/*-
 * #%L
 * Fiji's plugin for colocalization analysis.
 * %%
 * Copyright (C) 2009 - 2017 Fiji developers.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

package net.imagej.ops.coloc.threshold;

import net.imagej.ops.Ops;
import net.imagej.ops.coloc.BisectionStepper;
import net.imagej.ops.coloc.ChannelMapper;
import net.imagej.ops.coloc.Stepper;
import net.imagej.ops.coloc.pearsons.PearsonsResult;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.Hybrids;
import net.imagej.ops.special.hybrid.UnaryHybridCF;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.IterablePair;
import net.imglib2.util.Pair;

import org.scijava.plugin.Plugin;



/**
 * A class implementing the automatic finding of a threshold used for Pearson
 * and Manders colocalisation calculations using the Bisection stepper
 * implementation.
 *
 * @author Ellen T Arena
 */
@Plugin(type = Ops.Coloc.Threshold.class)
public class AutothresholdRegression<T extends RealType<T>, U extends RealType<U>>
	extends
	AbstractBinaryFunctionOp<Iterable<T>, Iterable<U>, AutothresholdRegressionResults<T,U>>
	implements Ops.Coloc.Threshold
{

	private UnaryHybridCF<Iterable<T>, T> ch1MeanOp;
	private UnaryHybridCF<Iterable<U>, U> ch2MeanOp;
	private UnaryFunctionOp<Iterable<T>, Pair<T, T>> ch1MinMaxOp;
	private UnaryFunctionOp<Iterable<U>, Pair<U, U>> ch2MinMaxOp;

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initialize() {
		ch1MeanOp = (UnaryHybridCF) Hybrids.unaryCF(ops(), Ops.Stats.Mean.class,
			in1().iterator().next().getClass(), in1());
		ch2MeanOp = (UnaryHybridCF) Hybrids.unaryCF(ops(), Ops.Stats.Mean.class,
			in2().iterator().next().getClass(), in2());
		ch1MinMaxOp = (UnaryFunctionOp) Functions.unary(ops(), Ops.Stats.MinMax.class,
			Pair.class, in1());
		ch2MinMaxOp = (UnaryFunctionOp) Functions.unary(ops(), Ops.Stats.MinMax.class,
			Pair.class, in2());
	}

	@Override
	public AutothresholdRegressionResults<T,U> calculate(final Iterable<T> image1,
		final Iterable<U> image2)
	{
		AutothresholdRegressionResults<T,U> results =
			new AutothresholdRegressionResults<T,U>();
		final Iterable<Pair<T, U>> samples = new IterablePair<>(image1, image2);

		final double ch1Mean = ch1MeanOp.calculate(image1).getRealDouble();
		final double ch2Mean = ch2MeanOp.calculate(image2).getRealDouble();

		final Pair<T, T> ch1minMaxT = ch1MinMaxOp.calculate(image1);
		final double ch1Min = ch1minMaxT.getA().getRealDouble();
		final double ch1Max = ch1minMaxT.getB().getRealDouble();
		final Pair<U, U> ch2minMaxU = ch2MinMaxOp.calculate(image2);
		double ch2Min = ch2minMaxU.getA().getRealDouble();
		double ch2Max = ch2minMaxU.getB().getRealDouble();

		final double combinedMean = ch1Mean + ch2Mean;

		// variables for summing up the
		double ch1MeanDiffSum = 0.0, ch2MeanDiffSum = 0.0, combinedMeanDiffSum =
			0.0;
		double combinedSum = 0.0;
		int N = 0, NZero = 0;

		for (final Pair<T, U> sample : samples) {
			// reference image data type
			final T type = sample.getA();

			final double ch1 = sample.getA().getRealDouble();
			final double ch2 = sample.getB().getRealDouble();

			combinedSum = ch1 + ch2;

			// TODO: Shouldn't the whole calculation take only pixels
			// into account that are combined above zero? And not just
			// the denominator (like it is done now)?

			// calculate the numerators for the variances
			ch1MeanDiffSum += (ch1 - ch1Mean) * (ch1 - ch1Mean);
			ch2MeanDiffSum += (ch2 - ch2Mean) * (ch2 - ch2Mean);
			combinedMeanDiffSum += (combinedSum - combinedMean) * (combinedSum -
				combinedMean);

			// count only pixels that are above zero
			if ((ch1 + ch2) > 0.00001) NZero++;

			N++;
		}

		final double ch1Variance = ch1MeanDiffSum / (N - 1);
		final double ch2Variance = ch2MeanDiffSum / (N - 1);
		final double combinedVariance = combinedMeanDiffSum / (N - 1.0);

		// http://mathworld.wolfram.com/Covariance.html
		// ?2 = X2?(X)2
		// = E[X2]?(E[X])2
		// var (x+y) = var(x)+var(y)+2(covar(x,y));
		// 2(covar(x,y)) = var(x+y) - var(x)-var(y);

		final double ch1ch2Covariance = 0.5 * (combinedVariance - (ch1Variance +
			ch2Variance));

		// calculate regression parameters
		final double denom = 2 * ch1ch2Covariance;
		final double num = ch2Variance - ch1Variance + Math.sqrt((ch2Variance -
			ch1Variance) * (ch2Variance - ch1Variance) + (4 * ch1ch2Covariance *
				ch1ch2Covariance));

		final double m = num / denom;
		final double b = ch2Mean - m * ch1Mean;

		// A stepper that walks thresholds
		Stepper stepper;
		// to map working thresholds to channels
		ChannelMapper mapper;

		// let working threshold walk on channel one if the regression line
		// leans more towards the abscissa (which represents channel one) for
		// positive and negative correlation.
		if (m > -1 && m < 1.0) {
			// Map working threshold to channel one (because channel one has a
			// larger maximum value.
			mapper = new ChannelMapper() {

				@Override
				public double getCh1Threshold(final double t) {
					return t;
				}

				@Override
				public double getCh2Threshold(final double t) {
					return (t * m) + b;
				}
			};
			// Stepper ...
			// Start at the midpoint of channel one
			stepper = new BisectionStepper(Math.abs(ch1Max + ch1Min) * 0.5, ch1Max);

		}
		else {
			// Map working threshold to channel two (because channel two has a
			// larger maximum value.
			mapper = new ChannelMapper() {

				@Override
				public double getCh1Threshold(final double t) {
					return (t - b) / m;
				}

				@Override
				public double getCh2Threshold(final double t) {
					return t;
				}
			};
			// Stepper ...
			// Start at the midpoint of channel two
			stepper = new BisectionStepper(Math.abs(ch2Max + ch2Min) * 0.5, ch2Max);
		}

		// Min threshold not yet implemented
		double ch1ThreshMax = ch1Max;
		double ch2ThreshMax = ch2Max;
		
		// extract sampling types of images
		T type1 = image1.iterator().next();
		U type2 = image2.iterator().next();

		// define some image type specific threshold variables
		final T thresholdCh1 = type1.createVariable();
		final U thresholdCh2 = type2.createVariable();

		/* Get min and max value of image data type. Since type of image
		 * one and two are the same, we dont't need to distinguish them.
		 */

		T minVal1 = type1.createVariable();
		minVal1.setReal(type1.getMinValue());
		T maxVal1 = type1.createVariable();
		maxVal1.setReal(type1.getMaxValue());
		U minVal2 = type2.createVariable();
		minVal2.setReal(type2.getMinValue());
		U maxVal2 = type2.createVariable();
		maxVal2.setReal(type2.getMaxValue());

		// do regression
		while (!stepper.isFinished()) {
			// round ch1 threshold and compute ch2 threshold
			ch1ThreshMax = Math.round(mapper.getCh1Threshold(stepper.getValue()));
			ch2ThreshMax = Math.round(mapper.getCh2Threshold(stepper.getValue()));
			/* Make sure we don't get overflow the image type specific threshold variables
			 * if the image data type doesn't support this value.
			 */
			thresholdCh1.set(clamp(ch1ThreshMax, minVal1, maxVal1));
			thresholdCh2.set(clamp(ch2ThreshMax, minVal2, maxVal2));

			// do pearsonsFast calculation within the limits
			final PearsonsResult currentPersonsR = (PearsonsResult) ops().run("pearsons", image1, image2, thresholdCh1, thresholdCh2);  ////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//pearsonsCorrellation.calculatePearsons(cursor, ch1Mean, ch2Mean, thresholdCh1, thresholdCh2, ThresholdMode.Below);
			stepper.update(currentPersonsR.correlationValue);
		}

		/* Store the new results. The lower thresholds are the types
		 * min value for now. For the max threshold we do a clipping  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		 * to make it fit into the image type.
		 */
		results.setCh1MinThreshold(minVal1);

		results.setCh1MaxThreshold(clamp(ch1ThreshMax, minVal1, maxVal1));

		results.setCh2MinThreshold(minVal2);

		results.setCh2MaxThreshold(clamp(ch2ThreshMax, minVal2, maxVal2));

		results.setAutoThresholdSlope(m);
		results.setAutoThresholdIntercept(b);
		results.setBToYMeanRatio(b / ch2Mean);

		return results;
	}

	/**
	 * Clamp a value to a min or max value. If the value is below min, min is
	 * returned. Accordingly, max is returned if the value is larger. If it is
	 * neither, the value itself is returned.
	 * @param <V>
	 */
	public <V extends RealType<V>> V clamp(double val, V minimum, V maximum)
	{
		V v = minimum.createVariable();
		v.setReal(val);
		return minimum.compareTo(v) > 0 ? minimum : maximum.compareTo(v) < 0 ? maximum : v;
	}
}
