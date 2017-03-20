package net.imagej.ops.geom.pixelbased;

import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Geometric.VerticesCount;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.DiamondShape;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Geometric.BoundarySize.class, description = "Pixelbased Boundary Size.")
public class DefaultBoundarySizeII extends AbstractUnaryHybridCF<LabelRegion<BitType>, DoubleType>
		implements VerticesCount {

	@Parameter(required = false)
	private Shape shape = new DiamondShape(1);

	@Parameter
	private OpService ops;

	@Override
	public DoubleType createOutput(LabelRegion<BitType> input) {
		return new DoubleType();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void compute(LabelRegion<BitType> input, DoubleType output) {
		final IterableInterval<BitType> outline = ops.geom().boundary(input);

		final RandomAccessibleInterval<DoubleType> kernel = ops.create()
				.kernel(new double[][] { { 10, 2, 10 }, { 2, 1, 2 }, { 10, 2, 10 } }, new DoubleType());

		final Cursor<FloatType> c = Views.iterable((RandomAccessibleInterval<FloatType>) ops.filter()
				.convolve(Views.interval(Views.extendZero((RandomAccessibleInterval<BitType>) outline),
						new long[] { -1, -1 }, new long[] { outline.max(0) + 1, outline.max(1) + 1 }), kernel))
				.cursor();

		int catA = 0;
		int catB = 0;
		int catC = 0;

		while (c.hasNext()) {
			switch ((int) c.next().get()) {
			case 15:
			case 7:
			case 25:
			case 5:
			case 17:
			case 27:
				catA++;
				break;
			case 21:
			case 33:
				catB++;
				break;
			case 13:
			case 23:
				catC++;
				break;
			}

		}

		output.set(catA + (catB * Math.sqrt(2)) + (catC * ((1d + Math.sqrt(2)) / 2d)));
	}

}
