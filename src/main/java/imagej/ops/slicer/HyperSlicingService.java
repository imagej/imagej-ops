package imagej.ops.slicer;

import imagej.ops.Op;
import imagej.ops.OpService;
import imagej.service.ImageJService;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;
import org.scijava.service.Service;

@Plugin(type = Service.class)
public class HyperSlicingService extends AbstractService implements
        ImageJService {

    @Parameter
    protected OpService opService;

    public RandomAccessibleInterval<?> process(RandomAccessibleInterval<?> src,
            RandomAccessibleInterval<?> res, int[] axis, Op op) {
        HyperSliceProcessor<RandomAccessibleInterval<?>, RandomAccessibleInterval<?>> hyperSlice =
                new HyperSliceProcessor<RandomAccessibleInterval<?>, RandomAccessibleInterval<?>>();
        return (RandomAccessibleInterval<?>)opService.run(hyperSlice, axis,
                src, res, op);
    }

    public RandomAccessibleInterval<?> hyperSlice(
            final RandomAccessibleInterval<?> rndAccessibleInterval,
            final Interval i) {
        return (RandomAccessibleInterval<?>)opService.run("hyperslicer",
                rndAccessibleInterval, i);
    }

}
