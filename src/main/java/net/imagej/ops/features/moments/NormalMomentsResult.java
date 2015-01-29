package net.imagej.ops.features.moments;

public class NormalMomentsResult {

	private double moment00;
	private double moment01;
	private double moment10;
	private double moment11;

	public void addToMoment00(double x, double y, double val) {
		moment00 += (Math.pow(x, 0) * Math.pow(y, 0) * val);
	}

	public void addToMoment01(double x, double y, double val) {
		moment01 += (Math.pow(x, 0) * Math.pow(y, 1) * val);
	}

	public void addToMoment10(double x, double y, double val) {
		moment10 += (Math.pow(x, 1) * Math.pow(y, 0) * val);
	}

	public void addToMoment11(double x, double y, double val) {
		moment11 += (Math.pow(x, 1) * Math.pow(y, 1) * val);
	}

	public double getMoment00() {
		return moment00;
	}

	public double getMoment01() {
		return moment01;
	}

	public double getMoment10() {
		return moment10;
	}

	public double getMoment11() {
		return moment11;
	}

}
