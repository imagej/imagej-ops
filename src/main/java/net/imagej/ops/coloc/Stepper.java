package net.imagej.ops.coloc;

/**
 * A stepper is used to change some value with every update() call. It should
 * finish at some point in time.
 *
 * @author Tom Kazimiers
 */
public abstract class Stepper {
	public abstract void update(double value);
	public abstract double getValue();
	public abstract boolean isFinished();
}
