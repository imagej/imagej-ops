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
package net.imagej.ops.features.sets.tables;

import java.util.Map;

import net.imagej.ops.features.sets.ComputerSet;
import net.imagej.ops.features.sets.processors.ComputerSetProcessor;
import net.imagej.ops.features.sets.processors.ComputerSetProcessorUtils;
import net.imagej.table.DefaultGenericTable;
import net.imagej.table.GenericTable;
import net.imglib2.type.Type;

/**
 * Default implementation of {@link ComputerSetTableService}.
 *
 * The columns are added in the same order the {@link ComputerSet}s were added
 * to the {@link ComputerSetProcessor}.
 *
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
public class DefaultComputerSetTableService<O extends Type<O>> implements ComputerSetTableService<O> {

	private final GenericTable table;

	public DefaultComputerSetTableService() {
		table = new DefaultGenericTable();
	}

	@Override
	public GenericTable createTable(final ComputerSet<?, O>[] computerSets,
			final Map<ComputerSet<?, O>, String> names, final int numRows) {
		for (final ComputerSet<?, O> computerSet : computerSets) {
			final String computerSetName = names.get(computerSet);

			for (final String computerName : computerSet.getComputerNames()) {
				table.appendColumn(ComputerSetProcessorUtils.getComputerTableName(computerSetName, computerName));

			}
		}

		for (int i = 0; i < numRows; i++) {
			table.appendRow();
		}
		return table;
	}
	
	@Override
	public GenericTable createTable(ComputerSet<?, O>[] computerSets, Map<ComputerSet<?, O>, String> names,
			String labelColumnName, int numRows) {
		table.appendColumn(labelColumnName);
		for (final ComputerSet<?, O> computerSet : computerSets) {
			final String computerSetName = names.get(computerSet);

			for (final String computerName : computerSet.getComputerNames()) {
				table.appendColumn(ComputerSetProcessorUtils.getComputerTableName(computerSetName, computerName));

			}
		}

		for (int i = 0; i < numRows; i++) {
			table.appendRow();
		}
		return table;
	}
}
