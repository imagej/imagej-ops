package net.imagej.ops.scalepixel;

/**
 * @author Christian Dietz
 */
public class ScaleUtils {

	/**
	 * Determines the factor to map the interval [oldMin, oldMax] to
	 * [newMin,newMax].
	 * 
	 * @param oldMin
	 * @param oldMax
	 * @param newMin
	 * @param newMax
	 * @return the normalization factor
	 */
	public static double calculateFactor(double oldMin, double oldMax,
			double newMin, double newMax) {
		return 1.0d / (oldMax - oldMin) * ((newMax - newMin));
	}
}
