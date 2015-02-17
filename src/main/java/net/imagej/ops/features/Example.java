package net.imagej.ops.features;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import net.imagej.ops.OpRef;
import net.imagej.ops.OpService;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MeanFeature;
import net.imagej.ops.features.sets.FirstOrderStatFeatureSet;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

import org.scijava.Context;

public class Example {

	public static void main(final String[] args) {

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
		final ResolvedOp<Img<FloatType>, DoubleType> resolved = ors.resolve(
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
		for (final Entry<OpRef<?>, DoubleType> feature : set.compute(rndImgA)
				.entrySet()) {
			System.out.println("Feature: [" + feature.getKey() + "] Value: ["
					+ feature.getValue().get() + "]");
		}

		/*
		 * Create your own auto-resolving feature-set
		 */
		final AbstractAutoResolvingFeatureSet<Img<FloatType>, DoubleType> ownSet = new AbstractAutoResolvingFeatureSet<Img<FloatType>, DoubleType>() {

			@Override
			public Set<OpRef<?>> getOutputOps() {
				final HashSet<OpRef<?>> outputOps = new HashSet<OpRef<?>>();
				outputOps.add(createOpRef(MeanFeature.class));
				return outputOps;
			}

			@Override
			public Set<OpRef<?>> getHiddenOps() {
				return new HashSet<OpRef<?>>();
			}

		};

		c.inject(ownSet);
		ownSet.setInput(rndImgA);
		ownSet.run();
		System.out.println("My very own feature set got resolved "
				+ ownSet.getOutput());

	}
}
