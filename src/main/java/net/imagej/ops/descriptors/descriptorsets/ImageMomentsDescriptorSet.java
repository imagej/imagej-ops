package net.imagej.ops.descriptors.descriptorsets;

import java.util.Iterator;

import net.imagej.ops.descriptors.moments.image.Moments;
import net.imglib2.Pair;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.ValuePair;

import org.scijava.Context;
import org.scijava.module.Module;

public class ImageMomentsDescriptorSet<I> extends ADescriptorSet<I> {

	public ImageMomentsDescriptorSet(Context context, Class<I> type) {
		super(context, type);

		addOp(Moments.class);
	}

	@Override
	protected Iterator<Pair<String, DoubleType>> createIterator() {
		final Module module = getCompiledModules().get(Moments.class);
		final DoubleType tmp = new DoubleType();

		module.run();

		return new Iterator<Pair<String, DoubleType>>() {

			final double[] output = ((Moments) module.getDelegateObject())
					.getOutput();

			int idx = 0;

			@Override
			public boolean hasNext() {
				return idx < output.length;
			}

			@Override
			public Pair<String, DoubleType> next() {
				tmp.set(output[idx]);
				return new ValuePair<String, DoubleType>("Image Moment [" + idx++
						+ "]", tmp);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Not Supported");
			}
		};
	}

}
