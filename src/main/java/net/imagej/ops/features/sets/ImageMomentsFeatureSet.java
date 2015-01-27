package net.imagej.ops.features.sets;

import net.imagej.ops.OpRef;
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
import net.imagej.ops.functionbuilder.OutputOpRef;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * {@link FeatureSet} containing Geometric Features.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 * 
 */
@Plugin(type = FeatureSet.class, label = "Image Moment Features")
public class ImageMomentsFeatureSet<I extends RealType<I>> extends
		AutoResolvingFeatureSet<I, DoubleType> {

	public ImageMomentsFeatureSet() {
		super(new DoubleType());
		// add helper
		addHiddenOp(new OpRef(NormalMomentsHelper.class));
		addHiddenOp(new OpRef(CentralMomentsHelper.class));

		// add features
		addOutputOp(new OutputOpRef<DoubleType>(Moment00Feature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(Moment01Feature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(Moment10Feature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(Moment11Feature.class,
				DoubleType.class));

		addOutputOp(new OutputOpRef<DoubleType>(CentralMoment00Feature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(CentralMoment01Feature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(CentralMoment10Feature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(CentralMoment11Feature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(CentralMoment20Feature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(CentralMoment02Feature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(CentralMoment21Feature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(CentralMoment12Feature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(CentralMoment30Feature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(CentralMoment03Feature.class,
				DoubleType.class));

		addOutputOp(new OutputOpRef<DoubleType>(
				NormalizedCentralMoment02Feature.class, DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(
				NormalizedCentralMoment03Feature.class, DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(
				NormalizedCentralMoment11Feature.class, DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(
				NormalizedCentralMoment12Feature.class, DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(
				NormalizedCentralMoment20Feature.class, DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(
				NormalizedCentralMoment21Feature.class, DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(
				NormalizedCentralMoment30Feature.class, DoubleType.class));

		addOutputOp(new OutputOpRef<DoubleType>(HuMoment1Feature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(HuMoment2Feature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(HuMoment3Feature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(HuMoment4Feature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(HuMoment5Feature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(HuMoment6Feature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(HuMoment7Feature.class,
				DoubleType.class));
	}
}
