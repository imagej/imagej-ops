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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.features.sets.ComputerSet;
import net.imagej.ops.features.sets.FirstOrderStatsComputerSet;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for the {@link ComputerSetProcessorUtils}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
public class ComputerSetProcessorUtilsTest extends AbstractOpTest {

	@SuppressWarnings({ "cast", "unchecked" })
	@Test
	public void uniqueNamesTest() {
		List<ComputerSet<?, DoubleType>> sets = new ArrayList<>();
		sets.add((FirstOrderStatsComputerSet<?, DoubleType>) ops.op(FirstOrderStatsComputerSet.class, Iterable.class));
		sets.add((FirstOrderStatsComputerSet<?, DoubleType>) ops.op(FirstOrderStatsComputerSet.class, Iterable.class));

		Map<ComputerSet<?, DoubleType>, String> uniqueNames = ComputerSetProcessorUtils.getUniqueNames(sets);

		Collection<String> names = uniqueNames.values();
		Collection<String> copy = new HashSet<>(names);

		for (String s : names) {
			copy.remove(s);
			Assert.assertTrue("Duplicated name.", !copy.contains(s));
		}

		int i = -1;
		for (ComputerSet<?, DoubleType> computerSet : sets) {
			if (i == -1) {
				Assert.assertTrue("No suffix is needed.",
						computerSet.getClass().getSimpleName().equals(uniqueNames.get(computerSet)));
				i++;
			} else {
				String tmp = computerSet.getClass().getSimpleName();
				tmp = tmp + "[" + i + "]";
				Assert.assertTrue("Incorrect suffix.", tmp.equals(uniqueNames.get(computerSet)));
			}
		}
	}

}
