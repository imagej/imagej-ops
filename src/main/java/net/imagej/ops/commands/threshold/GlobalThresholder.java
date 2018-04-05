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

package net.imagej.ops.commands.threshold;

import net.imagej.ImgPlus;
import net.imagej.axis.Axis;
import net.imagej.ops.AbstractOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.threshold.ComputeThreshold;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * TODO: should actually live in a different package!! OR: can this be
 * auto-generated?? (e.g. based on other plugin annotations)#
 * 
 * @author Martin Horn (University of Konstanz)
 */
@Plugin(type = Command.class, menuPath = "Image > Threshold > Apply Threshold")
public class GlobalThresholder<T extends RealType<T>> extends AbstractOp {

    @Parameter
    private ComputeThreshold<ImgPlus<T>,T> method;

    // should not be Dataset, DisplayService, ...
    @Parameter
    private ImgPlus<T> in;

    @Parameter(type = ItemIO.OUTPUT)
    private ImgPlus<BitType> out;

    // we need another widget for this!!
    @Parameter(required=false)
    private Axis[] axes;

    @Override
    public void run() {
        Op threshold = ops().op("threshold", out, in, method);

        // TODO actually map axes to int array
        ops().run(Ops.Slice.class, out, in, threshold, new int[]{0, 1});
    }
    
    // TODO call otsu: out = ops.run(GlobalThresholder.class, ops.ops(Otsu...),in).
}
