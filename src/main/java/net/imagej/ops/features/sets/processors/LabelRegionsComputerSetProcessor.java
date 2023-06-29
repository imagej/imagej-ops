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
package net.imagej.ops.features.sets.processors;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

import net.imagej.ops.Op;
import net.imagej.ops.features.sets.ComputerSet;
import net.imagej.ops.features.sets.tables.ComputerSetTableService;
import net.imagej.ops.special.computer.Computers;
import net.imagej.table.GenericTable;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.roi.labeling.LabelRegions;
import net.imglib2.type.Type;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * A LabelRegionsProcessor holds {@link ComputerSet}s and
 * {@link LabelRegionsComputerSetProcessor#compute(LabelRegions, GenericTable)}
 * computes the {@link Computers} on the given {@link LabelRegions} and returns a
 * {@link GenericTable}.
 *
 * The {@link GenericTable} holds for each {@link LabelRegion} a row and has as
 * many columns as {@link Computers} were calculated.
 *
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 * @param <S>
 *            LabelRegions type
 * @param <F>
 *            Type of the converted {@link LabelRegion}
 * @param <O>
 *            Output type of the {@link Computers}.
 */
@Plugin(type = Op.class)
public class LabelRegionsComputerSetProcessor<S, F, O extends Type<O>>
		extends AbstractUnaryComputerSetProcessor<LabelRegions<S>, F, O> {

	@Parameter
	private ComputerSetTableService<O> csts;

	/**
	 * Maps each {@link ComputerSet} to a unique name. This ensures unique
	 * column names in the {@link GenericTable}.
	 */
	private Map<ComputerSet<?, O>, String> names;
	
	/**
	 * Name of the column where the label is stored.
	 */
	private String labelColumnName;

	@Override
	public GenericTable createOutput(final LabelRegions<S> input1) {
		names = ComputerSetProcessorUtils.getUniqueNames(Arrays.asList(computerSets));
		labelColumnName = ComputerSetProcessorUtils.uniqueName(names.values(), "Label");
		return csts.createTable(computerSets, names, labelColumnName, input1.getExistingLabels().size());
	}

	@Override
	public void compute(final LabelRegions<S> input1, final GenericTable output) {
		
		AtomicInteger rowIdx = new AtomicInteger(0);
		
		StreamSupport.stream(input1.spliterator(), true).parallel().forEach(r -> {
			final int j = rowIdx.getAndIncrement();
			for (final ComputerSet<F, O> computerSet : computerSets) {
				// LabelRegion has to be converted to Polygon or Mesh.
				final Map<String, O> result = computerSet.calculate(cs.convert(r, computerSet.getInType()));
				for (final Entry<String, O> entry : result.entrySet()) {
					output.set(ComputerSetProcessorUtils.getComputerTableName(names.get(computerSet), entry.getKey()), j,
							result.get(entry.getValue()));
				}
			}
			output.set(labelColumnName, j, r.getLabel());
		});

	}

}
