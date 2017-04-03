package net.imagej.ops.segment.hough;

import net.imglib2.RealLocalizable;
import net.imglib2.roi.geom.real.AbstractWritableSphere;

/**
 * Representation of a circle segmented via Hough circle transformation.
 * <p>
 * Instances are {@link RealLocalizable} and have a radius (in pixel units) and
 * a sensitivity. The sensitivity factor is used to report a quality of the
 * Hough circle detection, the smallest the better the circle. The sensitivity
 * can be used to sort a list of circles.
 * 
 * @author Jean-Yves Tinevez.
 * @author Gabe Selzer.
 *
 */
public class HoughCircle extends AbstractWritableSphere implements
	Comparable<HoughCircle>
{

	private final double sensitivity;

	public HoughCircle( final RealLocalizable pos, final double radius, final double sensitivity )
	{
		super( getCenter(pos) , radius);
		this.radius = radius;
		this.sensitivity = sensitivity;
	}
	
	private static double[] getCenter(RealLocalizable l) {
		double[] center = new double[l.numDimensions()];
		l.localize(center);
		return center;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		char c = '(';
		for ( int i = 0; i < numDimensions(); i++ )
		{
			sb.append( c );
			sb.append( String.format( "%.1f", center[ i ] ) );
			c = ',';
		}
		sb.append( ")" );
		return String.format( "%s\tR=%.1f\tSensitivity=%.1f", sb.toString(), radius, sensitivity );
	}

	public double getRadius()
	{
		return radius;
	}

	public double getSensitivity()
	{
		return sensitivity;
	}

	@Override
	public int compareTo( final HoughCircle o )
	{
		return sensitivity < o.sensitivity ? -1 : sensitivity > o.sensitivity ? +1 : 0;
	}

	@Override
	public boolean test( final RealLocalizable point )
	{
		final double dx = center[0] - point.getDoublePosition( 0 );
		final double dy = center[1] - point.getDoublePosition( 1 );
		final double dr2 = dx * dx + dy * dy;

		if ( dr2 < radius * radius )
			return false;

		return true;
	}

}
