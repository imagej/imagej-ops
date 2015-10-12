
package net.imagej.ops.featuresets;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Op;
import net.imagej.ops.OpRef;
import net.imagej.ops.Ops.Stats.Sum;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.DoubleType;

public class FeatureSetTest extends AbstractOpTest {

	@SuppressWarnings("unchecked")
	private Class<? extends Op>[] myOps() {

		final ArrayList<Class<? extends Op>> myOpList =
			new ArrayList<Class<? extends Op>>();
		myOpList.add(MySum.class);
		return myOpList.toArray(new Class[myOpList.size()]);
	}

	public static class MySum<I> extends AbstractFunctionOp<I, DoubleType>
		implements Sum
	{

		@Override
		public DoubleType compute(I input) {
			System.out.println("Using my SuperDuperOp");
			return new DoubleType(1337);
		}

	}

	@Test
	public void simpleTest() {
		final Img<UnsignedByteType> template = createRandomOutput();

		final StatsFeatureSet<Img<UnsignedByteType>, RealType> func = ops.op(
			StatsFeatureSet.class, template, Class[].class, RealType.class);

		for (int i = 0; i < 100; i++) {
			System.out.println("\n #### NEXT ITERATION ####");
			Map<NamedFeature, RealType> res = func.compute(createRandomOutput());
			for (final Entry<NamedFeature, RealType> entry : res.entrySet()) {
				System.out.println(entry.getKey().getName() + " " + entry.getValue());
			}
		}
	}

	private Img<UnsignedByteType> createRandomOutput() {

		Img<UnsignedByteType> img = (Img<UnsignedByteType>) ops.create().img(
			new long[] { 10, 10 }, new UnsignedByteType());

		Cursor<UnsignedByteType> cursor = img.cursor();

		while (cursor.hasNext()) {
			cursor.next().setReal(Math.random() * 255);
		}
		return img;
	}
}
