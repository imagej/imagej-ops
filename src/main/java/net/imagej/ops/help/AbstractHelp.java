/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
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

package net.imagej.ops.help;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.imagej.ops.Op;
import net.imagej.ops.OpCandidate;
import net.imagej.ops.OpUtils;
import net.imagej.ops.Ops;

import org.scijava.ItemIO;
import org.scijava.module.ModuleInfo;
import org.scijava.plugin.Parameter;

/**
 * Base class for help operations.
 *
 * @author Curtis Rueden
 */
public abstract class AbstractHelp implements Ops.Help {

	@Parameter(type = ItemIO.OUTPUT)
	private String help;

	protected <OP extends Op> void help(final List<OpCandidate<OP>> candidates) {
		final ArrayList<ModuleInfo> infos = new ArrayList<ModuleInfo>();
		for (final OpCandidate<OP> candidate : candidates) {
			infos.add(candidate.getInfo());
		}
		help(infos);
	}

	protected void help(final Collection<? extends ModuleInfo> infos) {
		if (infos.size() == 0) {
			help = "No such operation.";
			return;
		}

		final StringBuilder sb = new StringBuilder("Available operations:");
		for (final ModuleInfo info : infos) {
			sb.append("\n\t" + OpUtils.opString(info));
		}

		if (infos.size() == 1) {
			final ModuleInfo info = infos.iterator().next();
			final String description = info.getDescription();
			if (description != null && !description.isEmpty()) {
				sb.append("\n\n" + description);
			}
		}

		help = sb.toString();
	}

}
