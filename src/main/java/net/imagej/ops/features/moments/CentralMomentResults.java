package net.imagej.ops.features.moments;

public class CentralMomentResults {

	private double centralMoment00;
	private double centralMoment01;
	private double centralMoment10;

	private double centralMoment11;
	private double centralMoment20;
	private double centralMoment02;
	private double centralMoment21;
	private double centralMoment12;
	private double centralMoment30;
	private double centralMoment03;

	public void addToCentralMoment20(double x, double y, double val) {
		centralMoment20 += val * Math.pow(x, 2) * Math.pow(y, 0);
	}

	public void addToCentralMoment02(double x, double y, double val) {
		centralMoment02 += val * Math.pow(x, 0) * Math.pow(y, 2);
	}

	public void addToCentralMoment21(double x, double y, double val) {
		centralMoment21 += val * Math.pow(x, 2) * Math.pow(y, 1);
	}

	public void addToCentralMoment12(double x, double y, double val) {
		centralMoment12 += val * Math.pow(x, 1) * Math.pow(y, 2);
	}

	public void addToCentralMoment30(double x, double y, double val) {
		centralMoment30 += val * Math.pow(x, 3) * Math.pow(y, 0);
	}

	public void addToCentralMoment03(double x, double y, double val) {
		centralMoment03 += val * Math.pow(x, 0) * Math.pow(y, 3);
	}

	public double getCentralMoment00() {
		return centralMoment00;
	}

	public double getCentralMoment01() {
		return centralMoment01;
	}

	public double getCentralMoment10() {
		return centralMoment10;
	}

	public double getCentralMoment11() {
		return centralMoment11;
	}

	public double getCentralMoment20() {
		return centralMoment20;
	}

	public double getCentralMoment02() {
		return centralMoment02;
	}

	public double getCentralMoment21() {
		return centralMoment21;
	}

	public double getCentralMoment12() {
		return centralMoment12;
	}

	public double getCentralMoment30() {
		return centralMoment30;
	}

	public double getCentralMoment03() {
		return centralMoment03;
	}

	public void setCentralMoment00(double centralMoment00) {
		this.centralMoment00 = centralMoment00;
	}

	public void setCentralMoment01(double centralMoment01) {
		this.centralMoment01 = centralMoment01;
	}

	public void setCentralMoment10(double centralMoment10) {
		this.centralMoment10 = centralMoment10;
	}

	public void setCentralMoment11(double moment11) {
		this.centralMoment11 = moment11;
	}
}
