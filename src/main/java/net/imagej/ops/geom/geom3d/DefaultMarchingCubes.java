/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2020 ImageJ developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.ops.geom.geom3d;

import net.imagej.mesh.Mesh;
import net.imagej.mesh.naive.NaiveDoubleMesh;
import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.geom.geom3d.mesh.DefaultVertexInterpolator;
import net.imagej.ops.geom.geom3d.mesh.VertexInterpolator;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;
import org.apache.commons.math3.util.MathArrays;
import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * This is a marching cubes implementation. It is inspired by Paul Bourke's
 * (http://paulbourke.net/geometry/polygonise/) implementation. Especially the
 * lookup tables are from his implementation.
 *
 * @author Tim-Oliver Buchholz (University of Konstanz)
 * @param <T> BooleanType
 */
@Plugin(type = Ops.Geometric.MarchingCubes.class)
public class DefaultMarchingCubes<T extends RealType<T> > extends
		AbstractUnaryFunctionOp<RandomAccessibleInterval<T>, Mesh> implements
		Ops.Geometric.MarchingCubes, Contingent
{
	@Parameter( type = ItemIO.INPUT, required = false )
	private double isolevel = 1;

	@Deprecated
	@Parameter( type = ItemIO.INPUT, required = false )
	private VertexInterpolator interpolatorClass =
			new DefaultVertexInterpolator();

	private final double[][] vertlist = new double[ 12 ][ 3 ];
	private final double[] vertex_values = new double[ 8 ];

	private static final double[] p0 = { 0, 0, 1 };
	private static final double[] p1 = { 1, 0, 1 };
	private static final double[] p2 = { 1, 0, 0 };
	private static final double[] p3 = { 0, 0, 0 };
	private static final double[] p4 = { 0, 1, 1 };
	private static final double[] p5 = { 1, 1, 1 };
	private static final double[] p6 = { 1, 1, 0 };
	private static final double[] p7 = { 0, 1, 0 };

	private byte[] mask(final RandomAccessibleInterval<T> input)
	{
		final int msx = ( int ) input.dimension( 0 );
		final int msy = ( int ) input.dimension( 1 );
		final int msz = ( int ) input.dimension( 2 );

		final int isx = msx + 2;
		final int isy = msy + 2;
		final int isz = msz + 2;

		final int is = isx * isy * isz;
		final byte[] indices = new byte[ is ];
		final Cursor< T > c = Views.flatIterable( input ).cursor();
		int j = isx * isy + isx + 1;
		for ( int z = 0; z < msz; ++z )
		{
			for ( int y = 0; y < msy; ++y )
			{
				for ( int x = 0; x < msx; ++x )
				{
					if ( c.next().getRealDouble() >= isolevel )
						indices[ j ] = 1;
					++j;
				}
				j += 2;
			}
			j += 2 * isx;
		}

		for ( int i = 0; i < is - 1; ++i )
			indices[ i ] = ubyte( indices[ i ] | ( indices[ i + 1 ] << 1 ) );

		for ( int i = 0; i < is - isx; ++i )
			indices[ i ] = ubyte( indices[ i ] | ( indices[ i + isx ] << 2 ) );

		for ( int i = 0; i < is - ( isx * isy ); ++i )
			indices[ i ] = ubyte( indices[ i ] | ( indices[ i + isx * isy ] << 4 ) );

		return indices;
	}

	private static byte ubyte( final int unsignedByte )
	{
		return ( byte ) ( unsignedByte & 0xff );
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public Mesh calculate(final RandomAccessibleInterval<T> input)
	{
		final int msx = ( int ) input.dimension( 0 );
		final int msy = ( int ) input.dimension( 1 );
		final int isx = msx + 2;
		final int isy = msy + 2;
		final byte[] mask = mask( input );

		Mesh output = new NaiveDoubleMesh();
		final RandomAccess< T > ra = Views.extendZero( input ).randomAccess( Intervals.expand( input, 1 ) );
		final int[] pos = new int[ 3 ];

		final int minX = ( int ) input.min( 0 ) - 1;
		final int minY = ( int ) input.min( 1 ) - 1;
		final int minZ = ( int ) input.min( 2 ) - 1;
		final int maxX = ( int ) input.max( 0 ) + 1;
		final int maxY = ( int ) input.max( 1 ) + 1;
		final int maxZ = ( int ) input.max( 2 ) + 1;

		for ( int z = minZ; z < maxZ; ++z )
		{
			for ( int y = minY; y < maxY; ++y )
			{
				for ( int x = minX; x < maxX; ++x )
				{
					final int mx = ( x - minX );
					final int my = ( y - minY );
					final int mz = ( z - minZ );
					final int mindex = mask[ mz * ( isx * isy ) + my * isx + mx ] & 0xff;

					final int EDGE = EDGE_TABLE[ mindex ];
					if ( EDGE != 0) {
						pos[ 0 ] = x;
						pos[ 1 ] = y;
						pos[ 2 ] = z;
						ra.setPosition( pos );
						vertex_values[3] = ra.get().getRealDouble();
						ra.fwd( 0 );
						vertex_values[2] = ra.get().getRealDouble();
						ra.fwd( 1 );
						vertex_values[6] = ra.get().getRealDouble();
						ra.bck( 0 );
						vertex_values[7] = ra.get().getRealDouble();
						ra.bck( 1 );
						ra.fwd( 2 );
						vertex_values[0] = ra.get().getRealDouble();
						ra.fwd( 0 );
						vertex_values[1] = ra.get().getRealDouble();
						ra.fwd( 1 );
						vertex_values[5] = ra.get().getRealDouble();
						ra.bck( 0 );
						vertex_values[4] = ra.get().getRealDouble();


						/* Find the vertices where the surface intersects the cube */
						if (0 != ( EDGE & 1)) {
							interpolatePoint(vertlist[0], p0, p1, vertex_values[0],
									vertex_values[1]);
						}
						if (0 != ( EDGE & 2)) {
							interpolatePoint(vertlist[1], p1, p2, vertex_values[1],
									vertex_values[2]);
						}
						if (0 != ( EDGE & 4)) {
							interpolatePoint(vertlist[2], p2, p3, vertex_values[2],
									vertex_values[3]);
						}
						if (0 != ( EDGE & 8)) {
							interpolatePoint(vertlist[3], p3, p0, vertex_values[3],
									vertex_values[0]);
						}
						if (0 != ( EDGE & 16)) {
							interpolatePoint(vertlist[4], p4, p5, vertex_values[4],
									vertex_values[5]);
						}
						if (0 != ( EDGE & 32)) {
							interpolatePoint(vertlist[5], p5, p6, vertex_values[5],
									vertex_values[6]);
						}
						if (0 != ( EDGE & 64)) {
							interpolatePoint(vertlist[6], p6, p7, vertex_values[6],
									vertex_values[7]);
						}
						if (0 != ( EDGE & 128)) {
							interpolatePoint(vertlist[7], p7, p4, vertex_values[7],
									vertex_values[4]);
						}
						if (0 != ( EDGE & 256)) {
							interpolatePoint(vertlist[8], p0, p4, vertex_values[0],
									vertex_values[4]);
						}
						if (0 != ( EDGE & 512)) {
							interpolatePoint(vertlist[9], p1, p5, vertex_values[1],
									vertex_values[5]);
						}
						if (0 != ( EDGE & 1024)) {
							interpolatePoint(vertlist[10], p2, p6, vertex_values[2],
									vertex_values[6]);
						}
						if (0 != ( EDGE & 2048)) {
							interpolatePoint(vertlist[11], p3, p7, vertex_values[3],
									vertex_values[7]);
						}

						/* Create the triangle */
						final byte[] TRIANGLE = TRIANGLE_TABLE[ mindex ];
						for ( int i = 0; i < TRIANGLE.length; i += 3) {
							final double v0x = vertlist[TRIANGLE[i]][0];
							final double v0y = vertlist[TRIANGLE[i]][1];
							final double v0z = vertlist[TRIANGLE[i]][2];
							final double v1x = vertlist[TRIANGLE[i + 1]][0];
							final double v1y = vertlist[TRIANGLE[i + 1]][1];
							final double v1z = vertlist[TRIANGLE[i + 1]][2];
							final double v2x = vertlist[TRIANGLE[i + 2]][0];
							final double v2y = vertlist[TRIANGLE[i + 2]][1];
							final double v2z = vertlist[TRIANGLE[i + 2]][2];
							if (positiveArea(v0x, v0y, v0z, v1x, v1y, v1z, v2x, v2y, v2z)) {
								output.triangles().add(x + v0x, y + v0y, z + v0z, x + v1x, y + v1y, z + v1z, x + v2x, y + v2y, z + v2z);
							}
						}
					}
				}
			}
		}
		return output;
	}

	private boolean positiveArea(double v0x, double v0y, double v0z, //
			double v1x, double v1y, double v1z, //
			double v2x, double v2y, double v2z)
	{
		final double p1x = v0x - v1x;
		final double p1y = v0y - v1y;
		final double p1z = v0z - v1z;
		final double p2x = v2x - v0x;
		final double p2y = v2y - v0y;
		final double p2z = v2z - v0z;

		// cross product
		final double cpx = MathArrays.linearCombination(p1y, p2z, -p1z, p2y);
		final double cpy = MathArrays.linearCombination(p1z, p2x, -p1x, p2z);
		final double cpz = MathArrays.linearCombination(p1x, p2y, -p1y, p2x);

		return cpx != 0 || cpy != 0 || cpz != 0;
	}

	private double[] interpolatePoint( double[] p0, double[] p1, boolean v1 )
	{
		return v1 ? p1 : p0;
	}

	private void interpolatePoint(double[] output, double[] p0, double[] p1, double v0, double v1) {
		if (Math.abs(isolevel - v0) < 0.00001) {
			for (int i = 0; i < 3; i++) {
				output[i] = p0[i];
			}
		} else if (Math.abs(isolevel - v1) < 0.00001) {
			for (int i = 0; i < 3; i++) {
				output[i] = p1[i];
			}
		} else if (Math.abs(v0 - v1) < 0.00001) {
			for (int i = 0; i < 3; i++) {
				output[i] = p0[i];
			}
		} else {
			double mu = (isolevel - v0) / (v1 - v0);

			output[0] = p0[0] + mu * (p1[0] - p0[0]);
			output[1] = p0[1] + mu * (p1[1] - p0[1]);
			output[2] = p0[2] + mu * (p1[2] - p0[2]);
		}
	}

	// For any edge, if one vertex is inside of the surface and the other is
	// outside of the surface
	// then the edge intersects the surface
	// For each of the 8 vertices of the cube can be two possible states :
	// either inside or outside of the surface
	// For any cube the are 2^8=256 possible sets of vertex states
	// This table lists the edges intersected by the surface for all 256
	// possible vertex states
	// There are 12 edges. For each entry in the table, if edge #n is
	// intersected, then bit #n is set to 1
	// This table is from Paul Bourke's
	// (http://paulbourke.net/geometry/polygonise/)
	// Marching Cubes implementation.
	private static final int[] EDGE_TABLE = new int[] {
			0x000, 0x80c, 0x406, 0xc0a, 0x8c0, 0x0cc, 0xcc6, 0x4ca,
			0x460, 0xc6c, 0x066, 0x86a, 0xca0, 0x4ac, 0x8a6, 0x0aa,
			0x109, 0x905, 0x50f, 0xd03, 0x9c9, 0x1c5, 0xdcf, 0x5c3,
			0x569, 0xd65, 0x16f, 0x963, 0xda9, 0x5a5, 0x9af, 0x1a3,
			0x203, 0xa0f, 0x605, 0xe09, 0xac3, 0x2cf, 0xec5, 0x6c9,
			0x663, 0xe6f, 0x265, 0xa69, 0xea3, 0x6af, 0xaa5, 0x2a9,
			0x30a, 0xb06, 0x70c, 0xf00, 0xbca, 0x3c6, 0xfcc, 0x7c0,
			0x76a, 0xf66, 0x36c, 0xb60, 0xfaa, 0x7a6, 0xbac, 0x3a0,
			0x190, 0x99c, 0x596, 0xd9a, 0x950, 0x15c, 0xd56, 0x55a,
			0x5f0, 0xdfc, 0x1f6, 0x9fa, 0xd30, 0x53c, 0x936, 0x13a,
			0x099, 0x895, 0x49f, 0xc93, 0x859, 0x055, 0xc5f, 0x453,
			0x4f9, 0xcf5, 0x0ff, 0x8f3, 0xc39, 0x435, 0x83f, 0x033,
			0x393, 0xb9f, 0x795, 0xf99, 0xb53, 0x35f, 0xf55, 0x759,
			0x7f3, 0xfff, 0x3f5, 0xbf9, 0xf33, 0x73f, 0xb35, 0x339,
			0x29a, 0xa96, 0x69c, 0xe90, 0xa5a, 0x256, 0xe5c, 0x650,
			0x6fa, 0xef6, 0x2fc, 0xaf0, 0xe3a, 0x636, 0xa3c, 0x230,
			0x230, 0xa3c, 0x636, 0xe3a, 0xaf0, 0x2fc, 0xef6, 0x6fa,
			0x650, 0xe5c, 0x256, 0xa5a, 0xe90, 0x69c, 0xa96, 0x29a,
			0x339, 0xb35, 0x73f, 0xf33, 0xbf9, 0x3f5, 0xfff, 0x7f3,
			0x759, 0xf55, 0x35f, 0xb53, 0xf99, 0x795, 0xb9f, 0x393,
			0x033, 0x83f, 0x435, 0xc39, 0x8f3, 0x0ff, 0xcf5, 0x4f9,
			0x453, 0xc5f, 0x055, 0x859, 0xc93, 0x49f, 0x895, 0x099,
			0x13a, 0x936, 0x53c, 0xd30, 0x9fa, 0x1f6, 0xdfc, 0x5f0,
			0x55a, 0xd56, 0x15c, 0x950, 0xd9a, 0x596, 0x99c, 0x190,
			0x3a0, 0xbac, 0x7a6, 0xfaa, 0xb60, 0x36c, 0xf66, 0x76a,
			0x7c0, 0xfcc, 0x3c6, 0xbca, 0xf00, 0x70c, 0xb06, 0x30a,
			0x2a9, 0xaa5, 0x6af, 0xea3, 0xa69, 0x265, 0xe6f, 0x663,
			0x6c9, 0xec5, 0x2cf, 0xac3, 0xe09, 0x605, 0xa0f, 0x203,
			0x1a3, 0x9af, 0x5a5, 0xda9, 0x963, 0x16f, 0xd65, 0x569,
			0x5c3, 0xdcf, 0x1c5, 0x9c9, 0xd03, 0x50f, 0x905, 0x109,
			0x0aa, 0x8a6, 0x4ac, 0xca0, 0x86a, 0x066, 0xc6c, 0x460,
			0x4ca, 0xcc6, 0x0cc, 0x8c0, 0xc0a, 0x406, 0x80c, 0x000
	};

	// For each of the possible cube state there is a specific triangulation
	// of the edge intersection points. This table lists all of
	// them in the form of 0-5 edge triples.
	//
	// This table is adapted from Paul Bourke's
	// (http://paulbourke.net/geometry/polygonise/)
	// Marching Cubes implementation.
	private static final byte[][] TRIANGLE_TABLE = new byte[][] {
			{},
			{ 11, 2, 3 },
			{ 2, 10, 1 },
			{ 10, 1, 3, 10, 3, 11 },
			{ 6, 11, 7 },
			{ 2, 3, 7, 2, 7, 6 },
			{ 1, 2, 11, 7, 1, 11, 1, 6, 10, 1, 7, 6 },
			{ 7, 6, 10, 1, 7, 10, 3, 7, 1 },
			{ 6, 5, 10 },
			{ 3, 11, 6, 5, 3, 6, 3, 10, 2, 3, 5, 10 },
			{ 6, 5, 1, 6, 1, 2 },
			{ 3, 11, 6, 5, 3, 6, 1, 3, 5 },
			{ 5, 10, 11, 5, 11, 7 },
			{ 5, 10, 2, 3, 5, 2, 7, 5, 3 },
			{ 1, 2, 11, 7, 1, 11, 5, 1, 7 },
			{ 3, 5, 1, 7, 5, 3 },
			{ 8, 3, 0 },
			{ 11, 2, 0, 11, 0, 8 },
			{ 8, 3, 2, 10, 8, 2, 8, 1, 0, 8, 10, 1 },
			{ 10, 1, 0, 8, 10, 0, 11, 10, 8 },
			{ 0, 8, 7, 6, 0, 7, 0, 11, 3, 0, 6, 11 },
			{ 0, 8, 7, 6, 0, 7, 2, 0, 6 },
			{ 0, 8, 1, 8, 7, 1, 7, 10, 1, 10, 7, 6, 11, 3, 2 },
			{ 7, 6, 10, 7, 10, 1, 8, 7, 1, 0, 8, 1 },
			{ 8, 3, 0, 10, 6, 5 },
			{ 5, 8, 11, 6, 5, 11, 5, 0, 8, 2, 5, 10, 5, 2, 0 },
			{ 8, 5, 1, 0, 8, 1, 8, 6, 5, 2, 8, 3, 8, 2, 6 },
			{ 8, 11, 0, 11, 5, 0, 5, 1, 0, 11, 6, 5 },
			{ 0, 10, 11, 3, 0, 11, 0, 5, 10, 7, 0, 8, 0, 7, 5 },
			{ 2, 0, 8, 5, 2, 8, 7, 5, 8, 2, 5, 10 },
			{ 11, 3, 2, 8, 1, 0, 8, 7, 1, 7, 5, 1 },
			{ 8, 7, 0, 7, 1, 0, 7, 5, 1 },
			{ 1, 9, 0 },
			{ 9, 0, 3, 11, 9, 3, 9, 2, 1, 9, 11, 2 },
			{ 2, 10, 9, 2, 9, 0 },
			{ 9, 0, 3, 11, 9, 3, 10, 9, 11 },
			{ 1, 9, 0, 7, 6, 11 },
			{ 9, 6, 2, 1, 9, 2, 9, 7, 6, 3, 9, 0, 9, 3, 7 },
			{ 7, 0, 2, 11, 7, 2, 7, 9, 0, 10, 7, 6, 7, 10, 9 },
			{ 3, 7, 0, 7, 10, 0, 10, 9, 0, 10, 7, 6 },
			{ 0, 1, 10, 6, 0, 10, 0, 5, 9, 0, 6, 5 },
			{ 3, 11, 0, 11, 6, 0, 6, 9, 0, 9, 6, 5, 10, 2, 1 },
			{ 6, 5, 9, 0, 6, 9, 2, 6, 0 },
			{ 11, 6, 3, 3, 6, 0, 6, 5, 0, 5, 9, 0 },
			{ 0, 7, 5, 9, 0, 5, 0, 11, 7, 10, 0, 1, 0, 10, 11 },
			{ 2, 1, 10, 0, 5, 9, 0, 3, 5, 3, 7, 5 },
			{ 7, 5, 9, 2, 7, 9, 0, 2, 9, 11, 7, 2 },
			{ 0, 3, 9, 3, 5, 9, 3, 7, 5 },
			{ 8, 3, 1, 8, 1, 9 },
			{ 11, 2, 1, 9, 11, 1, 8, 11, 9 },
			{ 8, 3, 2, 10, 8, 2, 9, 8, 10 },
			{ 8, 10, 9, 8, 11, 10 },
			{ 6, 9, 8, 7, 6, 8, 6, 1, 9, 3, 6, 11, 6, 3, 1 },
			{ 6, 2, 1, 8, 6, 1, 9, 8, 1, 7, 6, 8 },
			{ 11, 3, 2, 8, 6, 10, 9, 8, 10, 7, 6, 8 },
			{ 6, 10, 7, 10, 8, 7, 10, 9, 8 },
			{ 6, 3, 1, 10, 6, 1, 6, 8, 3, 9, 6, 5, 6, 9, 8 },
			{ 10, 2, 1, 11, 5, 9, 8, 11, 9, 6, 5, 11 },
			{ 9, 8, 5, 8, 2, 5, 2, 6, 5, 2, 8, 3 },
			{ 5, 9, 6, 9, 11, 6, 9, 8, 11 },
			{ 8, 5, 9, 7, 5, 8, 3, 1, 10, 11, 3, 10 },
			{ 8, 7, 9, 7, 5, 9, 2, 1, 10 },
			{ 5, 9, 7, 9, 8, 7, 2, 11, 3 },
			{ 8, 7, 9, 9, 7, 5 },
			{ 7, 8, 4 },
			{ 4, 7, 11, 2, 4, 11, 4, 3, 8, 4, 2, 3 },
			{ 2, 10, 1, 4, 7, 8 },
			{ 4, 1, 3, 8, 4, 3, 4, 10, 1, 11, 4, 7, 4, 11, 10 },
			{ 8, 4, 6, 8, 6, 11 },
			{ 2, 3, 8, 4, 2, 8, 6, 2, 4 },
			{ 1, 4, 6, 10, 1, 6, 1, 8, 4, 11, 1, 2, 1, 11, 8 },
			{ 1, 3, 8, 6, 1, 8, 4, 6, 8, 10, 1, 6 },
			{ 10, 6, 7, 8, 10, 7, 10, 4, 5, 10, 8, 4 },
			{ 8, 4, 3, 4, 5, 3, 5, 2, 3, 2, 5, 10, 6, 7, 11 },
			{ 8, 2, 6, 7, 8, 6, 8, 1, 2, 5, 8, 4, 8, 5, 1 },
			{ 6, 7, 11, 4, 3, 8, 4, 5, 3, 5, 1, 3 },
			{ 8, 4, 5, 10, 8, 5, 11, 8, 10 },
			{ 5, 10, 2, 5, 2, 3, 4, 5, 3, 8, 4, 3 },
			{ 5, 1, 2, 8, 5, 2, 11, 8, 2, 5, 8, 4 },
			{ 4, 5, 8, 5, 3, 8, 5, 1, 3 },
			{ 3, 0, 4, 3, 4, 7 },
			{ 4, 7, 11, 2, 4, 11, 0, 4, 2 },
			{ 10, 7, 3, 2, 10, 3, 10, 4, 7, 0, 10, 1, 10, 0, 4 },
			{ 11, 10, 1, 4, 11, 1, 0, 4, 1, 11, 4, 7 },
			{ 6, 11, 3, 0, 6, 3, 4, 6, 0 },
			{ 4, 2, 0, 6, 2, 4 },
			{ 2, 11, 3, 6, 1, 0, 4, 6, 0, 10, 1, 6 },
			{ 1, 0, 10, 0, 6, 10, 0, 4, 6 },
			{ 10, 0, 4, 5, 10, 4, 10, 3, 0, 7, 10, 6, 10, 7, 3 },
			{ 11, 6, 7, 10, 4, 5, 10, 2, 4, 2, 0, 4 },
			{ 2, 6, 3, 6, 7, 3, 0, 5, 1, 0, 4, 5 },
			{ 1, 0, 5, 0, 4, 5, 11, 6, 7 },
			{ 0, 4, 5, 11, 0, 5, 10, 11, 5, 3, 0, 11 },
			{ 10, 2, 5, 2, 4, 5, 2, 0, 4 },
			{ 4, 5, 0, 5, 1, 0, 11, 3, 2 },
			{ 4, 5, 0, 0, 5, 1 },
			{ 1, 9, 4, 7, 1, 4, 1, 8, 0, 1, 7, 8 },
			{ 4, 7, 9, 7, 11, 9, 11, 1, 9, 1, 11, 2, 3, 8, 0 },
			{ 7, 10, 9, 4, 7, 9, 7, 2, 10, 0, 7, 8, 7, 0, 2 },
			{ 3, 8, 0, 7, 9, 4, 7, 11, 9, 11, 10, 9 },
			{ 1, 11, 8, 0, 1, 8, 1, 6, 11, 4, 1, 9, 1, 4, 6 },
			{ 8, 0, 3, 9, 2, 1, 9, 4, 2, 4, 6, 2 },
			{ 2, 8, 0, 11, 8, 2, 10, 9, 4, 6, 10, 4 },
			{ 6, 10, 4, 10, 9, 4, 3, 8, 0 },
			{ 10, 6, 1, 6, 7, 1, 7, 0, 1, 0, 7, 8, 4, 5, 9 },
			{ 7, 11, 6, 10, 2, 1, 3, 8, 0, 5, 9, 4 },
			{ 4, 5, 9, 6, 8, 0, 2, 6, 0, 7, 8, 6 },
			{ 3, 8, 0, 5, 9, 4, 6, 7, 11 },
			{ 5, 9, 4, 1, 8, 0, 1, 10, 8, 10, 11, 8 },
			{ 8, 0, 3, 10, 2, 1, 5, 9, 4 },
			{ 2, 11, 0, 11, 8, 0, 5, 9, 4 },
			{ 4, 5, 9, 3, 8, 0 },
			{ 1, 9, 4, 7, 1, 4, 3, 1, 7 },
			{ 7, 11, 4, 4, 11, 9, 11, 2, 9, 2, 1, 9 },
			{ 10, 9, 2, 9, 7, 2, 7, 3, 2, 9, 4, 7 },
			{ 7, 11, 4, 11, 9, 4, 11, 10, 9 },
			{ 4, 6, 9, 6, 3, 9, 3, 1, 9, 3, 6, 11 },
			{ 9, 4, 1, 4, 2, 1, 4, 6, 2 },
			{ 9, 4, 10, 4, 6, 10, 3, 2, 11 },
			{ 9, 4, 10, 10, 4, 6 },
			{ 4, 5, 9, 6, 1, 10, 6, 7, 1, 7, 3, 1 },
			{ 4, 5, 9, 2, 1, 10, 11, 6, 7 },
			{ 3, 2, 7, 2, 6, 7, 9, 4, 5 },
			{ 5, 9, 4, 11, 6, 7 },
			{ 11, 3, 10, 3, 1, 10, 4, 5, 9 },
			{ 10, 2, 1, 4, 5, 9 },
			{ 4, 5, 9, 11, 3, 2 },
			{ 4, 5, 9 },
			{ 5, 4, 9 },
			{ 5, 4, 9, 3, 11, 2 },
			{ 2, 10, 5, 4, 2, 5, 2, 9, 1, 2, 4, 9 },
			{ 4, 11, 10, 5, 4, 10, 4, 3, 11, 1, 4, 9, 4, 1, 3 },
			{ 9, 5, 6, 11, 9, 6, 9, 7, 4, 9, 11, 7 },
			{ 9, 3, 7, 4, 9, 7, 9, 2, 3, 6, 9, 5, 9, 6, 2 },
			{ 1, 2, 9, 2, 11, 9, 11, 4, 9, 4, 11, 7, 6, 10, 5 },
			{ 5, 6, 10, 7, 9, 1, 3, 7, 1, 4, 9, 7 },
			{ 4, 9, 10, 4, 10, 6 },
			{ 3, 9, 10, 2, 3, 10, 3, 4, 9, 6, 3, 11, 3, 6, 4 },
			{ 4, 9, 1, 2, 4, 1, 6, 4, 2 },
			{ 6, 4, 9, 3, 6, 9, 1, 3, 9, 6, 3, 11 },
			{ 11, 7, 4, 9, 11, 4, 10, 11, 9 },
			{ 9, 10, 2, 7, 9, 2, 3, 7, 2, 4, 9, 7 },
			{ 11, 7, 4, 11, 4, 9, 2, 11, 9, 1, 2, 9 },
			{ 9, 1, 4, 1, 7, 4, 1, 3, 7 },
			{ 5, 4, 8, 3, 5, 8, 5, 0, 9, 5, 3, 0 },
			{ 5, 2, 0, 9, 5, 0, 5, 11, 2, 8, 5, 4, 5, 8, 11 },
			{ 2, 10, 3, 10, 5, 3, 5, 8, 3, 8, 5, 4, 9, 1, 0 },
			{ 9, 1, 0, 10, 4, 8, 11, 10, 8, 5, 4, 10 },
			{ 9, 5, 0, 5, 6, 0, 6, 3, 0, 3, 6, 11, 7, 4, 8 },
			{ 7, 4, 8, 5, 0, 9, 5, 6, 0, 6, 2, 0 },
			{ 9, 1, 0, 8, 7, 4, 11, 3, 2, 6, 10, 5 },
			{ 0, 9, 1, 6, 10, 5, 7, 4, 8 },
			{ 3, 6, 4, 8, 3, 4, 3, 10, 6, 9, 3, 0, 3, 9, 10 },
			{ 8, 11, 4, 11, 6, 4, 9, 2, 0, 9, 10, 2 },
			{ 0, 9, 1, 4, 3, 2, 6, 4, 2, 8, 3, 4 },
			{ 11, 6, 8, 6, 4, 8, 1, 0, 9 },
			{ 8, 7, 4, 11, 0, 9, 10, 11, 9, 3, 0, 11 },
			{ 10, 2, 9, 2, 0, 9, 7, 4, 8 },
			{ 1, 0, 9, 7, 4, 8, 11, 3, 2 },
			{ 9, 1, 0, 7, 4, 8 },
			{ 5, 4, 0, 5, 0, 1 },
			{ 11, 4, 0, 3, 11, 0, 11, 5, 4, 1, 11, 2, 11, 1, 5 },
			{ 2, 10, 5, 4, 2, 5, 0, 2, 4 },
			{ 4, 0, 5, 0, 11, 5, 11, 10, 5, 0, 3, 11 },
			{ 11, 1, 5, 6, 11, 5, 11, 0, 1, 4, 11, 7, 11, 4, 0 },
			{ 5, 2, 1, 6, 2, 5, 4, 0, 3, 7, 4, 3 },
			{ 6, 10, 5, 2, 7, 4, 0, 2, 4, 11, 7, 2 },
			{ 0, 3, 4, 3, 7, 4, 10, 5, 6 },
			{ 0, 1, 10, 6, 0, 10, 4, 0, 6 },
			{ 10, 2, 1, 11, 0, 3, 11, 6, 0, 6, 4, 0 },
			{ 2, 4, 0, 2, 6, 4 },
			{ 11, 6, 3, 6, 0, 3, 6, 4, 0 },
			{ 10, 11, 1, 11, 4, 1, 4, 0, 1, 4, 11, 7 },
			{ 7, 4, 3, 4, 0, 3, 10, 2, 1 },
			{ 7, 4, 11, 4, 2, 11, 4, 0, 2 },
			{ 0, 3, 4, 4, 3, 7 },
			{ 5, 4, 8, 3, 5, 8, 1, 5, 3 },
			{ 1, 5, 2, 5, 8, 2, 8, 11, 2, 8, 5, 4 },
			{ 10, 5, 2, 2, 5, 3, 5, 4, 3, 4, 8, 3 },
			{ 4, 8, 5, 8, 10, 5, 8, 11, 10 },
			{ 7, 4, 8, 5, 11, 3, 1, 5, 3, 6, 11, 5 },
			{ 2, 1, 6, 1, 5, 6, 8, 7, 4 },
			{ 2, 11, 3, 4, 8, 7, 5, 6, 10 },
			{ 6, 10, 5, 8, 7, 4 },
			{ 3, 1, 8, 1, 6, 8, 6, 4, 8, 1, 10, 6 },
			{ 4, 8, 6, 8, 11, 6, 1, 10, 2 },
			{ 3, 2, 8, 2, 4, 8, 2, 6, 4 },
			{ 4, 8, 6, 6, 8, 11 },
			{ 1, 10, 3, 10, 11, 3, 4, 8, 7 },
			{ 10, 2, 1, 7, 4, 8 },
			{ 7, 4, 8, 2, 11, 3 },
			{ 8, 7, 4 },
			{ 7, 8, 9, 7, 9, 5 },
			{ 2, 5, 7, 11, 2, 7, 2, 9, 5, 8, 2, 3, 2, 8, 9 },
			{ 2, 8, 9, 1, 2, 9, 2, 7, 8, 5, 2, 10, 2, 5, 7 },
			{ 5, 7, 10, 7, 11, 10, 1, 8, 9, 1, 3, 8 },
			{ 9, 5, 6, 11, 9, 6, 8, 9, 11 },
			{ 8, 9, 5, 2, 8, 5, 6, 2, 5, 8, 2, 3 },
			{ 6, 10, 5, 2, 9, 1, 2, 11, 9, 11, 8, 9 },
			{ 3, 8, 1, 8, 9, 1, 6, 10, 5 },
			{ 10, 6, 7, 8, 10, 7, 9, 10, 8 },
			{ 7, 11, 6, 3, 10, 2, 3, 8, 10, 8, 9, 10 },
			{ 2, 6, 1, 6, 8, 1, 8, 9, 1, 6, 7, 8 },
			{ 9, 1, 8, 1, 3, 8, 6, 7, 11 },
			{ 10, 8, 9, 11, 8, 10 },
			{ 3, 8, 2, 8, 10, 2, 8, 9, 10 },
			{ 2, 11, 1, 11, 9, 1, 11, 8, 9 },
			{ 3, 8, 1, 1, 8, 9 },
			{ 3, 0, 9, 5, 3, 9, 7, 3, 5 },
			{ 5, 7, 9, 7, 2, 9, 2, 0, 9, 7, 11, 2 },
			{ 1, 0, 9, 3, 10, 5, 7, 3, 5, 2, 10, 3 },
			{ 7, 11, 5, 11, 10, 5, 0, 9, 1 },
			{ 6, 11, 3, 6, 3, 0, 5, 6, 0, 9, 5, 0 },
			{ 5, 6, 9, 6, 0, 9, 6, 2, 0 },
			{ 9, 1, 0, 11, 3, 2, 6, 10, 5 },
			{ 1, 0, 9, 6, 10, 5 },
			{ 7, 3, 0, 10, 7, 0, 9, 10, 0, 7, 10, 6 },
			{ 0, 9, 2, 9, 10, 2, 7, 11, 6 },
			{ 6, 7, 2, 7, 3, 2, 9, 1, 0 },
			{ 9, 1, 0, 6, 7, 11 },
			{ 0, 9, 3, 9, 11, 3, 9, 10, 11 },
			{ 10, 2, 9, 9, 2, 0 },
			{ 0, 9, 1, 11, 3, 2 },
			{ 9, 1, 0 },
			{ 7, 8, 0, 1, 7, 0, 5, 7, 1 },
			{ 3, 8, 0, 7, 2, 1, 5, 7, 1, 11, 2, 7 },
			{ 0, 2, 8, 2, 5, 8, 5, 7, 8, 5, 2, 10 },
			{ 10, 5, 11, 5, 7, 11, 0, 3, 8 },
			{ 11, 8, 0, 5, 11, 0, 1, 5, 0, 6, 11, 5 },
			{ 5, 6, 1, 6, 2, 1, 8, 0, 3 },
			{ 8, 0, 11, 0, 2, 11, 5, 6, 10 },
			{ 3, 8, 0, 6, 10, 5 },
			{ 6, 7, 10, 10, 7, 1, 7, 8, 1, 8, 0, 1 },
			{ 10, 2, 1, 8, 0, 3, 7, 11, 6 },
			{ 8, 0, 7, 0, 6, 7, 0, 2, 6 },
			{ 8, 0, 3, 6, 7, 11 },
			{ 1, 10, 0, 10, 8, 0, 10, 11, 8 },
			{ 3, 8, 0, 10, 2, 1 },
			{ 2, 11, 0, 0, 11, 8 },
			{ 3, 8, 0 },
			{ 5, 3, 1, 5, 7, 3 },
			{ 2, 1, 11, 1, 7, 11, 1, 5, 7 },
			{ 10, 5, 2, 5, 3, 2, 5, 7, 3 },
			{ 10, 5, 11, 11, 5, 7 },
			{ 11, 3, 6, 3, 5, 6, 3, 1, 5 },
			{ 5, 6, 1, 1, 6, 2 },
			{ 11, 3, 2, 5, 6, 10 },
			{ 5, 6, 10 },
			{ 6, 7, 10, 7, 1, 10, 7, 3, 1 },
			{ 2, 1, 10, 7, 11, 6 },
			{ 3, 2, 7, 7, 2, 6 },
			{ 11, 6, 7 },
			{ 1, 10, 3, 3, 10, 11 },
			{ 10, 2, 1 },
			{ 2, 11, 3 },
			{}
	};

	@Override
	public boolean conforms() {
		return in().numDimensions() == 3;
	}
}
