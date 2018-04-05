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

package net.imagej.ops.threshold.rosin;

import net.imagej.ops.Ops;
import net.imagej.ops.threshold.AbstractComputeThresholdHistogram;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Implements Rosin's threshold method. From : Rosin, Paul L. "Unimodal
 * thresholding." Pattern recognition 34.11 (2001): 2083-2096.
 * <p>
 * Implementation inspired/copied from
 * <a href="http://users.cs.cf.ac.uk/Paul.Rosin/resources/unimodal/">Paul Rosin
 * C implementation</a>.
 * </p>
 *
 * @author Hadrien Mary
 */
@Plugin(type = Ops.Threshold.Rosin.class, priority = Priority.HIGH)
public class ComputeRosinThreshold<T extends RealType<T>> extends
	AbstractComputeThresholdHistogram<T> implements Ops.Threshold.Rosin
{

	@Parameter
	private LogService log;

	@Override
	public long computeBin(final Histogram1d<T> histo) {
		final long[] hist = histo.toLongArray();

		final int noPts = hist.length;
		int pk, st, fi, d1, d2;
		long tmp;
		int i;
		int thresh;
		boolean doInvert = false;

		pk = findStart(hist, noPts);
		st = findFirst(hist, noPts);
		fi = findEnd(hist, noPts);
		d1 = pk - st;
		d2 = fi - pk;

		if ((d1 < 0) || (d2 < 0)) {
			log.warn("Histogram peak at a strange location");
		}

		if (d1 > d2) {
			doInvert = true;
		}

		if (doInvert) {
			for (i = 0; i < noPts / 2; i++) {
				tmp = hist[i];
				hist[i] = hist[noPts - 1 - i];
				hist[noPts - 1 - i] = tmp;
			}
		}

		st = findStart(hist, noPts);

		thresh = findCorner(hist, st, noPts);

		if (doInvert) {
			thresh = noPts - thresh - 1;
		}

		return thresh;
	}

	private int findStart(final long[] Y, final int noPts) {
		int i, st;
		long max;

		// Find largest peak
		st = 0;
		max = Y[0];
		for (i = 1; i < noPts; i++) {
			if (Y[i] > max) {
				max = Y[i];
				st = i;
			}
		}

		return st;
	}

	private int findFirst(final long[] Y, final int noPts) {
		int i, st;

		st = 0;
		for (i = 0; i < noPts; i++) {
			if (Y[i] > 0) {
				st = i;
				break;
			}
		}

		return st;
	}

	private int findEnd(final long[] Y, final int noPts) {
		int i, fi;

		fi = 0;
		for (i = 1; i < noPts; i++) {
			if (Y[i] > 0) {
				fi = i;
			}
		}

		return fi;
	}

	private int findCorner(final long[] Y, final int st, int noPts) {
		final long[] X = new long[noPts];
		int i;
		float dist;
		float maxDist = -1;
		int thresh = -1;
		int end;

		for (i = st; i < noPts; i++) {
			X[i] = i;
		}

		end = noPts - 1;
		while ((Y[end] == 0) && (end >= 0)) {
			end--;
		}
		noPts = end;
		if (end <= 0) {
			throw new IllegalStateException("Histogram is empty.");
		}

		for (i = st; i <= noPts; i++) {
			dist = (Y[st] - Y[noPts - 1]) * X[i] //
				- (X[st] - X[noPts - 1]) * Y[i] //
				- (X[noPts - 1] * Y[st]) //
				+ (X[st] * Y[noPts - 1]);
			dist = (float) (Math.pow(dist, 2) / (Math.pow(X[st] - X[noPts - 1], 2) +
				Math.pow(Y[st] - Y[noPts - 1], 2)));
			dist = Math.abs(dist);

			if (dist > maxDist) {
				maxDist = dist;
				thresh = i;
			}
		}

		return thresh;
	}
}
