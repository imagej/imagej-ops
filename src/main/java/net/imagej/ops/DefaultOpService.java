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

package net.imagej.ops;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.scijava.command.CommandInfo;
import org.scijava.command.CommandService;
import org.scijava.log.LogService;
import org.scijava.module.Module;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.module.ModuleService;
import org.scijava.plugin.AbstractPTService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.Service;

/**
 * Default service for managing and executing {@link Op}s.
 * 
 * @author Curtis Rueden
 */
@Plugin(type = Service.class)
public class DefaultOpService extends AbstractPTService<Op> implements
	OpService
{

	@Parameter
	private ModuleService moduleService;

	@Parameter
	private CommandService commandService;

	@Parameter
	private OpMatchingService matcher;

	@Parameter
	private LogService log;

	// -- OpService methods --

	@Override
	public Object run(final String name, final Object... args) {
		final Module module = module(name, args);
		return run(module);
	}

	@Override
	public <OP extends Op> Object run(final Class<OP> type, final Object... args) {
		final Module module = module(type, args);
		return run(module);
	}

	@Override
	public Object run(final Op op, final Object... args) {
		return run(module(op, args));
	}

	@Override
	public Op op(final String name, final Object... args) {
		final Module module = module(name, args);
		if (module == null) return null;
		return (Op) module.getDelegateObject();
	}

	@Override
	public <OP extends Op> OP op(final Class<OP> type, final Object... args) {
		final Module module = module(type, args);
		if (module == null) return null;
		@SuppressWarnings("unchecked")
		final OP op = (OP) module.getDelegateObject();
		return op;
	}

	@Override
	public Module module(final String name, final Object... args) {
		return matcher.findModule(new OpRef<Op>(name, args));
	}

	@Override
	public <OP extends Op> Module module(final Class<OP> type,
		final Object... args)
	{
		return matcher.findModule(new OpRef<OP>(type, args));
	}

	@Override
	public Module module(final Op op, final Object... args) {
		final Module module = info(op).createModule(op);
		getContext().inject(module.getDelegateObject());
		return matcher.assignInputs(module, args);
	}

	@Override
	public CommandInfo info(final Op op) {
		return commandService.getCommand(op.getClass());
	}

	@Override
	public Collection<String> ops() {
		// collect list of unique operation names
		final HashSet<String> operations = new HashSet<String>();
		for (final CommandInfo info : matcher.getOps()) {
			final String name = info.getName();
			if (name != null && !name.isEmpty()) operations.add(info.getName());
		}

		// convert the set into a sorted list
		final ArrayList<String> sorted = new ArrayList<String>(operations);
		Collections.sort(sorted);
		return sorted;
	}

	@Override
	public String help(final String name) {
		return help(matcher.findCandidates(name, null));
	}

	@Override
	public <OP extends Op> String help(final Class<OP> type) {
		return help(matcher.findCandidates(null, type));
	}

	@Override
	public String help(final Op op) {
		return help(Collections.singleton(info(op)));
	}

	private String help(final Collection<? extends ModuleInfo> infos) {
		if (infos.size() == 0) {
			return "No such operation.";
		}

		final StringBuilder sb = new StringBuilder("Available operations:");
		for (final ModuleInfo info : infos) {
			sb.append("\n\t" + matcher.getOpString(info));
		}

		if (infos.size() == 1) {
			final ModuleInfo info = infos.iterator().next();
			final String description = info.getDescription();
			if (description != null && !description.isEmpty()) {
				sb.append("\n\n" + description);
			}
		}

		return sb.toString();
	}

	// -- Operation shortcuts --

	@Override
	public Object add(final Object... args) {
		return run(Ops.Add.NAME, args);
	}

	@Override
	public Object ascii(Object... args) {
		return run(Ops.ASCII.NAME, args);
	}

	@Override
	public Object chunker(Object... args) {
		return run(Ops.Chunker.NAME, args);
	}

	@Override
	public Object convert(Object... args) {
		return run(Ops.Convert.NAME, args);
	}

	@Override
	public Object convolve(Object... args) {
		return run(Ops.Convolve.NAME, args);
	}

	@Override
	public Object createimg(Object... args) {
		return run(Ops.CreateImg.NAME, args);
	}

	@Override
	public Object crop(Object... args) {
		return run(Ops.Crop.NAME, args);
	}

	@Override
	public Object deconvolve(Object... args) {
		return run(Ops.Deconvolve.NAME, args);
	}

	@Override
	public Object divide(Object... args) {
		return run(Ops.Divide.NAME, args);
	}

	@Override
	public Object equation(Object... args) {
		return run(Ops.Equation.NAME, args);
	}

	@Override
	public Object fft(Object... args) {
		return run("fft", args);
	}

	@Override
	public Object gauss(Object... args) {
		return run(Ops.Gauss.NAME, args);
	}

	@Override
	public Object gaussKernel(Object... args) {
		return run("gausskernel", args);
	}

	@Override
	public Object histogram(Object... args) {
		return run(Ops.Histogram.NAME, args);
	}

	@Override
	public Object identity(Object... args) {
		return run(Ops.Identity.NAME, args);
	}

	@Override
	public Object ifft(Object... args) {
		return run("ifft", args);
	}

	@Override
	public Object invert(Object... args) {
		return run(Ops.Invert.NAME, args);
	}

	@Override
	public Object join(Object... args) {
		return run(Ops.Join.NAME, args);
	}

	@Override
	public Object logKernel(Object... args) {
		return run("logkernel", args);
	}

	@Override
	public Object lookup(Object... args) {
		return run(Ops.Lookup.NAME, args);
	}

	@Override
	public Object loop(Object... args) {
		return run(Ops.Loop.NAME, args);
	}

	@Override
	public Object map(Object... args) {
		return run(Ops.Map.NAME, args);
	}

	@Override
	public Object max(Object... args) {
		return run(Ops.Max.NAME, args);
	}

	@Override
	public Object mean(Object... args) {
		return run(Ops.Mean.NAME, args);
	}

	@Override
	public Object median(Object... args) {
		return run(Ops.Median.NAME, args);
	}

	@Override
	public Object min(Object... args) {
		return run(Ops.Min.NAME, args);
	}

	@Override
	public Object minmax(Object... args) {
		return run(Ops.MinMax.NAME, args);
	}

	@Override
	public Object multiply(Object... args) {
		return run(Ops.Multiply.NAME, args);
	}

	@Override
	public Object normalize(Object... args) {
		return run(Ops.Normalize.NAME, args);
	}

	@Override
	public Object project(Object... args) {
		return run(Ops.Project.NAME, args);
	}

	@Override
	public Object quantile(Object... args) {
		return run(Ops.Quantile.NAME, args);
	}

	@Override
	public Object scale(Object... args) {
		return run(Ops.Scale.NAME, args);
	}

	@Override
	public Object size(Object... args) {
		return run(Ops.Size.NAME, args);
	}

	@Override
	public Object slicewise(Object... args) {
		return run(Ops.Slicewise.NAME, args);
	}

	@Override
	public Object stddev(Object... args) {
		return run(Ops.StdDeviation.NAME, args);
	}

	@Override
	public Object subtract(Object... args) {
		return run(Ops.Subtract.NAME, args);
	}

	@Override
	public Object sum(Object... args) {
		return run(Ops.Sum.NAME, args);
	}

	@Override
	public Object threshold(Object... args) {
		return run(Ops.Threshold.NAME, args);
	}

	@Override
	public Object variance(Object... args) {
		return run(Ops.Variance.NAME, args);
	}

	// -- SingletonService methods --

	@Override
	public Class<Op> getPluginType() {
		return Op.class;
	}

	// -- Helper methods --

	private Object run(final Module module) {
		module.run();
		return result(module);
	}

	private Object result(final Module module) {
		final List<Object> outputs = new ArrayList<Object>();
		for (final ModuleItem<?> output : module.getInfo().outputs()) {
			final Object value = output.getValue(module);
			outputs.add(value);
		}
		return outputs.size() == 1 ? outputs.get(0) : outputs;
	}

	// -- Deprecated methods --

	@Deprecated
	@Override
	public Object create(Object... args) {
		return createimg(args);
	}

}
