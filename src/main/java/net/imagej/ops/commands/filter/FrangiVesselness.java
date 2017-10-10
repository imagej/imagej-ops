/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
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

package net.imagej.ops.commands.filter;

import java.util.Arrays;

import net.imagej.ops.OpService;
import net.imglib2.Dimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * This command applied the Frangi Vesselness filter operation onto an image,
 * allowing users to specify:
 * 		1) The scales desired for the filter application
 * 		2) The physical spacing between image data points
 * 		3) Whether or not the pre-Gaussian filter should be performed.
 * 
 * @author Gabe Selzer
 */
@Plugin(type = Command.class, menuPath = "Process>Filters>Frangi Vesselness")
public class FrangiVesselness<T extends RealType<T>> implements Command {

	@Parameter
	private LogService log;

	@Parameter
	private OpService opService;

	@Parameter
	private Img<T> input;

	@Parameter(label = "Gauss before:")
	private boolean doGauss;

	@Parameter(label = "Spacing",
		description = "Physical distance between the observed data points")
	private String spacingString = "1, 1";

	@Parameter(label = "Scale",
		description = "Used as the sigma for the Gaussian filter, and as the scale for the vesselness filter.")
	private String scaleString = "2, 5";

	@Parameter(type = ItemIO.OUTPUT)
	private Img<FloatType> result;

	private double[] spacing;
	private int[] scales;
	private String regex = "(,|\\s)\\s*";

	private double[] checkDimensions(String in, int expectedDims,
		String arrName)
	{
		double[] result = Arrays.stream(in.split(regex)).mapToDouble(
			Double::parseDouble).toArray();
		// chop off the end if there are too many values passed in
		if (result.length > expectedDims) {
			result = Arrays.copyOf(result, input.numDimensions());
			log.warn("Too many values were passed in " + arrName +
				" array. Removing " + (expectedDims - result.length) + " value(s).");
		}
		// add values to the end of the array if there are too few values
		else if (result.length < expectedDims) {
			int actual = result.length;
			result = Arrays.copyOf(result, expectedDims);
			Arrays.fill(result, actual, result.length, result[actual - 1]);
			log.warn("Not enough values were passed in the " + arrName +
				" array. The remaining indices were filled with the last value given.");
		}
		return result;
	}

	@Override
	public void run() {
	//parse the spacing, and scales strings.
		spacing = checkDimensions(spacingString, input.numDimensions(), "Spacings");
		scales = Arrays.stream(scaleString.split(regex)).mapToInt(
				Integer::parseInt).toArray();
		Dimensions resultDims = Views.addDimension(input, 0, scales.length - 1);
		//create output image, potentially-filtered input
		result = opService.create().img(resultDims, new FloatType());

		for (int s = 0; s < scales.length; s++) {
			//Determine whether or not the user would like to apply the gaussian beforehand and do it.
			RandomAccessibleInterval<T> vesselnessInput = doGauss ? opService.filter().gauss(input, scales[s]) : input;
			IntervalView<FloatType> scaleResult = Views.hyperSlice(result, result.numDimensions() - 1, s);
			opService.filter().frangiVesselness(scaleResult, vesselnessInput, spacing, scales[s]);
		}
	}
}
