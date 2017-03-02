package net.imagej.ops.geom.pixelbased;

import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Geometric.VerticesCount;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.DiamondShape;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.img.Img;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory.Boundary;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Geometric.BoundarySize.class, description = "Pixelbased Boundary Size.")
public class DefaultBoundarySizeII<T extends RealType<T>> extends
		AbstractUnaryHybridCF<RandomAccessibleInterval<T>, DoubleType> implements VerticesCount {

	@Parameter(required = false)
	private Shape shape = new DiamondShape(1);

	@Parameter
	private OpService ops;

	@Override
	public DoubleType createOutput(RandomAccessibleInterval<T> input) {
		return new DoubleType();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void compute(RandomAccessibleInterval<T> input, DoubleType output) {
		RandomAccessibleInterval<T> outline = ops.geom().boundary(input);

		RandomAccessibleInterval<T> kernel = (RandomAccessibleInterval<T>) ops.create().kernel(new double[][] {{10, 2, 10}, {2, 1, 2}, {10, 2, 10}}, new DoubleType());
		
		Img<BitType> test = ops.convert().bit(Views.interval(Views.extendZero(outline), new long[]{-1, -1}, new long[]{outline.max(0)+1, outline.max(1)+1}));
		final Cursor<T> c = Views.iterable((RandomAccessibleInterval<T>) ops.filter().convolve(test, kernel)).cursor();
		
		Cursor<BitType> cu = test.cursor();
		while(cu.hasNext()) {
			System.out.println(cu.next().getRealDouble());
		}
		
		int catA = 0;
        int catB = 0;
        int catC = 0;
		
		while (c.hasNext()) {
            switch ((int)c.next().getRealDouble()) {
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
