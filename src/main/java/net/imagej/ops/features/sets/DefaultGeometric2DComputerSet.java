/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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
package net.imagej.ops.features.sets;

import net.imagej.ops.Op;
import net.imagej.ops.Ops.Geometric.BoundarySize;
import net.imagej.ops.Ops.Geometric.Boxivity;
import net.imagej.ops.Ops.Geometric.Circularity;
import net.imagej.ops.Ops.Geometric.Convexity;
import net.imagej.ops.Ops.Geometric.Eccentricity;
import net.imagej.ops.Ops.Geometric.MainElongation;
import net.imagej.ops.Ops.Geometric.MajorAxis;
import net.imagej.ops.Ops.Geometric.MaximumFeretsAngle;
import net.imagej.ops.Ops.Geometric.MaximumFeretsDiameter;
import net.imagej.ops.Ops.Geometric.MinimumFeretsAngle;
import net.imagej.ops.Ops.Geometric.MinimumFeretsDiameter;
import net.imagej.ops.Ops.Geometric.MinorAxis;
import net.imagej.ops.Ops.Geometric.Roundness;
import net.imagej.ops.Ops.Geometric.Size;
import net.imagej.ops.Ops.Geometric.Solidity;
import net.imagej.ops.features.sets.processors.ComputerSetProcessorUtils;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * Default implementation of {@link Geometric2DComputerSet}.
 *
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
@Plugin(type = ComputerSet.class, label = "Geometric 2D Computers")
public class DefaultGeometric2DComputerSet extends AbstractConfigurableComputerSet<Polygon, DoubleType>
		implements Geometric2DComputerSet<Polygon, DoubleType> {

	public DefaultGeometric2DComputerSet() {
		super(new DoubleType(), Polygon.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Op>[] getComputers() {
		return new Class[] { Boxivity.class, Convexity.class, Circularity.class, Eccentricity.class,
				MainElongation.class, MinimumFeretsAngle.class, MaximumFeretsAngle.class, MinimumFeretsDiameter.class,
				MaximumFeretsDiameter.class, MajorAxis.class, MinorAxis.class, BoundarySize.class, Roundness.class,
				Solidity.class, Size.class };
	}

	@Override
	protected Polygon getFakeInput() {
		return ComputerSetProcessorUtils.get2DPolygon();
	}
}
