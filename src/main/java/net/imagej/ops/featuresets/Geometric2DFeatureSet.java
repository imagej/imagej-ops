/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
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

package net.imagej.ops.featuresets;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.scijava.plugin.Plugin;

import net.imagej.ops.FunctionOp;
import net.imagej.ops.OpRef;
import net.imagej.ops.Ops.Geometric.BoundarySize;
import net.imagej.ops.Ops.Geometric.Boxivity;
import net.imagej.ops.Ops.Geometric.Circularity;
import net.imagej.ops.Ops.Geometric.Contour;
import net.imagej.ops.Ops.Geometric.Convexity;
import net.imagej.ops.Ops.Geometric.Eccentricity;
import net.imagej.ops.Ops.Geometric.FeretsAngle;
import net.imagej.ops.Ops.Geometric.FeretsDiameter;
import net.imagej.ops.Ops.Geometric.MainElongation;
import net.imagej.ops.Ops.Geometric.MajorAxis;
import net.imagej.ops.Ops.Geometric.MinorAxis;
import net.imagej.ops.Ops.Geometric.Roundness;
import net.imagej.ops.Ops.Geometric.Rugosity;
import net.imagej.ops.Ops.Geometric.Size;
import net.imagej.ops.Ops.Geometric.Solidity;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.type.numeric.RealType;

/**
 * {@link FeatureSet} to calculate {@link AbstractOpRefFeatureSet<I, O>}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 * @param <I>
 * @param <O>
 */
@Plugin(type = FeatureSet.class, label = "2D Geometric Features",
	description = "Calculates the 2D Geometric Features")
public class Geometric2DFeatureSet<L, O extends RealType<O>> extends
	AbstractOpRefFeatureSet<LabelRegion<L>, O> implements
	DimensionBoundFeatureSet<LabelRegion<L>, O>
{

	private FunctionOp<LabelRegion<L>, Polygon> contourFunc;

	@Override
	protected Collection<? extends OpRef<?>> initOpRefs() {
		final Set<OpRef<?>> refs = new HashSet<OpRef<?>>();

		refs.add(ref(Size.class));
		refs.add(ref(Circularity.class));
		refs.add(ref(Convexity.class));
		refs.add(ref(Eccentricity.class));
		refs.add(ref(MainElongation.class));
		refs.add(ref(FeretsAngle.class));
		refs.add(ref(FeretsDiameter.class));
		refs.add(ref(MajorAxis.class));
		refs.add(ref(MinorAxis.class));
		refs.add(ref(BoundarySize.class));
		refs.add(ref(Boxivity.class));
		refs.add(ref(Roundness.class));
		refs.add(ref(Rugosity.class));
		refs.add(ref(Solidity.class));

		return refs;
	}

	@Override
	protected O evalFunction(final FunctionOp<Object, ? extends O> func,
		final LabelRegion<L> input)
	{
		if (contourFunc == null) {
			contourFunc = ops().function(Contour.class, Polygon.class, in(), true,
				true);
		}

		Polygon contour = contourFunc.compute(input);
		return func.compute(contour);
	}

	@Override
	public int getMinDimensions() {
		return 2;
	}

	@Override
	public int getMaxDimensions() {
		return 2;
	}

	@Override
	public boolean conforms() {
		return in().numDimensions() == 2;
	}

}
