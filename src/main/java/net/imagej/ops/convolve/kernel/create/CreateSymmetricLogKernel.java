
package net.imagej.ops.convolve.kernel.create;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.ComplexType;

/**
 * Convenience op for generating a symmetric LOG kernel
 * 
 * @author bnorthan
 * @param <T>
 */
@Plugin(type = Op.class, name = Ops.LogKernel.NAME,
	priority = Priority.HIGH_PRIORITY)
public class CreateSymmetricLogKernel<T extends ComplexType<T>> extends
	AbstractCreateSymmetricKernel<T> implements Ops.LogKernel
{

	public void run() {

		double[] sigmas = new double[numDimensions];

		for (int d = 0; d < numDimensions; d++) {
			sigmas[d] = sigma;
		}

		if (calibration == null) {
			calibration = new double[numDimensions];

			for (int i = 0; i < numDimensions; i++) {
				calibration[i] = 1.0;
			}
		}

		output = (Img<T>) ops.run("logkernel", outType, fac, sigmas, calibration);
	}
}
