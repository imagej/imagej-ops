package net.imagej.ops.descriptors.haralick;

import net.imagej.ops.OutputOp;
import net.imagej.ops.histogram.CooccurrenceMatrix;

/**
 * Base interface for "createcoocmatrix" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = CooccurrenceMatrix.NAME)
 * </pre>
 * 
 * @author Christian Dietz
 */
public interface CoocMatrixCreate extends OutputOp<CooccurrenceMatrix> {

	public final String NAME = "createcoocmatrix";

}
