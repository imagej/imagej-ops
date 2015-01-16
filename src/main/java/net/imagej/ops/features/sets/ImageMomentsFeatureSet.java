package net.imagej.ops.features.sets;

import net.imagej.ops.features.AbstractFeatureSet;
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
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * {@link FeatureSet} containing Geometric Features.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 * 
 */
@Plugin(type = FeatureSet.class, label = "Image Moment Features")
public class ImageMomentsFeatureSet extends
		AbstractFeatureSet<IterableInterval<? extends RealType<?>>> {

	@Override
	protected void init() {

		// add helper
		addInvisible(NormalMomentsHelper.class, getInput());
		addInvisible(CentralMomentsHelper.class, getInput());

		// add features
		addVisible(Moment00Feature.class);
		addVisible(Moment01Feature.class);
		addVisible(Moment10Feature.class);
		addVisible(Moment11Feature.class);

		addVisible(CentralMoment00Feature.class);
		addVisible(CentralMoment01Feature.class);
		addVisible(CentralMoment10Feature.class);
		addVisible(CentralMoment11Feature.class);
		addVisible(CentralMoment20Feature.class);
		addVisible(CentralMoment02Feature.class);
		addVisible(CentralMoment21Feature.class);
		addVisible(CentralMoment12Feature.class);
		addVisible(CentralMoment30Feature.class);
		addVisible(CentralMoment03Feature.class);
		
		addVisible(NormalizedCentralMoment02Feature.class);
		addVisible(NormalizedCentralMoment03Feature.class);
		addVisible(NormalizedCentralMoment11Feature.class);
		addVisible(NormalizedCentralMoment12Feature.class);
		addVisible(NormalizedCentralMoment20Feature.class);
		addVisible(NormalizedCentralMoment21Feature.class);
		addVisible(NormalizedCentralMoment30Feature.class);

		addVisible(HuMoment1Feature.class);
		addVisible(HuMoment2Feature.class);
		addVisible(HuMoment3Feature.class);
		addVisible(HuMoment4Feature.class);
		addVisible(HuMoment5Feature.class);
		addVisible(HuMoment6Feature.class);
		addVisible(HuMoment7Feature.class);
	}

}
