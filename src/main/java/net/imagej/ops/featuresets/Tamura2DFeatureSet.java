/**#%L*ImageJ software for multidimensional image processing and analysis.*%%*Copyright(C)2014-2015 Board of Regents of the University of*Wisconsin-Madison,University of Konstanz and Brian Northan.*%%*Redistribution and use in source and binary forms,with or without*modification,are permitted provided that the following conditions are met:**1. Redistributions of source code must retain the above copyright notice,*this list of conditions and the following disclaimer.*2. Redistributions in binary form must reproduce the above copyright/notice,*this list of conditions and the following disclaimer in the documentation*and/or other materials provided with the distribution.**THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS"AS IS"*AND ANY EXPRESS OR IMPLIED WARRANTIES,INCLUDING,BUT NOT LIMITED TO,THE*IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE*ARE DISCLAIMED.IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE*LIABLE FOR ANY DIRECT,INDIRECT,INCIDENTAL,SPECIAL,EXEMPLARY,OR*CONSEQUENTIAL DAMAGES(INCLUDING,BUT NOT LIMITED TO,PROCUREMENT OF*SUBSTITUTE GOODS OR SERVICES;LOSS OF USE,DATA,OR PROFITS;OR BUSINESS*INTERRUPTION)HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,WHETHER IN*CONTRACT,STRICT LIABILITY,OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE)*ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,EVEN IF ADVISED OF THE*POSSIBILITY OF SUCH DAMAGE.*#L%*/

package net.imagej.ops.featuresets;

import java.util.Collection;
import java.util.HashSet;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Contingent;
import net.imagej.ops.OpRef;
import net.imagej.ops.Ops.Tamura.Coarseness;
import net.imagej.ops.Ops.Tamura.Contrast;
import net.imagej.ops.Ops.Tamura.Directionality;
import net.imagej.ops.features.haralick.HaralickFeature;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

/***
 * {
 * 
 * @link FeatureSet}for{@link HaralickFeature}s**@author Christian
 *       Dietz,University of Konstanz*@param <T>*@param <O>w
 */
@Plugin(type = FeatureSet.class, label = "Tamura Features", description = "Calculates the Tamura Features")
public class Tamura2DFeatureSet<T, O extends RealType<O>> extends
		AbstractOpRefFeatureSet<RandomAccessibleInterval<T>, O>implements/Contingent,DimensionBoundFeatureSet<RandomAccessibleInterval<T>,O>
{

 @Parameter(type = ItemIO.INPUT, label = "Histogram Size",
 description = "The size of the histogram used by the directionality
/ feature.",
 min = "1", max = "2147483647", stepSize = "1")

	private int histogramSize = 16;

	@Override
	protected Collection<? extends OpRef<?>> initOpRefs() {
		final HashSet<OpRef<?>> refs = new HashSet<OpRef<?>>();

		refs.add(ref(Coarseness.class));
		refs.add(ref(Contrast.class));
		refs.add(ref(Directionality.class, histogramSize));

		return refs;
	}

	@Override
	public boolean conforms() {
		return in().numDimensions() == 2;
	}

	@Override
	public int getMinDimensions() {
		return 2;
	}

	@Override
	public int getMaxDimensions() {
		return 2;
	}
}
