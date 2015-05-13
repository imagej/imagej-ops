package net.imagej.ops.create.helper;

import net.imglib2.Dimensions;
import net.imglib2.FinalInterval;

import org.scijava.convert.AbstractConverter;
import org.scijava.convert.ConversionRequest;
import org.scijava.convert.Converter;
import org.scijava.plugin.Plugin;

@Plugin(type = Converter.class)
public class IntArrayToDimensionsConverter extends
		AbstractConverter<int[], Dimensions> implements
		Converter<int[], Dimensions> {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T convert(Object src, Class<T> dest) {

		int[] input = (int[]) src;
		long[] convert = new long[input.length];
		for (int i = 0; i < input.length; i++) {
			convert[i] = input[i];
		}

		return (T) new FinalInterval(convert);
	}

	@Override
	public Class<Dimensions> getOutputType() {
		return Dimensions.class;
	}

	@Override
	public Class<int[]> getInputType() {
		return int[].class;
	}

	@Override
	public boolean supports(ConversionRequest request) {
		return request.sourceClass() == int[].class
				&& request.destType() == Dimensions.class;
	}
}
