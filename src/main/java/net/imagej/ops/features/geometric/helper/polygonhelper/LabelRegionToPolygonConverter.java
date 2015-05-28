package net.imagej.ops.features.geometric.helper.polygonhelper;

import java.lang.reflect.Type;

import net.imagej.ops.OpService;
import net.imagej.ops.geometric.polygon.GeometricPolygonOps.MooreContoursPolygon;
import net.imagej.ops.geometric.polygon.Polygon;
import net.imglib2.roi.labeling.LabelRegion;

import org.scijava.Priority;
import org.scijava.convert.AbstractConverter;
import org.scijava.convert.ConversionRequest;
import org.scijava.convert.Converter;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@SuppressWarnings("rawtypes")
@Plugin(type = Converter.class, priority = Priority.FIRST_PRIORITY)
public class LabelRegionToPolygonConverter extends
		AbstractConverter<LabelRegion, Polygon> {

	@Parameter
	private OpService ops;

	@SuppressWarnings("unchecked")
	@Override
	public <T> T convert(Object src, Class<T> dest) {
		return (T) ops.run(MooreContoursPolygon.class, src, true, true);
	}

	@Override
	public Class<Polygon> getOutputType() {
		return Polygon.class;
	}

	@Override
	public Class<LabelRegion> getInputType() {
		return LabelRegion.class;
	}

	@Override
	public boolean supports(ConversionRequest request) {

		Object sourceObject = request.sourceObject();
		Class<?> sourceClass = request.sourceClass();

		if (sourceObject != null && !(sourceObject instanceof LabelRegion)) {
			return false;
		} else if (sourceObject != null && !(sourceClass == LabelRegion.class)) {
			return false;
		}

		Class<?> destClass = request.destClass();
		Type destType = request.destType();

		if (destClass != null && !(destClass == Polygon.class)) {
			return false;
		} else if (destType != null && !(destType instanceof Polygon)) {
			return false;
		}

		return true;
	}
}
