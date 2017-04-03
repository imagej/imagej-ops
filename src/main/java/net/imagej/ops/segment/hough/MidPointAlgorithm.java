package net.imagej.ops.segment.hough;

import net.imglib2.Cursor;
import net.imglib2.Localizable;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.Sampler;
import net.imglib2.type.numeric.RealType;

/**
 * Write circles in an image using the mid-point algorithm.
 * 
 * @author Jean-Yves Tinevez
 * @see <a href=
 *      "https://en.wikipedia.org/wiki/Midpoint_circle_algorithm">Midpoint
 *      circle algorithm on Wikipedia.</a>
 *
 */
public class MidPointAlgorithm
{

	public static final < T > Cursor< T > cursor( final RandomAccessible< T > rai, final Localizable center, final long radius )
	{
		return new MidPointCursor< T >( rai, center, radius, 0, 1 );
	}

	/**
	 * Writes a circle in the target {@link RandomAccessible}. The circle is
	 * written by <b>incrementing</b> the pixel values by 1 along the circle.
	 * 
	 * @param rai
	 *            the random accessible. It is the caller responsibility to
	 *            ensure it can be accessed everywhere the circle will be
	 *            iterated.
	 * @param center
	 *            the circle center. Must be at least of dimension 2. Dimensions
	 *            0 and 1 are used to specify the circle center.
	 * @param radius
	 *            the circle radius. The circle is written in a plane in
	 *            dimensions 0 and 1.
	 * @param <T>
	 *            the type of the target image.
	 */
	public static < T extends RealType< T > > void inc( final RandomAccessible< T > rai, final Localizable center, final long radius )
	{
		final Cursor< T > cursor = cursor( rai, center, radius );
		while ( cursor.hasNext() )
		{
			cursor.fwd();
			cursor.get().inc();
		}
	}

	/**
	 * Writes a circle in the target {@link RandomAccessible}. The circle is
	 * written by <b>setting</b> the pixel values with the specified value.
	 * 
	 * @param rai
	 *            the target random accessible. It is the caller responsibility
	 *            to ensure it can be accessed everywhere the circle will be
	 *            iterated.
	 * @param center
	 *            the circle center. Must be at least of dimension 2. Dimensions
	 *            0 and 1 are used to specify the circle center.
	 * @param radius
	 *            the circle radius. The circle is written in a plane in
	 *            dimensions 0 and 1.
	 * @param value
	 *            the value to write along the circle.
	 * @param <T>
	 *            the type of the target image.
	 */
	public static < T extends RealType< T > > void set( final RandomAccessible< T > rai, final Localizable center, final long radius, final T value )
	{
		final Cursor< T > cursor = cursor( rai, center, radius );
		while ( cursor.hasNext() )
		{
			cursor.fwd();
			cursor.get().set( value );
		}
	}
	
	/**
	 * Writes a circle in the target {@link RandomAccessible}. The circle is
	 * written by <b>adding</b> the specified value to the pixel values already
	 * in the image.
	 * 
	 * @param rai
	 *            the random accessible. It is the caller responsibility to
	 *            ensure it can be accessed everywhere the circle will be
	 *            iterated.
	 * @param center
	 *            the circle center. Must be at least of dimension 2. Dimensions
	 *            0 and 1 are used to specify the circle center.
	 * @param radius
	 *            the circle radius. The circle is written in a plane in
	 *            dimensions 0 and 1.
	 * @param value
	 *            the value to add along the circle.
	 * @param <T>
	 *            the type of the target image.
	 */
	public static < T extends RealType< T > > void add( final RandomAccessible< T > rai, final Localizable center, final long radius, final T value )
	{
		final Cursor< T > cursor = cursor( rai, center, radius );
		while ( cursor.hasNext() )
		{
			cursor.fwd();
			cursor.get().add( value );
		}
	}
	
	private MidPointAlgorithm()
	{}

	private static final class MidPointCursor< T > implements Cursor< T >
	{

		private final RandomAccessible< T > rai;

		private final RandomAccess< T > ra;

		private final Localizable center;

		private final long radius;

		private final int dimX;

		private final int dimY;

		private long x;

		private long y;

		private long dx;

		private long dy;

		private long f;

		private Octant octant;

		private boolean hasNext;


		private static enum Octant
		{
			INIT, EAST, NORTH, WEST, SOUTH, O1, O2, O3, O4, O5, O6, O7, O8;
		}

		public MidPointCursor( final RandomAccessible< T > rai, final Localizable center, final long radius, final int dimX, final int dimY )
		{
			this.rai = rai;
			this.center = center;
			this.radius = radius;
			this.dimX = dimX;
			this.dimY = dimY;
			this.ra = rai.randomAccess();
			reset();
		}

		@Override
		public void reset()
		{
			x = 0;
			y = radius;
			f = 1 - radius;
			dx = 1;
			dy = -2 * radius;
			octant = Octant.INIT;
			ra.setPosition( center );
			hasNext = true;
		}

		@Override
		public void fwd()
		{
			switch ( octant )
			{
			default:
			case INIT:
				ra.setPosition( center.getLongPosition( dimY ) + radius, dimY );
				octant = Octant.NORTH;
				break;

			case NORTH:
				ra.setPosition( center.getLongPosition( dimY ) - radius, dimY );
				octant = Octant.SOUTH;
				break;

			case SOUTH:
				ra.setPosition( center.getLongPosition( dimX ) - radius, dimX );
				ra.setPosition( center.getLongPosition( dimY ), dimY );
				octant = Octant.WEST;
				break;

			case WEST:
				ra.setPosition( center.getLongPosition( dimX ) + radius, dimX );
				octant = Octant.EAST;
				break;

			case EAST:
				x = x + 1;
				dx = dx + 2;
				f = f + dx;
				ra.setPosition( center.getLongPosition( dimX ) + x, dimX );
				ra.setPosition( center.getLongPosition( dimY ) + y, dimY );
				octant = Octant.O1;
				break;

			case O1:
				ra.setPosition( center.getLongPosition( dimX ) - x, dimX );
				octant = Octant.O2;
				break;

			case O2:
				ra.setPosition( center.getLongPosition( dimY ) - y, dimY );
				octant = Octant.O3;
				break;

			case O3:
				ra.setPosition( center.getLongPosition( dimX ) + x, dimX );
				octant = Octant.O4;
				// Stop here if x==y, lest the 45º will be iterated twice.
				if ( x >= y )
					hasNext = false;
				break;

			case O4:
				ra.setPosition( center.getLongPosition( dimX ) + y, dimX );
				ra.setPosition( center.getLongPosition( dimY ) - x, dimY );
				octant = Octant.O5;
				break;

			case O5:
				ra.setPosition( center.getLongPosition( dimX ) - y, dimX );
				octant = Octant.O6;
				break;

			case O6:
				ra.setPosition( center.getLongPosition( dimY ) + x, dimY );
				octant = Octant.O7;
				break;

			case O7:
				ra.setPosition( center.getLongPosition( dimX ) + y, dimX );
				octant = Octant.O8;
				// Stop here if dx would cross y.
				if ( x >= y - 1 )
					hasNext = false;
				break;

			case O8:
				if ( f > 0 )
				{
					y = y - 1;
					dy = dy + 2;
					f = f + dy;
				}
				x = x + 1;
				dx = dx + 2;
				f = f + dx;
				ra.setPosition( center.getLongPosition( dimX ) + x, dimX );
				ra.setPosition( center.getLongPosition( dimY ) + y, dimY );
				octant = Octant.O1;
				break;
			}
		}

		@Override
		public boolean hasNext()
		{
			return hasNext;
		}

		@Override
		public void localize( final float[] position )
		{
			ra.localize( position );
		}

		@Override
		public void localize( final double[] position )
		{
			ra.localize( position );
		}

		@Override
		public float getFloatPosition( final int d )
		{
			return ra.getFloatPosition( d );
		}

		@Override
		public double getDoublePosition( final int d )
		{
			return ra.getDoublePosition( d );
		}

		@Override
		public int numDimensions()
		{
			return ra.numDimensions();
		}

		@Override
		public T get()
		{
			return ra.get();
		}

		@Override
		public Sampler< T > copy()
		{
			return ra.copy();
		}

		@Override
		public void jumpFwd( final long steps )
		{
			for ( int i = 0; i < steps; i++ )
				fwd();
		}

		@Override
		public T next()
		{
			fwd();
			return get();
		}

		@Override
		public void localize( final int[] position )
		{
			ra.localize( position );
		}

		@Override
		public void localize( final long[] position )
		{
			ra.localize( position );
		}

		@Override
		public int getIntPosition( final int d )
		{
			return ra.getIntPosition( d );
		}

		@Override
		public long getLongPosition( final int d )
		{
			return ra.getLongPosition( d );
		}

		@Override
		public Cursor< T > copyCursor()
		{
			return new MidPointCursor<>( rai, center, radius, dimX, dimY );
		}

	}
}
