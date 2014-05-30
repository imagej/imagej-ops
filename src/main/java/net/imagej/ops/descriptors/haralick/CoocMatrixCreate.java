package net.imagej.ops.descriptors.haralick;

import net.imagej.ops.OutputOp;
import net.imglib2.ops.data.CooccurrenceMatrix;

/**
 * Base interface for "cooccurrencematrix" operations.
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
	public final String NAME = "cooccurrencematrix";
}
