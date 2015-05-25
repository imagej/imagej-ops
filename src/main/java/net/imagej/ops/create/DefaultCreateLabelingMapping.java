package net.imagej.ops.create;

import net.imagej.ops.OpService;
import net.imagej.ops.Ops.Create;
import net.imagej.ops.OutputOp;
import net.imagej.ops.create.CreateOps.CreateIntegerType;
import net.imagej.ops.create.CreateOps.CreateLabelingMapping;
import net.imglib2.roi.labeling.LabelingMapping;
import net.imglib2.type.numeric.IntegerType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * 
 * Create a LabelingMapping which can store at least maxNumSets different Sets
 * 
 * @author Christian Dietz, University of Konstanz
 *
 * @param <L>
 *            label type
 */
@Plugin(type = Create.class)
public class DefaultCreateLabelingMapping<L> implements CreateLabelingMapping,
		OutputOp<LabelingMapping<L>> {

	@Parameter
	private OpService ops;

	@Parameter(type = ItemIO.OUTPUT)
	private LabelingMapping<L> output;

	@Parameter(required = false)
	private int maxNumSets;

	@Override
	public void run() {
		output = new LabelingMapping<L>((IntegerType<?>) ops.run(
				CreateIntegerType.class, maxNumSets));
	}

	@Override
	public LabelingMapping<L> getOutput() {
		return output;
	}

	@Override
	public void setOutput(final LabelingMapping<L> output) {
		this.output = output;
	}

}
