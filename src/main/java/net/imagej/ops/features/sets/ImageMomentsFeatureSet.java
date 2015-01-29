package net.imagej.ops.features.sets;

import net.imagej.ops.features.AutoResolvingFeatureSet;
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
@Plugin(type = FeatureSet.class, label = "Image Moment Features")
public class ImageMomentsFeatureSet<I> extends
		AutoResolvingFeatureSet<I, DoubleType> {

	public ImageMomentsFeatureSet() {

		// add helper
		addHiddenOp(NormalMomentsHelper.class);
		addHiddenOp(CentralMomentsHelper.class);

		// add features
		addOutputOp(Moment00Feature.class);
		addOutputOp(Moment01Feature.class);
		addOutputOp(Moment10Feature.class);
		addOutputOp(Moment11Feature.class);

		addOutputOp(CentralMoment00Feature.class);
		addOutputOp(CentralMoment01Feature.class);
		addOutputOp(CentralMoment10Feature.class);
		addOutputOp(CentralMoment11Feature.class);
		addOutputOp(CentralMoment20Feature.class);
		addOutputOp(CentralMoment02Feature.class);
		addOutputOp(CentralMoment21Feature.class);
		addOutputOp(CentralMoment12Feature.class);
		addOutputOp(CentralMoment30Feature.class);
		addOutputOp(CentralMoment03Feature.class);

		addOutputOp(NormalizedCentralMoment02Feature.class);
		addOutputOp(NormalizedCentralMoment03Feature.class);
		addOutputOp(NormalizedCentralMoment11Feature.class);
		addOutputOp(NormalizedCentralMoment12Feature.class);
		addOutputOp(NormalizedCentralMoment20Feature.class);
		addOutputOp(NormalizedCentralMoment21Feature.class);
		addOutputOp(NormalizedCentralMoment30Feature.class);

		addOutputOp(HuMoment1Feature.class);
		addOutputOp(HuMoment2Feature.class);
		addOutputOp(HuMoment3Feature.class);
		addOutputOp(HuMoment4Feature.class);
		addOutputOp(HuMoment5Feature.class);
		addOutputOp(HuMoment6Feature.class);
		addOutputOp(HuMoment7Feature.class);
	}
}
