package net.imagej.ops.image.cooccurrencematrix;

import net.imagej.ops.Ops.Image.CooccurrenceMatrix;

/**
 * Simple interface for enums representing the orientation of a
 * {@link CooccurrenceMatrix}
 * 
 * @author Christian Dietz, University of Konstanz
 *
 */
public interface MatrixOrientation {

	boolean isCompatible(int numDims);

	MatrixOrientation getByName(String name);

	int getValueAtDim(int d);

	int numDims();

}
