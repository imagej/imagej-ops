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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.imagej.ops.Ops.Stats.Max;
import net.imagej.ops.Ops.Stats.Mean;
import net.imagej.ops.features.sets.ComputerSet;
import net.imagej.ops.features.sets.FirstOrderStatsComputerSet;
import net.imagej.ops.features.sets.tables.DefaultComputerSetTableService;
import net.imagej.table.GenericTable;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Test;

/**
 * Test for the {@link IterableComputerSetProcessor}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
public class IterableComputerSetProcessorTest extends AbstractComputerSetProcessorTest {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void allComputersAreActiveTest() {
		FirstOrderStatsComputerSet<Iterable, DoubleType> stats = ops.op(FirstOrderStatsComputerSet.class,
				Iterable.class);

		IterableComputerSetProcessor<FloatType, DoubleType> processor = ops.op(IterableComputerSetProcessor.class,
				Iterable.class, new ComputerSet[] { stats }, new DefaultComputerSetTableService<>());

		GenericTable result = processor.calculate(getTestImage2D());

		List<ComputerSet<?, DoubleType>> computerSets = new ArrayList<>();
		computerSets.add(stats);
		checkAllResultTableForOneComputerSet(result, computerSets, 0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void someComputersAreActiveTest() {
		FirstOrderStatsComputerSet<Iterable, DoubleType> stats = ops.op(FirstOrderStatsComputerSet.class,
				Iterable.class, Arrays.asList(new Class[] { Mean.class, Max.class }));

		IterableComputerSetProcessor<FloatType, DoubleType> processor = ops.op(IterableComputerSetProcessor.class,
				Iterable.class, new ComputerSet[] { stats }, new DefaultComputerSetTableService<>());

		GenericTable result = processor.calculate(getTestImage2D());

		List<ComputerSet<?, DoubleType>> computerSets = new ArrayList<>();
		computerSets.add(stats);
		checkAllResultTableForOneComputerSet(result, computerSets, 0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void multiComputerSetProcessingTest() {
		FirstOrderStatsComputerSet<Iterable, DoubleType> stats = ops.op(FirstOrderStatsComputerSet.class,
				Iterable.class);		
		FirstOrderStatsComputerSet<Iterable, DoubleType> stats1 = ops.op(FirstOrderStatsComputerSet.class,
				Iterable.class, Arrays.asList(new Class[] { Mean.class, Max.class }));

		IterableComputerSetProcessor<FloatType, DoubleType> processor = ops.op(IterableComputerSetProcessor.class,
				Iterable.class, new ComputerSet[] { stats, stats1 }, new DefaultComputerSetTableService<>());

		final GenericTable result = processor.calculate(getTestImage2D());

		final List<ComputerSet<?, DoubleType>> computerSets = new ArrayList<>();
		computerSets.add(stats);
		computerSets.add(stats1);
		checkResultTableForManyComputerSets(result, computerSets, 0);
	}

}
