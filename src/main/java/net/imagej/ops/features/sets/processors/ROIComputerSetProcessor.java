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
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import net.imagej.ops.Op;
import net.imagej.ops.features.sets.ComputerSet;
import net.imagej.ops.features.sets.tables.ComputerSetTableService;
import net.imagej.ops.special.computer.Computers;
import net.imagej.table.GenericTable;
import net.imglib2.RandomAccessible;
import net.imglib2.roi.Regions;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.roi.labeling.LabelRegions;
import net.imglib2.type.Type;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * A ROIProcessor holds {@link ComputerSet}s and
 * {@link ROIComputerSetProcessor#compute(RandomAccessible, LabelRegions, GenericTable)}
 * computes the {@link Computers} on the sampled {@link LabelRegion} of I and
 * returns a {@link GenericTable}.
 *
 * The {@link GenericTable} holds for each {@link LabelRegion} a row and has as
 * many columns as {@link Computers} were calculated.
 *
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 * @param <T>
 *            type of the {@link RandomAccessible}
 * @param <S>
 *            type of the {@link LabelRegions}
 * @param <O>
 *            output type of the {@link Computers}
 */
@Plugin(type = Op.class)
public class ROIComputerSetProcessor<T extends Type<T>, S, O extends Type<O>>
		extends AbstractBinaryComputerSetProcessor<RandomAccessible<T>, LabelRegions<S>, Iterable<T>, O> {

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
	public GenericTable createOutput(final RandomAccessible<T> input1, final LabelRegions<S> input2) {
		names = ComputerSetProcessorUtils.getUniqueNames(Arrays.asList(computerSets));
		labelColumnName = ComputerSetProcessorUtils.uniqueName(names.values(), "Label");
		return csts.createTable(computerSets, names, labelColumnName, input2.getExistingLabels().size());
	}

	@Override
	public void compute(final RandomAccessible<T> input1, final LabelRegions<S> input2, final GenericTable output) {

		Set<Pair<Iterable<T>, S>> regions = new HashSet<>();
		input2.forEach(r -> regions.add(new ValuePair<Iterable<T>, S>(Regions.sample(r, input1), r.getLabel())));

		AtomicInteger rowIdx = new AtomicInteger(0);

		regions.parallelStream().forEach(p -> {
			final int j = rowIdx.getAndIncrement();
			for (final ComputerSet<Iterable<T>, O> computerSet : computerSets) {
				final Map<String, O> result = computerSet.calculate(p.getA());
				for (final Entry<String, O> entry : result.entrySet()) {
					output.set(ComputerSetProcessorUtils.getComputerTableName(names.get(computerSet), entry.getKey()),
							j, entry.getValue());
				}
			}
			output.set(labelColumnName, j, p.getB());
		});

	}

}
