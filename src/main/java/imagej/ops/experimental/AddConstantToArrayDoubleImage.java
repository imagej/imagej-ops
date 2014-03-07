/*
 * #%L
 * A framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package imagej.ops.experimental;

import imagej.ops.Op;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.basictypeaccess.array.DoubleArray;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "add")
public class AddConstantToArrayDoubleImage implements Op {

	@Parameter(type = ItemIO.BOTH)
	private ArrayImg<DoubleType, DoubleArray> image;

	@Parameter
	private double value;

	@Override
	public void run() {
		final double[] data = image.update(null).getCurrentStorageArray();
		for (int i = 0; i < data.length; i++) {
			data[i] += value;
		}
	}

}

/*
NOTES:

Can select which "add" to use based on parameter compatibility
- We have list of inputs & outputs and their types
- Can check whether that list is compatible using ConversionUtils
- Use @Plugin name for name of op
 -- Avoids needing to load classes for *all* ops; only load matching names
- Use strong typing for input/output params
 -- Avoids needing to complicate Op interface, or even instantiate Ops to
    decide whether to use them
- Generics are not *completely* erased at runtime! We can inspect the
  generic qualities of fields to decide if they match. ConversionUtils has
  some support for this already, and more can be done as needed.

Note that this example uses ItemIO.BOTH; we NEED to do this for passing outputs
- I think we should keep this flexible; an Op can use BOTH, or not
- You can make any op that has a BOTH parameter into one that works functionally
  by linking it to a copying Op

In general, solving the "when to reuse buffers" problem is too difficult.
- Should be left to programmers.
- An Op can be created that uses other Ops
- Just need it to be dirt simple to call each Op (agnostic of implementation)
- ij.ops().op(name_of_op, args...)
- And then shortcuts for all builtins: e.g., ij.ops().add(...)

*/
