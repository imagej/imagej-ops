/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2023 ImageJ2 developers.
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
import net.imagej.mesh.Triangle;
import net.imagej.mesh.Vertices;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.img.Img;
import net.imglib2.type.logic.BitType;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * <p>
 * This is a voxelizer that produces a binary image with values filled in along
 * the surface of the mesh.
 * </p>
 * <p>
 * Thanks to Tomas Möller for sharing his public domain code:
 * http://fileadmin.cs.lth.se/cs/personal/tomas_akenine-moller/code/tribox.txt
 * </p>
 * 
 * @author Kyle Harrington (University of Idaho)
 */
@Plugin(type = Ops.Geometric.Voxelization.class)
public class DefaultVoxelization3D extends AbstractUnaryFunctionOp<Mesh, RandomAccessibleInterval<BitType>>
		implements Ops.Geometric.Voxelization {

	@Parameter(type = ItemIO.INPUT, required = false)
	private int width = 10;

	@Parameter(type = ItemIO.INPUT, required = false)
	private int height = 10;

	@Parameter(type = ItemIO.INPUT, required = false)
	private int depth = 10;

	@Parameter
	private OpService ops;

	// Constants
	private static final double EPSILON = 1e-6;

	@Override
	public RandomAccessibleInterval<BitType> calculate(Mesh input) {
		Img<BitType> outImg = ops.create().img(new FinalInterval(width, height, depth), new BitType());

		Vertices verts = input.vertices();
		RealPoint minPoint = new RealPoint(3);
		RealPoint maxPoint = new RealPoint(3);

		// Initialize minPoint and maxPoint with the first vertex
		minPoint.setPosition(verts.iterator().next());
		maxPoint.setPosition(verts.iterator().next());

		// Calculate min and max with padding
		double[] padding = {0.5, 0.5, 0.5};
		for (RealLocalizable v : verts) {
			for (int d = 0; d < 3; d++) {
				if (v.getDoublePosition(d) < minPoint.getDoublePosition(d)) {
					minPoint.setPosition(v.getDoublePosition(d) - padding[d], d);
				}
				if (v.getDoublePosition(d) > maxPoint.getDoublePosition(d)) {
					maxPoint.setPosition(v.getDoublePosition(d) + padding[d], d);
				}
			}
		}

		// Calculate dimPoint and step sizes
		double[] dimPoint = new double[3];
		double[] stepSizes = new double[3];
		double[] voxelHalfsize = new double[3];
		for (int d = 0; d < 3; d++) {
			dimPoint[d] = maxPoint.getDoublePosition(d) - minPoint.getDoublePosition(d);
			stepSizes[d] = dimPoint[d] / new int[]{width, height, depth}[d];
			voxelHalfsize[d] = stepSizes[d] / 2.0;
		}

		for (final Triangle tri : input.triangles()) {
			Vector3D v1 = new Vector3D(tri.v0x(), tri.v0y(), tri.v0z());
			Vector3D v2 = new Vector3D(tri.v1x(), tri.v1y(), tri.v1z());
			Vector3D v3 = new Vector3D(tri.v2x(), tri.v2y(), tri.v2z());

			double[] minSubBoundary = {
					Math.min(v1.getX(), Math.min(v2.getX(), v3.getX())) - minPoint.getDoublePosition(0),
					Math.min(v1.getY(), Math.min(v2.getY(), v3.getY())) - minPoint.getDoublePosition(1),
					Math.min(v1.getZ(), Math.min(v2.getZ(), v3.getZ())) - minPoint.getDoublePosition(2)
			};

			double[] maxSubBoundary = {
					Math.max(v1.getX(), Math.max(v2.getX(), v3.getX())) - minPoint.getDoublePosition(0),
					Math.max(v1.getY(), Math.max(v2.getY(), v3.getY())) - minPoint.getDoublePosition(1),
					Math.max(v1.getZ(), Math.max(v2.getZ(), v3.getZ())) - minPoint.getDoublePosition(2)
			};

			int[][] indicesRange = new int[3][2];
			for (int d = 0; d < 3; d++) {
				indicesRange[d][0] = (int) Math.max(0, Math.floor(minSubBoundary[d] / stepSizes[d]));
				indicesRange[d][1] = (int) Math.min(new int[]{width, height, depth}[d], Math.ceil(maxSubBoundary[d] / stepSizes[d]) + 1);
			}

			double[][] corners = {
					{-0.5, -0.5, -0.5}, {0.5, -0.5, -0.5}, {-0.5, 0.5, -0.5}, {0.5, 0.5, -0.5},
					{-0.5, -0.5, 0.5}, {0.5, -0.5, 0.5}, {-0.5, 0.5, 0.5}, {0.5, 0.5, 0.5}
			};

			RandomAccess<BitType> ra = outImg.randomAccess();
			for (int i = indicesRange[0][0]; i < indicesRange[0][1]; i++) {
				for (int j = indicesRange[1][0]; j < indicesRange[1][1]; j++) {
					for (int k = indicesRange[2][0]; k < indicesRange[2][1]; k++) {
						ra.setPosition(new int[]{i, j, k});
						if (!ra.get().get()) {
							boolean intersected = false;
							double[] voxelCenter = {i * stepSizes[0], j * stepSizes[1], k * stepSizes[2]};
							for (double[] corner : corners) {
								double[] shiftedCenter = new double[3];
								for (int d = 0; d < 3; d++) {
									shiftedCenter[d] = voxelCenter[d] + corner[d] * voxelHalfsize[d];
								}
								if (triBoxOverlap(shiftedCenter, voxelHalfsize, v1, v2, v3) == 1) {
									intersected = true;
									break;
								}
							}
							if (intersected) {
								ra.get().set(true);
							}
						}
					}
				}
			}
		}
		return outImg;
	}

	private double findMin(double x0, double x1, double x2) {
		return Math.min(Math.min(x0, x1), x2);
	}

	private double findMax(double x0, double x1, double x2) {
		return Math.max(Math.max(x0, x1), x2);
	}

	private double dotArray(double[] v1, double[] v2) {
		return v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];
	}

	private int planeBoxOverlap(double[] normalArray, double[] vertArray, double[] maxboxArray) {
		double[] vminArray = new double[3];
		double[] vmaxArray = new double[3];
		for (int q = 0; q <= 2; q++) {
			double v = vertArray[q];
			if (normalArray[q] > 0.0F) {
				vminArray[q] = (-maxboxArray[q] - v);
				maxboxArray[q] -= v;
			} else {
				maxboxArray[q] -= v;
				vmaxArray[q] = (-maxboxArray[q] - v);
			}
		}
		if (dotArray(normalArray, vminArray) > 0.0F) {
			return 0;
		}
		if (dotArray(normalArray, vmaxArray) >= 0.0F) {
			return 1;
		}
		return 0;
	}

	private int axisTest_x01(double e0, double e02, double fez, double fey, double[] v0, double[] v1, double[] v2,
			double[] boxhalfsize) {
		double p0 = e0 * v0[1] - e02 * v0[2];
		double p2 = e0 * v2[1] - e02 * v2[2];
		double max;
		double min;

		if (p0 < p2) {
			min = p0;
			max = p2;
		} else {
			min = p2;
			max = p0;
		}
		double rad = fez * boxhalfsize[1] + fey * boxhalfsize[2];
		if ((min > rad) || (max < -rad)) {
			return 0;
		}
		return 1;
	}

	private int axisTest_x2(double a, double b, double fa, double fb, double[] v0, double[] v1, double[] v2,
			double[] boxhalfsize) {
		double p0 = a * v0[1] - b * v0[2];
		double p1 = a * v1[1] - b * v1[2];
		double max;
		double min;

		if (p0 < p1) {
			min = p0;
			max = p1;
		} else {
			min = p1;
			max = p0;
		}
		double rad = fa * boxhalfsize[1] + fb * boxhalfsize[2];
		if ((min > rad) || (max < -rad)) {
			return 0;
		}
		return 1;
	}

	private int axisTest_y02(double a, double b, double fa, double fb, double[] v0, double[] v1, double[] v2,
			double[] boxhalfsize) {
		double p0 = -a * v0[0] + b * v0[2];
		double p2 = -a * v2[0] + b * v2[2];
		double max;
		double min;

		if (p0 < p2) {
			min = p0;
			max = p2;
		} else {
			min = p2;
			max = p0;
		}
		double rad = fa * boxhalfsize[0] + fb * boxhalfsize[2];
		if ((min > rad) || (max < -rad)) {
			return 0;
		}
		return 1;
	}

	private int axisTest_y1(double a, double b, double fa, double fb, double[] v0, double[] v1, double[] v2,
			double[] boxhalfsize) {
		double p0 = -a * v0[0] + b * v0[2];
		double p1 = -a * v1[0] + b * v1[2];
		double max;
		double min;

		if (p0 < p1) {
			min = p0;
			max = p1;
		} else {
			min = p1;
			max = p0;
		}
		double rad = fa * boxhalfsize[0] + fb * boxhalfsize[2];
		if ((min > rad) || (max < -rad)) {
			return 0;
		}
		return 1;
	}

	private int axisTest_z12(double a, double b, double fa, double fb, double[] v0, double[] v1, double[] v2,
			double[] boxhalfsize) {
		double p1 = a * v1[0] - b * v1[1];
		double p2 = a * v2[0] - b * v2[1];
		double max;
		double min;

		if (p2 < p1) {
			min = p2;
			max = p1;
		} else {
			min = p1;
			max = p2;
		}
		double rad = fa * boxhalfsize[0] + fb * boxhalfsize[1];
		if ((min > rad) || (max < -rad)) {
			return 0;
		}
		return 1;
	}

	private int axisTest_z0(double a, double b, double fa, double fb, double[] v0, double[] v1, double[] v2,
			double[] boxhalfsize) {
		double p0 = a * v0[0] - b * v0[1];
		double p1 = a * v1[0] - b * v1[1];
		double max;
		double min;

		if (p0 < p1) {
			min = p0;
			max = p1;
		} else {
			min = p1;
			max = p0;
		}
		double rad = fa * boxhalfsize[0] + fb * boxhalfsize[1];
		if ((min > rad) || (max < -rad)) {
			return 0;
		}
		return 1;
	}

	private void sub(double[] v0, double[] vert1, double[] boxcenter) {
		vert1[0] -= boxcenter[0];
		vert1[1] -= boxcenter[1];
		vert1[2] -= boxcenter[2];
	}

	private void cross(double[] dest, double[] v1, double[] v2) {
		dest[0] = (v1[1] * v2[2] - v1[2] * v2[1]);
		dest[1] = (v1[2] * v2[0] - v1[0] * v2[2]);
		dest[2] = (v1[0] * v2[1] - v1[1] * v2[0]);
	}

	private int triBoxOverlap(double[] boxcenter, double[] boxhalfsize, Vector3D pf1, Vector3D pf2, Vector3D pf3) {
		double[] vert1 = pf1.toArray();
		double[] vert2 = pf2.toArray();
		double[] vert3 = pf3.toArray();

		double[] v0 = new double[3];
		double[] v1 = new double[3];
		double[] v2 = new double[3];

		double[] normal = new double[3];
		double[] e0 = new double[3];
		double[] e1 = new double[3];
		double[] e2 = new double[3];

		sub(v0, vert1, boxcenter);
		sub(v1, vert2, boxcenter);
		sub(v2, vert3, boxcenter);

		sub(e0, v1, v0);
		sub(e1, v2, v1);
		sub(e2, v0, v2);

		double fex = Math.abs(e0[0]);
		double fey = Math.abs(e0[1]);
		double fez = Math.abs(e0[2]);

		axisTest_x01(e0[2], e0[1], fez, fey, v0, v1, v2, boxhalfsize);
		axisTest_y02(e0[2], e0[0], fez, fex, v0, v1, v2, boxhalfsize);
		axisTest_z12(e0[1], e0[0], fey, fex, v0, v1, v2, boxhalfsize);

		fex = Math.abs(e1[0]);
		fey = Math.abs(e1[1]);
		fez = Math.abs(e1[2]);

		axisTest_x01(e1[2], e1[1], fez, fey, v0, v1, v2, boxhalfsize);
		axisTest_y02(e1[2], e1[0], fez, fex, v0, v1, v2, boxhalfsize);
		axisTest_z0(e1[1], e1[0], fey, fex, v0, v1, v2, boxhalfsize);

		fex = Math.abs(e2[0]);
		fey = Math.abs(e2[1]);
		fez = Math.abs(e2[2]);

		axisTest_x2(e2[2], e2[1], fez, fey, v0, v1, v2, boxhalfsize);
		axisTest_y1(e2[2], e2[0], fez, fex, v0, v1, v2, boxhalfsize);
		axisTest_z12(e2[1], e2[0], fey, fex, v0, v1, v2, boxhalfsize);

		double min = findMin(v0[0], v1[0], v2[0]);
		double max = findMax(v0[0], v1[0], v2[0]);
		if ((min > boxhalfsize[0]) || (max < -boxhalfsize[0])) {
			return 0;
		}
		min = findMin(v0[1], v1[1], v2[1]);
		max = findMax(v0[1], v1[1], v2[1]);
		if ((min > boxhalfsize[1]) || (max < -boxhalfsize[1])) {
			return 0;
		}
		min = findMin(v0[2], v1[2], v2[2]);
		max = findMax(v0[2], v1[2], v2[2]);
		if ((min > boxhalfsize[2]) || (max < -boxhalfsize[2])) {
			return 0;
		}
		cross(normal, e0, e1);
		if (planeBoxOverlap(normal, v0, boxhalfsize) != 1) {
			return 0;
		}
		return 1;
	}

}
