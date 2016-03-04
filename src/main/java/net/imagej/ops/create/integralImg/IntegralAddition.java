package net.imagej.ops.create.integralImg;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.converter.Converter;
import net.imglib2.converter.RealDoubleConverter;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Computes an integral image from an input image with order n.
 * 
 * @author Stefan Helfrich (University of Konstanz)
 */
@Plugin(type = Ops.Create.IntegralAdd.class,
	priority = Priority.LOW_PRIORITY)
public class IntegralAddition<I extends RealType<I>> extends
	AbstractUnaryComputerOp<IterableInterval<I>, IterableInterval<DoubleType>> implements
	Ops.Create.IntegralAdd
{
	
	private Converter<I, DoubleType> converter = new RealDoubleConverter<>();
	
	@Override
	public void compute1(final IterableInterval<I> input,
		final IterableInterval<DoubleType> output)
	{
		// TODO Input should just be one-dimensional (check!)
		
		Cursor<I> inputCursor = input.cursor();
		Cursor<DoubleType> outputCursor = output.cursor();
		
		DoubleType previousOutputValue = null;
		
		while (outputCursor.hasNext()) {
			I inputValue = inputCursor.next();
			DoubleType outputValue = outputCursor.next();
			
			if (previousOutputValue == null) {
				previousOutputValue = outputValue.copy();
				continue;
			}
			
			DoubleType convertedInputValue = outputValue.createVariable();
			converter.convert(inputValue, convertedInputValue);
			
			previousOutputValue.add(convertedInputValue);
			
			outputValue.set(previousOutputValue.copy());
		}
	}
	
}
