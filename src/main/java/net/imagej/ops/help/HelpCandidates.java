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

package net.imagej.ops.help;

import net.imagej.ops.Op;
import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.special.SpecialOp;

import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Gets documentation for ops of the given name and/or type.
 *
 * @author Curtis Rueden
 */
@Plugin(type = Ops.Help.class,
	description = "Gets documentation for ops of the given name and/or type.")
public class HelpCandidates extends AbstractHelp {

	@Parameter
	private OpService ops;

	@Parameter
	private OpMatchingService matcher;

	@Parameter(required = false)
	private LogService log;

	@Parameter(required = false)
	private String name;

	@Parameter(required = false)
	private Class<? extends Op> opType;

	@Parameter(required = false)
	private Integer arity;

	@Parameter(required = false)
	private SpecialOp.Flavor flavor;

	@Override
	public void run() {
		final int a = arity == null ? -1 : arity;
		help(SpecialOp.candidates(ops, name, opType, a, flavor));
	}

}
