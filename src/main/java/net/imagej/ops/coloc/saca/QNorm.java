
package net.imagej.ops.coloc.saca;

import org.apache.commons.math3.distribution.NormalDistribution;

final class QNorm {

	private QNorm() {}

	public static double compute(final double p) {
		if (p < 0 || p > 1) {
			return Double.NaN;
		}
		if (p == 0 || p == 1) {
			return Double.POSITIVE_INFINITY;
		}

		return compute(p, 0, 1, true, false);
	}

	public static double compute(double p, final double mean, final double sd,
		final boolean lowerTail, final boolean logP)
	{
		final NormalDistribution dist = new NormalDistribution(mean, sd);
		if (logP) p = Math.exp(p);
		final double q = dist.inverseCumulativeProbability(p);
		return lowerTail ? q : -q;
	}
}
