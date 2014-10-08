package net.imagej.ops.features;

/**
 * Default implementation of a {@link FeatureResult}
 * 
 * @author Christian Dietz (University of Konstanz)
 * 
 */
public class DefaultFeatureResult implements FeatureResult {

	private double value;

	private String name;

	public DefaultFeatureResult() {
		//
	}

	public DefaultFeatureResult(String name, double value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public double getValue() {
		return value;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
