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

import net.imagej.ops.special.inplace.AbstractBinaryInplace1Op;
import net.imagej.ops.special.inplace.BinaryInplace1Op;

import org.scijava.plugin.Parameter;

/**
 * Abstract base class for {@link MapBinaryInplace1} implementations.
 * 
 * @author Leon Yang
 * @param <EI1> element type of first inputs
 * @param <EI2> element type of second inputs
 * @param <EO> element type of outputs
 * @param <PA> producer of first inputs + outputs
 * @param <PI> producer of second inputs
 */
public abstract class AbstractMapBinaryInplace1<EI1, EI2, EO extends EI1, PA, PI>
	extends AbstractBinaryInplace1Op<PA, PI> implements
	MapBinaryInplace1<EI1, EI2, EO, BinaryInplace1Op<EI1, EI2, EO>>
{

	@Parameter
	private BinaryInplace1Op<EI1, EI2, EO> op;

	@Override
	public BinaryInplace1Op<EI1, EI2, EO> getOp() {
		return op;
	}

	@Override
	public void setOp(final BinaryInplace1Op<EI1, EI2, EO> op) {
		this.op = op;
	}
}
