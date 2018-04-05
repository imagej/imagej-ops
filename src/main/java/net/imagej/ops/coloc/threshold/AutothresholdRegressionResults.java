package net.imagej.ops.coloc.threshold;


public class AutothresholdRegressionResults<T,U> {
	private double warnYInterceptToYMeanRatioThreshold;
	// the slope and and intercept of the regression line
	private double autoThresholdSlope, autoThresholdIntercept;
	/* The thresholds for both image channels. Pixels below a lower
	 * threshold do NOT include the threshold and pixels above an upper
	 * one will NOT either. Pixels "in between (and including)" thresholds
	 * do include the threshold values.
	 */
	private T ch1MinThreshold, ch1MaxThreshold;
	private U ch2MinThreshold, ch2MaxThreshold;
	// additional information
	private double bToYMeanRatio;

	public AutothresholdRegressionResults() {
		warnYInterceptToYMeanRatioThreshold = 0.01;
		autoThresholdSlope = 0.0;
		autoThresholdIntercept = 0.0;
		bToYMeanRatio = 0.0;

	}

	public double getWarnYInterceptToYMeanRatioThreshold() {
		return warnYInterceptToYMeanRatioThreshold;
	}
	public void setWarnYInterceptToYMeanRatioThreshold(double x) {
		warnYInterceptToYMeanRatioThreshold = x;
	}
	public double getAutoThresholdSlope() {
		return autoThresholdSlope;
	}
	public void setAutoThresholdSlope(double x) {
		autoThresholdSlope = x;
	}
	public double getAutoThresholdIntercept() {
		return autoThresholdIntercept;
	}
	public void setAutoThresholdIntercept(double x) {
		autoThresholdIntercept = x;
	}
	public double getBToYMeanRatio() {
		return bToYMeanRatio;
	}
	public void setBToYMeanRatio(double x) {
		bToYMeanRatio = x;
	}
	public T getCh1MinThreshold() {
		return ch1MinThreshold;
	}
	public void setCh1MinThreshold(T x) {
		ch1MinThreshold = x;
	}
	public T getCh1MaxThreshold() {
		return ch1MaxThreshold;
	}
	public void setCh1MaxThreshold(T x) {
		ch1MaxThreshold = x;
	}
	public U getCh2MinThreshold() {
		return ch2MinThreshold;
	}
	public void setCh2MinThreshold(U x) {
		ch2MinThreshold = x;
	}
	public U getCh2MaxThreshold() {
		return ch2MaxThreshold;
	}
	public void setCh2MaxThreshold(U x) {
		ch2MaxThreshold = x;
	}
	
}
