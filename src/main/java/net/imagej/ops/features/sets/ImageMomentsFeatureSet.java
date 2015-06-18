package net.imagej.ops.features.sets;

import java.util.HashSet;
import java.util.Set;

import net.imagej.ops.OpRef;
import net.imagej.ops.features.AbstractAutoResolvingFeatureSet;
import net.imagej.ops.features.FeatureSet;
import net.imagej.ops.features.moments.ImageMomentFeatures.CentralMoment00Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.CentralMoment01Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.CentralMoment02Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.CentralMoment03Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.CentralMoment10Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.CentralMoment11Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.CentralMoment12Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.CentralMoment20Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.CentralMoment21Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.CentralMoment30Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment1Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment2Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment3Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment4Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment5Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment6Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment7Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.Moment00Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.Moment01Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.Moment10Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.Moment11Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment02Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment03Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment11Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment12Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment20Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment21Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment30Feature;
import net.imagej.ops.features.moments.helper.CentralMomentsHelper;
import net.imagej.ops.features.moments.helper.NormalMomentsHelper;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * {@link FeatureSet} containing Geometric Features.
 *
 * @author Daniel Seebacher, University of Konstanz.
 *
 */
@Plugin(type = FeatureSet.class, label = "Image Moment Features", description = "Calculates the Image Moment Features")
public class ImageMomentsFeatureSet<I> extends
		AbstractAutoResolvingFeatureSet<I, DoubleType> {

	@Override
	public Set<OpRef<?>> getOutputOps() {
		final HashSet<OpRef<?>> outputOps = new HashSet<OpRef<?>>();

		outputOps.add(createOpRef(Moment00Feature.class));
		outputOps.add(createOpRef(Moment01Feature.class));
		outputOps.add(createOpRef(Moment10Feature.class));
		outputOps.add(createOpRef(Moment11Feature.class));

		outputOps.add(createOpRef(CentralMoment00Feature.class));
		outputOps.add(createOpRef(CentralMoment01Feature.class));
		outputOps.add(createOpRef(CentralMoment10Feature.class));
		outputOps.add(createOpRef(CentralMoment11Feature.class));
		outputOps.add(createOpRef(CentralMoment20Feature.class));
		outputOps.add(createOpRef(CentralMoment02Feature.class));
		outputOps.add(createOpRef(CentralMoment21Feature.class));
		outputOps.add(createOpRef(CentralMoment12Feature.class));
		outputOps.add(createOpRef(CentralMoment30Feature.class));
		outputOps.add(createOpRef(CentralMoment03Feature.class));

		outputOps.add(createOpRef(NormalizedCentralMoment02Feature.class));
		outputOps.add(createOpRef(NormalizedCentralMoment03Feature.class));
		outputOps.add(createOpRef(NormalizedCentralMoment11Feature.class));
		outputOps.add(createOpRef(NormalizedCentralMoment12Feature.class));
		outputOps.add(createOpRef(NormalizedCentralMoment20Feature.class));
		outputOps.add(createOpRef(NormalizedCentralMoment21Feature.class));
		outputOps.add(createOpRef(NormalizedCentralMoment30Feature.class));

		outputOps.add(createOpRef(HuMoment1Feature.class));
		outputOps.add(createOpRef(HuMoment2Feature.class));
		outputOps.add(createOpRef(HuMoment3Feature.class));
		outputOps.add(createOpRef(HuMoment4Feature.class));
		outputOps.add(createOpRef(HuMoment5Feature.class));
		outputOps.add(createOpRef(HuMoment6Feature.class));
		outputOps.add(createOpRef(HuMoment7Feature.class));

		return outputOps;
	}

	@Override
	public Set<OpRef<?>> getHiddenOps() {
		final HashSet<OpRef<?>> hiddenOps = new HashSet<OpRef<?>>();

		hiddenOps.add(createOpRef(NormalMomentsHelper.class));
		hiddenOps.add(createOpRef(CentralMomentsHelper.class));

		return hiddenOps;
	}
}
