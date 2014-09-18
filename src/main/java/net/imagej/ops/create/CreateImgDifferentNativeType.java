package net.imagej.ops.create;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = Ops.CreateImg.NAME)
public class CreateImgDifferentNativeType<T extends NativeType<T>, V extends NativeType<V>>
	implements Ops.CreateImg
{

	@Parameter
	private OpService ops;

	@Parameter(type = ItemIO.OUTPUT)
	private Img<V> output;

	@Parameter
	private Img<T> input;

	@Parameter
	private NativeType<V> type;

	// TODO discuss exception handling
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			long[] dims = new long[input.numDimensions()];
			input.dimensions(dims);
			output = (Img<V>) ops.run(Ops.CreateImg.class, input.factory()
					.imgFactory(type), type.copy(), dims);
		} catch (IncompatibleTypeException e) {
			throw new RuntimeException(e);
		}
	}
}
