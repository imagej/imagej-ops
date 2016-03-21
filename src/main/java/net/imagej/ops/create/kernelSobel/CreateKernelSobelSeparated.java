package net.imagej.ops.create.kernelSobel;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.create.AbstractCreateKernelImg;
import net.imglib2.Cursor;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.complex.ComplexDoubleType;
import net.imglib2.type.numeric.complex.ComplexFloatType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

import org.scijava.plugin.Plugin;

/**
 * Creates a separated sobel kernel.
 * 
 * @author Eike Heinz, University of Konstanz
 *
 * @param <T> type of input
 */

@Plugin(type = Ops.Create.KernelSobel.class, name = Ops.Create.KernelSobel.NAME)
public class CreateKernelSobelSeparated<T extends ComplexType<T> & NativeType<T>>
		extends AbstractCreateKernelImg<T> implements Contingent {

	private static final float[] values = { 1.0f, 2.0f, 1.0f, -1.0f, 0.0f, 1.0f };
	
	@Override
	public void run() {

		long[] dim = new long[4];

		dim[0] = 3;
		dim[1] = 1;

		for (int k = 2; k < dim.length; k++) {
			dim[k] = 1;
		}

		dim[dim.length - 1] = 2;

		createOutputImg(dim);
		final Cursor<T> cursor = getOutput().cursor();
		int i = 0;
		while (cursor.hasNext()) {
			cursor.fwd();
			cursor.get().setReal(values[i]);
			i++;
		}

	}

	@Override
	public boolean conforms() {
		// if outType is not null make sure it is a supported type
		if (getOutType() != null) {
			final Object tmp = getOutType();
			if ((tmp instanceof FloatType) || (tmp instanceof DoubleType) || (tmp instanceof ComplexFloatType)
					|| (tmp instanceof ComplexDoubleType))
				return true;
			return false;
		}

		return true;
	}

}
