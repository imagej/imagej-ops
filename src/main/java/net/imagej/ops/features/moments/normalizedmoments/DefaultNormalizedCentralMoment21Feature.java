package net.imagej.ops.features.moments.normalizedmoments;

import net.imagej.ops.Op;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment21Feature;
import net.imagej.ops.features.moments.helper.CentralMomentsHelper;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link NormalizedCentralMoment21Feature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = NormalizedCentralMoment21Feature.NAME, label = NormalizedCentralMoment21Feature.LABEL)
public class DefaultNormalizedCentralMoment21Feature implements
		NormalizedCentralMoment21Feature<DoubleType> {

	@Parameter(type = ItemIO.INPUT)
	private CentralMomentsHelper momentsHelper;

	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType out;


	@Override
	public void run() {
	    if(out == null){
	        out = new DoubleType();
	    }
	    
		out.setReal(momentsHelper.getOutput().getCentralMoment21()
				/ Math.pow(momentsHelper.getOutput().getCentralMoment00(),
						1 + ((2 + 1) / 2)));
	}


    @Override
    public DoubleType getOutput() {
        return out;
    }


    @Override
    public void setOutput(DoubleType output) {
        this.out = output;
    }
}
