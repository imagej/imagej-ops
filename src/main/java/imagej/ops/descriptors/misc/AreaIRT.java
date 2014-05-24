package imagej.ops.descriptors.misc;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;

import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = Area.NAME, label = Area.LABEL, priority = Priority.LOW_PRIORITY)
public class AreaIRT extends
		AbstractFunction<Iterable<? extends RealType<?>>, DoubleType> implements
		Area {

	@Override
	@SuppressWarnings("unused")
	public DoubleType compute(Iterable<? extends RealType<?>> input,
			DoubleType output) {

		if(output == null){
			output = new DoubleType();
		}

		double count = 0;
		for (RealType<?> realType : input) {
			++count;
		}

		output.set(count);
		return output;
	}

}
