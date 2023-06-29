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

import net.imagej.ops.Ops.Geometric.Circularity;
import net.imagej.ops.Ops.Geometric.Size;
import net.imagej.ops.features.sets.ComputerSet;
import net.imagej.ops.features.sets.Geometric2DComputerSet;
import net.imagej.ops.features.sets.tables.DefaultComputerSetTableService;
import net.imagej.table.GenericTable;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.roi.labeling.LabelRegions;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for the {@link LabelRegionsComputerSetProcessor}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
public class LabelRegionsComputerSetProcessorTest extends AbstractComputerSetProcessorTest {

	private LabelRegions<String> roi;

	@Before
	public void createROI() {
		roi = createLabelRegions(getTestImage2D(), 1, 255);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void allComputersAreActiveTest() {
		Geometric2DComputerSet<Polygon, DoubleType> geom = ops.op(Geometric2DComputerSet.class, Polygon.class);

		LabelRegionsComputerSetProcessor<String, DoubleType, DoubleType> processor = ops.op(
				LabelRegionsComputerSetProcessor.class, LabelRegions.class, new ComputerSet[] { geom },
				new DefaultComputerSetTableService<>());

		GenericTable result = processor.calculate(roi);

		List<ComputerSet<?, DoubleType>> computerSets = new ArrayList<>();
		computerSets.add(geom);
		checkAllResultTableForOneComputerSet(result, computerSets, 1);
	}

	@SuppressWarnings({ "unchecked" })
	@Test
	public void somecomputersAreActiveTest() {
		Geometric2DComputerSet<Polygon, DoubleType> geom = ops.op(Geometric2DComputerSet.class, Polygon.class,
				Arrays.asList(new Class[] { Circularity.class, Size.class }));

		LabelRegionsComputerSetProcessor<String, DoubleType, DoubleType> processor = ops.op(
				LabelRegionsComputerSetProcessor.class, LabelRegions.class, new ComputerSet[] { geom },
				new DefaultComputerSetTableService<>());

		GenericTable result = processor.calculate(roi);

		List<ComputerSet<?, DoubleType>> computerSets = new ArrayList<>();
		computerSets.add(geom);
		checkAllResultTableForOneComputerSet(result, computerSets, 1);
	}

	@SuppressWarnings({ "unchecked" })
	@Test
	public void mutliComputerSetProcessingTest() {
		Geometric2DComputerSet<Polygon, DoubleType> geom = ops.op(Geometric2DComputerSet.class, Polygon.class);
		Geometric2DComputerSet<Polygon, DoubleType> geom1 = ops.op(Geometric2DComputerSet.class, Polygon.class,
				Arrays.asList(new Class[] { Circularity.class, Size.class }));

		LabelRegionsComputerSetProcessor<String, DoubleType, DoubleType> processor = ops.op(
				LabelRegionsComputerSetProcessor.class, LabelRegions.class, new ComputerSet[] { geom, geom1 },
				new DefaultComputerSetTableService<>());

		GenericTable result = processor.calculate(roi);

		List<ComputerSet<?, DoubleType>> computerSets = new ArrayList<>();
		computerSets.add(geom);
		computerSets.add(geom1);
		checkResultTableForManyComputerSets(result, computerSets, 1);
	}

}
