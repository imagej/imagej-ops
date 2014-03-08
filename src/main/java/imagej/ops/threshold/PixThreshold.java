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
package imagej.ops.threshold;

import imagej.ops.Op;
import imagej.ops.UnaryFunction;
import net.imglib2.type.logic.BitType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "pixThreshold")
public class PixThreshold<T extends Comparable<T>> extends
        UnaryFunction<T, BitType> {

    @Parameter
    private T threshold;

    @Parameter
    private T in;

    @Parameter(type = ItemIO.OUTPUT)
    private BitType out;

    @Override
    public T getInput() {
        return in;
    }

    @Override
    public BitType getOutput() {
        return out;
    }

    @Override
    public void setInput(final T input) {
        in = input;
    }

    @Override
    public void setOutput(final BitType output) {
        out = output;
    }

    public void setThreshold(T threshold) {
        this.threshold = threshold;
    }

    @Override
    public BitType compute(final T input, final BitType output) {
        output.set(input.compareTo(threshold) > 0);
        return output;
    }

    @Override
    public UnaryFunction<T, BitType> copy() {
        final PixThreshold<T> func = new PixThreshold<T>();
        func.threshold = threshold;
        return func;
    }
}
