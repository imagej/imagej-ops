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

package net.imagej.ops.copy;

import java.util.List;
import java.util.Set;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.NullaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.roi.labeling.LabelingMapping;
import net.imglib2.roi.labeling.LabelingMapping.SerialisationAccess;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Copies a {@link LabelingMapping} into another {@link LabelingMapping}
 * 
 * @author Christian Dietz (University of Konstanz)
 * @param <L>
 */
@Plugin(type = Ops.Copy.LabelingMapping.class,
	priority = Priority.VERY_HIGH)
public class CopyLabelingMapping<L> extends
		AbstractUnaryHybridCF<LabelingMapping<L>, LabelingMapping<L>> implements
		Ops.Copy.LabelingMapping {
	
	private NullaryFunctionOp<LabelingMapping<L>> outputCreator;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize() {
		outputCreator = (NullaryFunctionOp) Functions.nullary(ops(),
			Ops.Create.LabelingMapping.class, LabelingMapping.class, in().numSets());
	}

	@Override
	public LabelingMapping<L> createOutput(final LabelingMapping<L> input) {
		return outputCreator.calculate();
	}

	@Override
	public void compute(final LabelingMapping<L> input,
			final LabelingMapping<L> output) {

		final LabelingMappingSerializationAccess<L> access = new LabelingMappingSerializationAccess<>(
				output);
		access.setLabelSets(new LabelingMappingSerializationAccess<>(input)
				.getLabelSets());
	}

}

/*
 * Access to LabelingMapping
 */
final class LabelingMappingSerializationAccess<T> extends
		SerialisationAccess<T> {

	protected LabelingMappingSerializationAccess(
			final LabelingMapping<T> labelingMapping) {
		super(labelingMapping);
	}

	@Override
	protected void setLabelSets(List<Set<T>> labelSets) {
		super.setLabelSets(labelSets);
	}

	@Override
	protected List<Set<T>> getLabelSets() {
		return super.getLabelSets();
	}

}
