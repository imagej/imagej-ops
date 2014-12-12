
package net.imagej.ops.convolve.kernel.create;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.ComplexType;

/**
 * Convenience op for generating a symmetric Gaussian kernel
 * 
 * @author bnorthan
 * @param <T>
 */
@Plugin(type = Op.class, name = Ops.GaussKernel.NAME,
	priority = Priority.HIGH_PRIORITY)
public class CreateSymmetricGaussianKernel<T extends ComplexType<T>> extends
	AbstractCreateSymmetricKernel<T> implements Ops.GaussKernel
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

		output =
			(Img<T>) ops.run(CreateGaussianKernel.class, outType, fac, sigmas,
				calibration);
	}
}
