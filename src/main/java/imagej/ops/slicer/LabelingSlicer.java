package imagej.ops.slicer;

import net.imglib2.Interval;
import net.imglib2.labeling.Labeling;
import net.imglib2.labeling.LabelingView;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

public class LabelingSlicer<L extends Comparable<L>> extends
		AbstractHyperSlicer {

	@Parameter
	Interval interval;

	@Parameter
	Labeling<L> in;

	@Parameter(type = ItemIO.OUTPUT)
	Labeling<L> out;

	@Override
	public void run() {
		out = new LabelingView<L>(hyperSlice(in, interval), in.<L> factory());
	}
}
