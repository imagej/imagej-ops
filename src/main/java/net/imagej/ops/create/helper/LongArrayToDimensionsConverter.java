package net.imagej.ops.create.helper;

import net.imglib2.Dimensions;
import net.imglib2.FinalInterval;

import org.scijava.convert.AbstractConverter;
import org.scijava.convert.ConversionRequest;
import org.scijava.convert.Converter;
import org.scijava.plugin.Plugin;

@Plugin(type = Converter.class)
public class LongArrayToDimensionsConverter extends
		AbstractConverter<long[], Dimensions> implements
		Converter<long[], Dimensions> {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T convert(Object src, Class<T> dest) {
		long[] input = (long[]) src;
		return (T) new FinalInterval(input);
	}

	@Override
	public Class<Dimensions> getOutputType() {
		return Dimensions.class;
	}

	@Override
	public Class<long[]> getInputType() {
		return long[].class;
	}

	@Override
	public boolean supports(ConversionRequest request) {

		return request.sourceClass() == long[].class
				&& request.destType() == Dimensions.class;
	}
}
