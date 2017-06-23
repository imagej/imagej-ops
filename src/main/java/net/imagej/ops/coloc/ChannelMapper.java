package net.imagej.ops.coloc;

/**
 * A channel mapper should map an input value to either channel one or
 * channel two.
 * 
 * @author Tom Kazimiers
 */
public interface ChannelMapper {
	double getCh1Threshold(double t);
	double getCh2Threshold(double t);
}
