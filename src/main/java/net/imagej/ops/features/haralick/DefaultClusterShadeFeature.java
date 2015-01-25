package net.imagej.ops.features.haralick;

import net.imagej.ops.Op;
import net.imagej.ops.features.haralick.HaralickFeatures.ClusterShadeFeature;
import net.imagej.ops.features.haralick.helper.CoocStdX;
import net.imagej.ops.features.haralick.helper.CooccurrenceMatrix;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

// cluster shade (from cellcognition)
// https://github.com/CellCognition/cecog/blob/master/csrc/include/cecog/features.hxx#L495
@Plugin(type = Op.class, name = ClusterShadeFeature.NAME)
public class DefaultClusterShadeFeature implements ClusterShadeFeature {

    @Parameter
    private CooccurrenceMatrix cooc;

    @Parameter
    private CoocStdX coocStdX;

    @Parameter(type = ItemIO.OUTPUT)
    private double output;

    @Override
    public double getFeatureValue() {
        return output;
    }

    @Override
    public void run() {
        final double[][] matrix = cooc.getOutput();
        final int nrGrayLevels = matrix.length;
        final double stdx = coocStdX.getOutput();

        output = 0;
        for (int j = 0; j < nrGrayLevels; j++) {
            output += Math.pow(2 * j - 2 * stdx, 3) * matrix[j][j];
            for (int i = j + 1; i < nrGrayLevels; i++) {
                output += 2 * Math.pow((i + j - 2 * stdx), 3) * matrix[i][j];
            }
        }
    }
}
