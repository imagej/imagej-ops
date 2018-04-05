/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.ops.map;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.ComputerConverter;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.converter.read.ConvertedRandomAccessibleInterval;
import net.imglib2.type.Type;

import org.scijava.plugin.Plugin;

/**
 * {@link MapView} which converts an {@link RandomAccessibleInterval} between
 * element types.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @param <EI> element type of inputs
 * @param <EO> element type of outputs
 */
@Plugin(type = Ops.Map.class)
public class MapViewRAIToRAI<EI, EO extends Type<EO>>
	extends
	AbstractMapView<EI, EO, RandomAccessibleInterval<EI>, RandomAccessibleInterval<EO>>
{

	@Override
	public RandomAccessibleInterval<EO> calculate(
		final RandomAccessibleInterval<EI> input)
	{
		final ComputerConverter<EI, EO> converter =
			new ComputerConverter<>(getOp());
		return new ConvertedRandomAccessibleInterval<>(input, converter,
			getType());
	}

}
