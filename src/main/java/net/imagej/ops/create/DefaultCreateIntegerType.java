
package net.imagej.ops.create;

import net.imagej.ops.Ops.Create;
import net.imagej.ops.OutputOp;
import net.imagej.ops.create.CreateOps.CreateIntegerType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.IntegerType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.integer.ShortType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedIntType;
import net.imglib2.type.numeric.integer.UnsignedShortType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Create an IntegerType with at least maxValue maximum
 *
 * @author Christian Dietz, University of Konstanz
 * @param <I> any IntegerType
 */
@SuppressWarnings("rawtypes")
@Plugin(type = Create.class)
public class DefaultCreateIntegerType implements CreateIntegerType,
	OutputOp<IntegerType>
{

	@Parameter(type = ItemIO.OUTPUT)
	private IntegerType output;

	@Parameter(required = false)
	private long maxValue;

	@Override
	public void run() {
		if (maxValue > 0) {
			if (maxValue <= 2) {
				output = (IntegerType) new BitType();
			}
			else if (maxValue <= Byte.MAX_VALUE + 1) {
				output = (IntegerType) new ByteType();
			}
			else if (maxValue <= (Byte.MAX_VALUE + 1) * 2) {
				output = (IntegerType) new UnsignedByteType();
			}
			else if (maxValue <= Short.MAX_VALUE + 1) {
				output = (IntegerType) new ShortType();
			}
			else if (maxValue <= (Short.MAX_VALUE + 1) * 2) {
				output = (IntegerType) new UnsignedShortType();
			}
			else if (maxValue <= Integer.MAX_VALUE + 1) {
				output = (IntegerType) new IntType();
			}
			else if (maxValue <= (Integer.MAX_VALUE + 1l) * 2l) {
				output = (IntegerType) new UnsignedIntType();
			}
			else if (maxValue <= Long.MAX_VALUE) {
				output = (IntegerType) new LongType();
			}
		}
		else {
			output = (IntegerType) new IntType();
		}
	}

	@Override
	public IntegerType getOutput() {
		return output;
	}

	@Override
	public void setOutput(final IntegerType output) {
		this.output = output;
	}

}
