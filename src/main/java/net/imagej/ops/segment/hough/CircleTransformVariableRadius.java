/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2016 Tobias Pietzsch, Stephan Preibisch, Stephan Saalfeld,
 * John Bogovic, Albert Cardona, Barry DeZonia, Christian Dietz, Jan Funke,
 * Aivar Grislis, Jonathan Hale, Grant Harris, Stefan Helfrich, Mark Hiner,
 * Martin Horn, Steffen Jaensch, Lee Kamentsky, Larry Lindsey, Melissa Linkert,
 * Mark Longair, Brian Northan, Nick Perry, Curtis Rueden, Johannes Schindelin,
 * Jean-Yves Tinevez and Michael Zinsmaier.
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

package net.imagej.ops.segment.hough;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Segment.HoughCircle;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.roi.geom.real.Sphere;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * This op performs the Hough Circle Transform on a Binary RealType Image
 * (typically one that has first gone through edge detection.) This op performs
 * on a set radius on 2-D images.
 * 
 * @author Gabe Selzer
 */
@Plugin(type = HoughCircle.class, priority = Priority.HIGH_PRIORITY)
public class CircleTransformVariableRadius<T extends RealType<T>> extends
AbstractUnaryComputerOp<RandomAccessibleInterval<T>, List<Sphere>> implements
Contingent, Ops.Segment.HoughCircle {

	@Parameter(min = "4", description = "The radius to search for, in pixels.")
	int minRadius;
	
	@Parameter(description = "The maximum radius to search for, in pixels.")
	int maxRadius;
	
	@Parameter(description = "The increase in the radius between runs of the transform")
	int stepRadius = 1;
	
	@Parameter(min = "2",
		description = "The number of points required to consider a circle.")
	int threshold;
	
	@Override
	public void compute(RandomAccessibleInterval<T> input, List<Sphere> output) {
		
		// List of spheres generated during each iteration, added to output at end of iteration.
		List<Sphere> currentOutput;
		
		//loop through radii, call set radius transform at each step, add step output to total output.
		for(int i = minRadius; i <= maxRadius; i += stepRadius) {
			currentOutput = new ArrayList<Sphere>();
			ops().run(net.imagej.ops.segment.hough.CircleTransform.class, currentOutput, input, i, threshold);
			
			for(Sphere s: currentOutput) output.add(s);
		}
	}

	@Override
	public boolean conforms() {
		return (in().numDimensions() == 2);
	}

}
