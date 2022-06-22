/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2022 ImageJ2 developers.
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
package net.imagej.ops.topology.eulerCharacteristic;

import java.util.stream.LongStream;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

import org.scijava.plugin.Plugin;

/**
 * An Op which calculates the correction needed to approximate the contribution of the image to the
 * Euler characteristic χ of the whole image. That is, it's assumed that the image is a small part cut
 * from a larger sample.
 * <p>
 * From Odgaard {@literal &} Gundersen (see below): <i>"-- the Euler characteristic of the entire 3-D
 * space will not be obtained by simply adding the Euler characteristics of cubic specimens.
 * By doing this, the contribution of the lower dimensional elements will not be considered".</i>
 * They give the correction as c = -1/2χ_2 - 1/4χ_1 - -1/8χ_0, where
 * </p>
 * <ul>
 * <li>χ_2 = χ of all the faces of the stack</li>
 * <li>χ_1 = χ of all the edges of the stack</li>
 * <li>χ_0 = χ of all the corner vertices of the stack.</li>
 * </ul>
 * Each face contributes to two, each edge to four, and each corner to eight stacks.
 * 
 * <p>
 * Odgaard A, Gundersen HJG (1993)<br>
 * Quantification of connectivity in cancellous bone, with special emphasis on 3-D reconstructions.<br>
 * Bone 14: 173-182.<br>
 * <a href="http://dx.doi.org/10.1016/8756-3282(93)90245-6">doi:10.1016/8756-3282(93)90245-6</a>
 * </p>
 * @author Michael Doube (Royal Veterinary College, London)
 * @author Richard Domander (Royal Veterinary College, London)
 * 
 * NB: Methods are public and static to help testing
 */
@Plugin(type = Ops.Topology.EulerCorrection.class)
public class EulerCorrection<B extends BooleanType<B>>
extends AbstractUnaryHybridCF<RandomAccessibleInterval<B>, DoubleType>
implements Ops.Topology.EulerCorrection, Contingent {
	/** The algorithm is defined only for 3D images */
	@Override
	public boolean conforms() {
		return in().numDimensions() == 3;
	}


	@Override
	public void compute(RandomAccessibleInterval<B> interval, DoubleType output) {
		final Traverser<B> traverser = new Traverser<>(interval);
		final long chiZero = stackCorners(traverser);
		final long e = stackEdges(traverser) + 3 * chiZero;
		final long d = voxelEdgeIntersections(traverser) + chiZero;
		final long c = stackFaces(traverser) + 2 * e - 3 * chiZero;
		final long b = voxelEdgeFaceIntersections(traverser);
		final long a = voxelFaceIntersections(traverser);

		final long chiOne = d - e;
		final long chiTwo = a - b + c;

		output.set(chiTwo / 2.0 + chiOne / 4.0 + chiZero / 8.0);
	}

	@Override
	public DoubleType createOutput(RandomAccessibleInterval<B> input) {
		return new DoubleType(0.0);
	}

	/**
	 * Counts the foreground voxels in stack corners
	 * <p>
	 * Calculates χ_0 from Odgaard and Gundersen
	 * </p>
	 */
	public static <B extends BooleanType<B>> int stackCorners(final Traverser<B> traverser) {
		int foregroundVoxels = 0;
		foregroundVoxels += getAtLocation(traverser, traverser.x0, traverser.y0, traverser.z0);
		foregroundVoxels += getAtLocation(traverser, traverser.x1, traverser.y0, traverser.z0);
		foregroundVoxels += getAtLocation(traverser, traverser.x1, traverser.y1, traverser.z0);
		foregroundVoxels += getAtLocation(traverser, traverser.x0, traverser.y1, traverser.z0);
		foregroundVoxels += getAtLocation(traverser, traverser.x0, traverser.y0, traverser.z1);
		foregroundVoxels += getAtLocation(traverser, traverser.x1, traverser.y0, traverser.z1);
		foregroundVoxels += getAtLocation(traverser, traverser.x1, traverser.y1, traverser.z1);
		foregroundVoxels += getAtLocation(traverser, traverser.x0, traverser.y1, traverser.z1);
		return foregroundVoxels;
	}

	/**
	 * Count the foreground voxels on the edges lining the stack
	 * <p>
	 * Contributes to χ_1 from Odgaard and Gundersen
	 * </p>
	 */
	public static <B extends BooleanType<B>> long stackEdges(final Traverser<B> traverser) {
		final long[] foregroundVoxels = {0};

		// left to right stack edges
		LongStream.of(traverser.z0, traverser.z1).forEach(z -> {
			LongStream.of(traverser.y0, traverser.y1).forEach(y -> {
				for (long x = 1; x < traverser.x1; x++) {
					foregroundVoxels[0] += getAtLocation(traverser, x, y, z);
				}
			});
		});

		LongStream.of(traverser.z0, traverser.z1).forEach(z -> {
			LongStream.of(traverser.x0, traverser.x1).forEach(x -> {
				for (long y = 1; y < traverser.y1; y++) {
					foregroundVoxels[0] += getAtLocation(traverser, x, y, z);
				}
			});
		});

		LongStream.of(traverser.y0, traverser.y1).forEach(y -> {
			LongStream.of(traverser.x0, traverser.x1).forEach(x -> {
				for (long z = 1; z < traverser.z1; z++) {
					foregroundVoxels[0] += getAtLocation(traverser, x, y, z);
				}
			});
		});

		return foregroundVoxels[0];
	}

	/**
	 * Count the foreground voxels on the faces that line the stack
	 * <p>
	 * Contributes to χ_2 from Odgaard and Gundersen
	 * </p>
	 */
	public static <B extends BooleanType<B>> int stackFaces(final Traverser<B> traverser) {
		final int[] foregroundVoxels = {0};

		LongStream.of(traverser.z0, traverser.z1).forEach(z -> {
			Cursor<B> cursor = Views.hyperSlice(traverser.rai, 2, z).localizingCursor();
			while (cursor.hasNext()) {
				cursor.fwd();
				final long x = cursor.getLongPosition(0);
				final long y = cursor.getLongPosition(1);
				if ( x == 0 || y == 0 || x == traverser.x1 || y == traverser.y1 )
					continue;
				foregroundVoxels[0] += cursor.get().getRealDouble();
			}
		});

		LongStream.of(traverser.y0, traverser.y1).forEach(y -> {
			Cursor<B> cursor = Views.hyperSlice(traverser.rai, 1, y).localizingCursor();
			while (cursor.hasNext()) {
				cursor.fwd();
				final long x = cursor.getLongPosition(0);
				final long z = cursor.getLongPosition(1);
				if ( x == 0 || z == 0 || x == traverser.x1 || z == traverser.z1 )
					continue;
				foregroundVoxels[0] += cursor.get().getRealDouble();
			}
		});

		LongStream.of(traverser.x0, traverser.x1).forEach(x -> {
			Cursor<B> cursor = Views.hyperSlice(traverser.rai, 0, x).localizingCursor();
			while (cursor.hasNext()) {
				cursor.fwd();
				final long y = cursor.getLongPosition(0);
				final long z = cursor.getLongPosition(1);
				if ( y == 0 || z == 0 || y == traverser.y1 || z == traverser.z1 )
					continue;
				foregroundVoxels[0] += cursor.get().getRealDouble();
			}
		});

		return foregroundVoxels[0];
	}

	/**
	 * Count the number of intersections between voxels in each 2x1 neighborhood and the the edges of the stack
	 * <p>
	 * Contributes to χ_1 from Odgaard and Gundersen
	 * </p>
	 */
	public static <B extends BooleanType<B>> long voxelEdgeIntersections(final Traverser<B> traverser) {
		final int[] voxelVertices = {0};

		LongStream.of(traverser.z0, traverser.z1).forEach(z -> {
			traverser.access.setPosition(z, 2);
			LongStream.of(traverser.y0, traverser.y1).forEach(y -> {
				traverser.access.setPosition(y, 1);
				for (long x = 1; x < traverser.xSize; x++) {
					final int voxelA = getAtLocation(traverser, x, y, z);
					final int voxelB = getAtLocation(traverser, x - 1, y, z);
					voxelVertices[0] += voxelA | voxelB;
				}
			});
		});

		LongStream.of(traverser.z0, traverser.z1).forEach(z -> {
			traverser.access.setPosition(z, 2);
			LongStream.of(traverser.x0, traverser.x1).forEach(x -> {
				traverser.access.setPosition(x, 0);
				for (long y = 1; y < traverser.ySize; y++) {
					final int voxelA = getAtLocation(traverser, x, y, z);
					final int voxelB = getAtLocation(traverser, x, y - 1, z);
					voxelVertices[0] += voxelA | voxelB;
				}
			});
		});

		LongStream.of(traverser.y0, traverser.y1).forEach(y -> {
			traverser.access.setPosition(y, 1);
			LongStream.of(traverser.x0, traverser.x1).forEach(x -> {
				traverser.access.setPosition(x, 0);
				for (long z = 1; z < traverser.zSize; z++) {
					final int voxelA = getAtLocation(traverser, x, y, z);
					final int voxelB = getAtLocation(traverser, x, y, z - 1);
					voxelVertices[0] += voxelA | voxelB;
				}
			});
		});

		return voxelVertices[0];
	}

	/**
	 * Count the intersections between voxel edges in each 2x2 neighborhood and the faces lining the stack
	 * <p>
	 * Contributes to χ_2 from Odgaard and Gundersen
	 * </p>
	 */
	public static <B extends BooleanType<B>> long voxelEdgeFaceIntersections(final Traverser<B> traverser) {
		final long[] voxelEdges = {0};
		final long[] iterations = {0};

		// Front and back faces (all 4 edges). Check 2 edges per voxel
		LongStream.of(traverser.z0, traverser.z1).forEach(z -> {
			RandomAccessibleInterval<B> sliceRai = Views.hyperSlice(traverser.rai, 2, z);
			sliceRai = Views.expandZero(sliceRai, 1, 1);
			final long w = sliceRai.dimension(0);

			Cursor<B> cursorA = Views.flatIterable(sliceRai).cursor();
			Cursor<B> cursorB = Views.flatIterable(sliceRai).cursor();
			Cursor<B> cursorC = Views.flatIterable(sliceRai).cursor();

			cursorA.jumpFwd(w + 1);
			cursorB.jumpFwd(1);
			cursorC.jumpFwd(w);

			while (cursorA.hasNext()) {
				cursorA.next();
				cursorB.next();
				cursorC.next();

				final boolean voxel = cursorA.get().get();
				if (voxel) {
					iterations[0]++;
					voxelEdges[0] += 2;
					continue;
				}

				voxelEdges[0] += cursorB.get().getRealDouble();
				voxelEdges[0] += cursorC.get().getRealDouble();

			}
		});

		// Top and bottom faces (horizontal edges)
		LongStream.of(traverser.y0, traverser.y1).forEach(y -> {
			RandomAccessibleInterval<B> sliceRai = Views.hyperSlice(traverser.rai, 1, y);
			final long w = sliceRai.dimension(0);

			Cursor<B> cursorA = Views.flatIterable(sliceRai).cursor();
			Cursor<B> cursorB = Views.flatIterable(sliceRai).cursor();

			cursorA.jumpFwd(w);
			cursorB.jumpFwd(0);

			while (cursorA.hasNext()) {
				cursorA.next();
				cursorB.next();

				if (cursorA.get().get() || cursorB.get().get())
					voxelEdges[0]++;
			}
		});

		// Top and bottom faces (vertical edges)
		LongStream.of(traverser.y0, traverser.y1).forEach(y -> {
			RandomAccessibleInterval<B> sliceRai = Views.hyperSlice(traverser.rai, 1, y);
			sliceRai = Views.expandZero(sliceRai, 1, 1);
			final long w = sliceRai.dimension(0);

			Cursor<B> cursorA = Views.flatIterable(sliceRai).cursor();
			Cursor<B> cursorB = Views.flatIterable(sliceRai).cursor();

			cursorA.jumpFwd(w + 1);
			cursorB.jumpFwd(w);

			while (cursorA.hasNext()) {
				cursorA.next();
				cursorB.next();

				if (cursorA.get().get() || cursorB.get().get())
					voxelEdges[0]++;        	  	
			}
		});

		// Left and right faces (horizontal edges)
		LongStream.of(traverser.x0, traverser.x1).forEach(x -> {
			RandomAccessibleInterval<B> sliceRai = Views.hyperSlice(traverser.rai, 0, x);
			final long w = sliceRai.dimension(0);

			Cursor<B> cursorA = Views.flatIterable(sliceRai).cursor();
			Cursor<B> cursorB = Views.flatIterable(sliceRai).cursor();

			cursorA.jumpFwd(w);
			cursorB.jumpFwd(0);

			while (cursorA.hasNext()) {
				cursorA.next();
				cursorB.next();

				if (cursorA.get().get() || cursorB.get().get())
					voxelEdges[0]++;
			}
		});

		// Left and right faces (vertical edges)
		LongStream.of(traverser.x0, traverser.x1).forEach(x -> {
			RandomAccessibleInterval<B> sliceRai = Views.hyperSlice(traverser.rai, 0, x);
			sliceRai = Views.expandZero(sliceRai, 1, 1);
			final long w = sliceRai.dimension(0);

			Cursor<B> cursorA = Views.flatIterable(sliceRai).localizingCursor();
			Cursor<B> cursorB = Views.flatIterable(sliceRai).cursor();

			cursorA.jumpFwd(w + 1);
			cursorB.jumpFwd(w);

			while (cursorA.hasNext()) {
				cursorA.next();
				cursorB.next();

				final long y = cursorA.getLongPosition(0);
				final long z = cursorA.getLongPosition(1);

				if (y == 0 || y > traverser.y1 || z > traverser.z1)
					continue;

				if (cursorA.get().get() || cursorB.get().get())
					voxelEdges[0]++;
			}
		});

		return voxelEdges[0];
	}

	/**
	 * Count the intersections between voxels in each 2x2 neighborhood and the faces lining the stack
	 * <p>
	 * Contributes to χ_2 from Odgaard and Gundersen
	 * </p>
	 */
	public static <B extends BooleanType<B>> long voxelFaceIntersections(final Traverser<B> traverser) {
		final long[] pixelFaces = {0};

		LongStream.of(traverser.z0, traverser.z1).forEach(z -> {
			RandomAccessibleInterval<B> sliceRai =  Views.hyperSlice(traverser.rai, 2, z);
			sliceRai = Views.expandZero(sliceRai, 1, 1);
			final long w = sliceRai.dimension(0);

			Cursor<B> cursorA = Views.flatIterable(sliceRai).cursor();
			Cursor<B> cursorB = Views.flatIterable(sliceRai).cursor();
			Cursor<B> cursorC = Views.flatIterable(sliceRai).cursor();
			Cursor<B> cursorD = Views.flatIterable(sliceRai).cursor();

			cursorA.jumpFwd(w + 1);
			cursorB.jumpFwd(w);
			cursorC.jumpFwd(1);
			cursorD.jumpFwd(0);

			while (cursorA.hasNext()) {
				final int pixelA = (int) cursorA.next().getRealDouble();
				final int pixelB = (int) cursorB.next().getRealDouble();
				final int pixelC = (int) cursorC.next().getRealDouble();
				final int pixelD = (int) cursorD.next().getRealDouble();
				pixelFaces[0] += pixelA | pixelB | pixelC | pixelD;
			}
		});

		LongStream.of(traverser.x0, traverser.x1).forEach(x -> {
			RandomAccessibleInterval<B> sliceRai =  Views.hyperSlice(traverser.rai, 0, x);
			sliceRai = Views.expandZero(sliceRai, 1, 0);
			final long w = sliceRai.dimension(0);

			Cursor<B> cursorA = Views.flatIterable(sliceRai).cursor();
			Cursor<B> cursorB = Views.flatIterable(sliceRai).cursor();
			Cursor<B> cursorC = Views.flatIterable(sliceRai).cursor();
			Cursor<B> cursorD = Views.flatIterable(sliceRai).cursor();

			cursorA.jumpFwd(w + 1);
			cursorB.jumpFwd(w);
			cursorC.jumpFwd(1);
			cursorD.jumpFwd(0);

			while (cursorA.hasNext()) {
				final int pixelA = (int) cursorA.next().getRealDouble();
				final int pixelB = (int) cursorB.next().getRealDouble();
				final int pixelC = (int) cursorC.next().getRealDouble();
				final int pixelD = (int) cursorD.next().getRealDouble();
				pixelFaces[0] += pixelA | pixelB | pixelC | pixelD;
			}
		});

		LongStream.of(traverser.y0, traverser.y1).forEach(y -> {
			RandomAccessibleInterval<B> sliceRai =  Views.hyperSlice(traverser.rai, 1, y);
			final long w = sliceRai.dimension(0);

			Cursor<B> cursorA = Views.flatIterable(sliceRai).localizingCursor();
			Cursor<B> cursorB = Views.flatIterable(sliceRai).cursor();
			Cursor<B> cursorC = Views.flatIterable(sliceRai).cursor();
			Cursor<B> cursorD = Views.flatIterable(sliceRai).cursor();

			cursorA.jumpFwd(w + 1);
			cursorB.jumpFwd(w);
			cursorC.jumpFwd(1);
			cursorD.jumpFwd(0);

			while (cursorA.hasNext()) {
				final int pixelA = (int) cursorA.next().getRealDouble();
				final int pixelB = (int) cursorB.next().getRealDouble();
				final int pixelC = (int) cursorC.next().getRealDouble();
				final int pixelD = (int) cursorD.next().getRealDouble();
				if (cursorA.getLongPosition(0) == 0)
					continue;
				pixelFaces[0] += pixelA | pixelB | pixelC | pixelD;
			}
		});
		return pixelFaces[0];
	}

	//region -- Helper methods --
	private static <B extends BooleanType<B>> int getAtLocation(final Traverser<B> traverser, final long x,
		final long y, final long z) {
		traverser.access.setPosition(x, 0);
		traverser.access.setPosition(y, 1);
		traverser.access.setPosition(z, 2);
		final double realDouble = traverser.access.get().getRealDouble();

		return (int) realDouble;
	}
	//endregion

	/** A convenience class for passing parameters */
	public static class Traverser<B extends BooleanType<B>> {
		public final long x0 = 0;
		public final long y0 = 0;
		public final long z0 = 0;
		public final long x1;
		public final long y1;
		public final long z1;
		public final long xSize;
		public final long ySize;
		public final long zSize;
		public final RandomAccess<B> access;
		public final RandomAccessibleInterval<B> rai;

		public Traverser(RandomAccessibleInterval<B> interval) {
			xSize = interval.dimension(0);
			ySize = interval.dimension(1);
			zSize = interval.dimension(2);
			x1 = xSize - 1;
			y1 = ySize - 1;
			z1 = zSize - 1;
			access = Views.extendZero(interval).randomAccess();
			rai = interval;
		}
	}
}
