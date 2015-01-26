package net.imagej.ops.features;

import java.util.List;

import net.imagej.ops.features.sets.FirstOrderStatFeatureSet;
import net.imagej.ops.features.sets.HaralickFeatureSet;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.junit.Test;

public class FeatureSetTest extends AbstractFeatureTest {

    @Test
    public void testHaralickFeatureSet() {

        @SuppressWarnings("unchecked")
        HaralickFeatureSet<UnsignedByteType> op = ops.op(
                HaralickFeatureSet.class, random, 8, 1, "HORIZONTAL");

        eval(op.compute(random));
        eval(op.compute(constant));
        eval(op.compute(empty));
    }

    @Test
    public void testFirstOrderStatistics() {

        @SuppressWarnings("unchecked")
        FirstOrderStatFeatureSet<Img<UnsignedByteType>> op = ops.op(
                FirstOrderStatFeatureSet.class, random, new double[] { 50, 60,
                        70 });

        eval(op.compute(random));
        eval(op.compute(constant));
        eval(op.compute(empty));
    }

    private void eval(List<Pair<String, DoubleType>> list) {
        for (final Pair<String, DoubleType> result : list) {
            print(result);
        }
    }

    private void print(Pair<String, DoubleType> result) {
        System.out.println(result.getA() + " " + result.getB());
    }

}
