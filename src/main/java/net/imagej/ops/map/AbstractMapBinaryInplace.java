/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
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

import net.imagej.ops.special.AbstractBinaryOp;
import net.imagej.ops.special.BinaryOp;
import net.imagej.ops.special.InplaceOp;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

/**
 * Abstract base class for {@link MapBinaryInplace} implementations.
 * 
 * @author Leon Yang
 * @param <EI1> element type of first inputs
 * @param <EI2> element type of second inputs
 * @param <EO> element type of outputs
 * @param <PI1> producer of first inputs
 * @param <PI2> producer of second inputs
 * @param <PO> producer of outputs
 */
public abstract class AbstractMapBinaryInplace<EI1, EI2, EO, PI1, PI2, PO>
	extends AbstractBinaryOp<PI1, PI2, PO> implements
	MapBinaryInplace<EI1, EI2, EO, BinaryOp<EI1, EI2, EO>>, InplaceOp<PO>
{

	@Parameter(type = ItemIO.BOTH)
	private PO out;

	@Parameter
	private PI1 in1;

	@Parameter
	private PI2 in2;

	@Parameter
	private BinaryOp<EI1, EI2, EO> op;

	@Override
	public PO arg() {
		return out;
	}

	@Override
	public void setArg(PO arg) {
		out = arg;
	}

	@Override
	public PO out() {
		return out;
	}

	@Override
	public PI1 in1() {
		return in1;
	}

	@Override
	public PI2 in2() {
		return in2;
	}

	@Override
	public void setInput1(PI1 input1) {
		in1 = input1;
	}

	@Override
	public void setInput2(PI2 input2) {
		in2 = input2;
	}

	@Override
	public BinaryOp<EI1, EI2, EO> getOp() {
		return op;
	}

	@Override
	public void setOp(BinaryOp<EI1, EI2, EO> op) {
		this.op = op;
	}

	@Override
	public boolean conforms() {
		return op instanceof InplaceOp;
	}

	@Override
	public AbstractMapBinaryInplace<EI1, EI2, EO, PI1, PI2, PO>
		getIndependentInstance()
	{
		return this;
	}

}
