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
import net.imagej.ops.special.computer.Computers;
import net.imagej.table.GenericTable;
import net.imglib2.type.Type;

/**
 * This class creates a result table for a {@link ComputerSetProcessor}.
 *
 * @author Tim-Oliver Buchholz, University of Konstanz.
 *
 * @param <O>
 *            datatype of the table entries
 */
public interface ComputerSetTableService<O extends Type<O>> {

	/**
	 * Create a new {@link GenericTable} to store the results of a
	 * {@link ComputerSetProcessor}.
	 *
	 * @param computerSets
	 *            of the {@link ComputerSetProcessor}
	 * @param names
	 *            a map which maps a unique name to each {@link ComputerSet}
	 * @param numRows
	 *            number of rows
	 * @return a table with a column for each {@link Computers} and numRows
	 *         rows.
	 */
	public GenericTable createTable(final ComputerSet<?, O>[] computerSets, final Map<ComputerSet<?, O>, String> names,
			final int numRows);

	/**
	 * Create a new {@link GenericTable} to store the results of a
	 * {@link ComputerSetProcessor} and the label name. The name of the label is
	 * stored in the first column.
	 *
	 * @param computerSets
	 *            of the {@link ComputerSetProcessor}
	 * @param names
	 *            a map which maps a unique name to each {@link ComputerSet}
	 * @param labelColumnName
	 *            the name of the label column. The column name has to be unique.
	 * @param numRows
	 *            number of rows
	 * @return a table with a column for each {@link Computers} and numRows
	 *         rows.
	 */
	public GenericTable createTable(final ComputerSet<?, O>[] computerSets, final Map<ComputerSet<?, O>, String> names,
			final String labelColumnName, final int numRows);
}
