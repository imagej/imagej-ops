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
package net.imagej.ops.coloc;

/**
 * Try to converge a threshold based on an update value condition. If the
 * update value is larger zero, the threshold is lowered by half the distance
 * between the last thresholds. If the update value falls below zero or is not
 * a number, the threshold is increased by such a half step.
 *
 * @author Tom Kazimiers
 *
 */
public class BisectionStepper extends Stepper {
	protected double threshold1;
	protected double threshold2;
	protected double thrDiff = Double.NaN;
	protected int iterations = 0;
	protected int maxIterations = 100;

	/**
	 * Initialize the bisection stepper with a start threshold and its
	 * last threshold
	 *
	 * @param threshold The current threshold
	 * @param lastThreshold The last threshold
	 */
	public BisectionStepper(double threshold, double lastThreshold) {
		threshold1 = threshold;
		threshold2 = lastThreshold;
		thrDiff = Math.abs(threshold1 - threshold2);
	}

	/**
	 * Update threshold by a bisection step. If {@code value} is below zero or
	 * not a number, the step is made upwards. If it is above zero, the stoep is
	 * downwards.
	 */
	@Override
	public void update(double value) {
		// update working thresholds for next iteration
		threshold2 = threshold1;
		if (Double.NaN == value || value < 0) {
			// we went too far, increase by the absolute half
			threshold1 = threshold1 + thrDiff * 0.5;
		} else if (value > 0) {
			// as long as r > 0 we go half the way down
			threshold1 = threshold1 - thrDiff * 0.5;
		}
		// Update difference to last threshold
		thrDiff = Math.abs(threshold1 - threshold2);
		// Update update counter
		iterations++;
	}

	/**
	 * Get current threshold.
	 */
	@Override
	public double getValue() {
		return threshold1;
	}

	/**
	 * If the difference between both thresholds is &lt; 1, we consider
	 * that as reasonable close to abort the regression.
	 */
	@Override
	public boolean isFinished() {
		return iterations > maxIterations || thrDiff < 1.0;
	}
}
