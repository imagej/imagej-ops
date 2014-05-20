
package imagej.ops.descriptors.statistics;

public abstract class AbstractFeature extends AbstractDescriptor implements
	Feature
{

	@Override
	public double getFeature() {
		return getOutput()[0];
	}

	@Override
	public double[] initOutput() {
		return new double[1];
	}

	@Override
	protected void compute(final double[] output) {
		output[0] = compute();
	}

	public abstract double compute();

}
