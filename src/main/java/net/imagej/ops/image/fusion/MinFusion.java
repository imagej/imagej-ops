package net.imagej.ops.image.fusion;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Image.FuseMin;
import net.imglib2.type.numeric.RealType;

@Plugin(type = Ops.Image.FuseMin.class, name = Ops.Image.FuseMin.NAME)
public class MinFusion<T extends RealType<T>> extends AbstractFusionOp<T>implements FuseMin {

	@Override
	public T getPixelValue(T in1, T in2) {
		if (in1.compareTo(in2) < 0) {
			return in1;
		} else {
			return in2;
		}
	}

	@Override
	public T getExtensionValue(T in) {
		in.setReal(in.getMaxValue());
		return in;
	}
}
