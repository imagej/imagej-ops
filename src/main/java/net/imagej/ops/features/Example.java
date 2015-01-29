package net.imagej.ops.features;

import net.imagej.ops.OpRef;
import net.imagej.ops.OpService;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MeanFeature;
import net.imagej.ops.features.sets.FirstOrderStatFeatureSet;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Pair;

import org.scijava.Context;

public class Example {

	public static void main(String[] args) {

		// some rnd data (could actually be an op on its own)
		final Img<FloatType> rndImgA = ArrayImgs
				.floats(new long[] { 100, 100 });
		final Img<FloatType> rndImgB = ArrayImgs
				.floats(new long[] { 500, 150 });

		for (final FloatType type : rndImgA) {
			type.set((float) Math.random());
		}

		for (final FloatType type : rndImgB) {
			type.set((float) Math.random());
		}

		// create service & context
		final Context c = new Context();
		final OpResolverService ors = c.service(OpResolverService.class);
		final OpService ops = c.service(OpService.class);

		/*
		 * Call a single feature.
		 */
		// 1. Create ResolvedOp (my naming sucks, ideas welcome)
		ResolvedOp<Img<FloatType>, DoubleType> resolved = ors.resolveAndRun(
				DoubleType.class, rndImgA, MeanFeature.class);

		// 2. Calculate and print results
		System.out.println("Mean: " + resolved.compute(rndImgA).get());
		System.out.println("Mean: " + resolved.compute(rndImgB).get());

		/*
		 * Use a standard feature set
		 */
		@SuppressWarnings("unchecked")
		final FirstOrderStatFeatureSet<Img<?>> set = ops.op(
				FirstOrderStatFeatureSet.class, rndImgA);

		// access via list
		for (final Pair<String, DoubleType> feature : set.getFeatures(rndImgA)) {
			System.out.println("Feature: [" + feature.getA() + "] Value: ["
					+ feature.getB().get() + "]");
		}

		/*
		 * Create your own auto-resolving feature-set
		 */
		final AutoResolvingFeatureSet<Img<FloatType>, DoubleType> ownSet = new AutoResolvingFeatureSet<Img<FloatType>, DoubleType>();
		c.inject(ownSet);

		// add any annotated op
		@SuppressWarnings("rawtypes")
		OpRef<MeanFeature> ref = new OpRef<MeanFeature>(MeanFeature.class);

		// add some stuff
		ownSet.addOutputOp(new OpRef<MeanFeature>(MeanFeature.class));

		// get via ref. OpRef is required as you may want to add the same Op
		// several times, but with different parameters (e.g. percentile)
		final MeanFeature<DoubleType> op = (MeanFeature<DoubleType>) ownSet
				.compute(rndImgA).get(ref);

		System.out
				.println("My very own feature set got resolved " + op.getOutput());

	}
}
