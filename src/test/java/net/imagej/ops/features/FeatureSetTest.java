package net.imagej.ops.features;

import java.util.List;

import net.imagej.ops.Op;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MaxFeature;
import net.imagej.ops.features.sets.FirstOrderStatFeatureSet;
import net.imagej.ops.features.sets.HaralickFeatureSet;
import net.imagej.ops.features.sets.HistogramFeatureSet;
import net.imagej.ops.statistics.firstorder.FirstOrderStatOps.Max;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedByteType;

import org.junit.Test;
import org.scijava.plugin.PluginInfo;
import org.scijava.plugin.PluginService;

public class FeatureSetTest extends AbstractFeatureTest {

    @Test
    public void testHaralickFeatureSet() {

        @SuppressWarnings("unchecked")
        HaralickFeatureSet<UnsignedByteType> op = ops.op(
                HaralickFeatureSet.class, random, 8, 1, "HORIZONTAL");

        op.compute(random);
        op.compute(constant);
        op.compute(empty);
    }

    @Test
    public void testFirstOrderStatistics() {
        @SuppressWarnings("unchecked")
        FirstOrderStatFeatureSet<Img<UnsignedByteType>> op = ops.op(
                FirstOrderStatFeatureSet.class, random, new double[] { 50, 60,
                        70 });

        op.compute(random);
        op.compute(constant);
        op.compute(empty);
    }

    @Test
    public void testHistogramFeatureSet() {

        @SuppressWarnings("unchecked")
        HistogramFeatureSet<UnsignedByteType> op = ops.op(
                HistogramFeatureSet.class, Img.class, 256);

        op.compute(random).size();
        op.compute(constant);
        op.compute(empty);
    }

}
