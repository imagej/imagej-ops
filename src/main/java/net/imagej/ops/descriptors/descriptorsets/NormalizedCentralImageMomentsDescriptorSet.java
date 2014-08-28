package net.imagej.ops.descriptors.descriptorsets;

import java.util.Iterator;

import net.imagej.ops.descriptors.moments.image.NormalizedCentralMoments;
import net.imglib2.Pair;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.ValuePair;

import org.scijava.Context;
import org.scijava.module.Module;

public class NormalizedCentralImageMomentsDescriptorSet<I> extends ADescriptorSet<I> {

	public NormalizedCentralImageMomentsDescriptorSet(Context context, Class<I> type) {
		super(context, type);

		addOp(NormalizedCentralMoments.class);
	}

	@Override
	protected Iterator<Pair<String, DoubleType>> createIterator() {
		final Module module = getCompiledModules().get(NormalizedCentralMoments.class);
		final DoubleType tmp = new DoubleType();

		module.run();

		return new Iterator<Pair<String, DoubleType>>() {

			final double[] output = ((NormalizedCentralMoments) module.getDelegateObject())
					.getOutput();

			int idx = 0;

			@Override
			public boolean hasNext() {
				return idx < output.length;
			}

			@Override
			public Pair<String, DoubleType> next() {
				tmp.set(output[idx]);
				return new ValuePair<String, DoubleType>("Normalized Central Image" + idx++
						+ "]", tmp);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Not Supported");
			}
		};
	}

}
