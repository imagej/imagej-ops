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

import java.util.Collection;

import net.imagej.ImageJService;
import net.imagej.ops.create.CreateOps;
import net.imagej.ImgPlus;
import net.imagej.ops.logic.LogicNamespace;
import net.imagej.ops.math.MathNamespace;
import net.imagej.ops.threshold.ThresholdNamespace;

import org.scijava.command.CommandInfo;
import org.scijava.module.Module;
import org.scijava.plugin.PTService;

/**
 * Interface for services that manage and execute {@link Op}s.
 *
 * @author Curtis Rueden
 */
public interface OpService extends PTService<Op>, ImageJService {

	/**
	 * Executes the given operation with the specified arguments. The best
	 * {@link Op} implementation to use will be selected automatically from the
	 * operation name and arguments.
	 *
	 * @param name
	 *            The operation to execute. If multiple {@link Op}s share this
	 *            name, then the best {@link Op} implementation to use will be
	 *            selected automatically from the name and arguments.
	 * @param args
	 *            The operation's arguments.
	 * @return The result of the execution. If the {@link Op} has no outputs,
	 *         this will return {@code null}. If exactly one output, it will be
	 *         returned verbatim. If more than one, a {@code List<Object>} of
	 *         the outputs will be given.
	 */
	Object run(String name, Object... args);

	/**
	 * Executes the operation of the given type with the specified arguments.
	 * The best {@link Op} implementation to use will be selected automatically
	 * from the operation type and arguments.
	 *
	 * @param type
	 *            The {@link Class} of the operation to execute. If multiple
	 *            {@link Op}s share this type (e.g., the type is an interface
	 *            which multiple {@link Op}s implement), then the best
	 *            {@link Op} implementation to use will be selected
	 *            automatically from the type and arguments.
	 * @param args
	 *            The operation's arguments.
	 * @return The result of the execution. If the {@link Op} has no outputs,
	 *         this will return {@code null}. If exactly one output, it will be
	 *         returned verbatim. If more than one, a {@code List<Object>} of
	 *         the outputs will be given.
	 */
	<OP extends Op> Object run(Class<OP> type, Object... args);

	/**
	 * Executes the given {@link Op} with the specified arguments.
	 *
	 * @param op
	 *            The {@link Op} to execute.
	 * @param args
	 *            The operation's arguments.
	 * @return The result of the execution. If the {@link Op} has no outputs,
	 *         this will return {@code null}. If exactly one output, it will be
	 *         returned verbatim. If more than one, a {@code List<Object>} of
	 *         the outputs will be given.
	 */
	Object run(Op op, Object... args);

	/**
	 * Gets the best {@link Op} to use for the given operation and arguments,
	 * populating its inputs.
	 *
	 * @param name
	 *            The name of the operation. If multiple {@link Op}s share this
	 *            name, then the best {@link Op} implementation to use will be
	 *            selected automatically from the name and arguments.
	 * @param args
	 *            The operation's arguments.
	 * @return An {@link Op} with populated inputs, ready to run.
	 */
	Op op(String name, Object... args);

	/**
	 * Gets the best {@link Op} to use for the given operation type and
	 * arguments, populating its inputs.
	 *
	 * @param type
	 *            The {@link Class} of the operation. If multiple {@link Op}s
	 *            share this type (e.g., the type is an interface which multiple
	 *            {@link Op}s implement), then the best {@link Op}
	 *            implementation to use will be selected automatically from the
	 *            type and arguments.
	 * @param args
	 *            The operation's arguments.
	 * @return An {@link Op} with populated inputs, ready to run.
	 */
	<O extends Op> O op(Class<O> type, Object... args);

	/**
	 * Gets the best {@link Op} to use for the given operation and arguments,
	 * wrapping it as a {@link Module} with populated inputs.
	 *
	 * @param name
	 *            The name of the operation.
	 * @param args
	 *            The operation's arguments.
	 * @return A {@link Module} wrapping the best {@link Op}, with populated
	 *         inputs, ready to run.
	 */
	Module module(String name, Object... args);

	/**
	 * Gets the best {@link Op} to use for the given operation type and
	 * arguments, wrapping it as a {@link Module} with populated inputs.
	 *
	 * @param type
	 *            The required type of the operation. If multiple {@link Op}s
	 *            share this type (e.g., the type is an interface which multiple
	 *            {@link Op}s implement), then the best {@link Op}
	 *            implementation to use will be selected automatically from the
	 *            type and arguments.
	 * @param args
	 *            The operation's arguments.
	 * @return A {@link Module} wrapping the best {@link Op}, with populated
	 *         inputs, ready to run.
	 */
	<OP extends Op> Module module(Class<OP> type, Object... args);

	/**
	 * Wraps the given {@link Op} as a {@link Module}, populating its inputs.
	 *
	 * @param op
	 *            The {@link Op} to wrap and populate.
	 * @param args
	 *            The operation's arguments.
	 * @return A {@link Module} wrapping the {@link Op}, with populated inputs,
	 *         ready to run.
	 */
	Module module(Op op, Object... args);

	/** Gets the metadata for a given {@link Op}. */
	CommandInfo info(Op op);

	/** Gets the names of all available operations. */
	Collection<String> ops();

	// -- Operation shortcuts - global namespace --

	/** Executes the "ascii" operation on the given arguments. */
	@OpMethod(op = Ops.ASCII.class)
	Object ascii(Object... args);

	/** Executes the "chunker" operation on the given arguments. */
	@OpMethod(op = Ops.Chunker.class)
	Object chunker(Object... args);

	/** Executes the "convert" operation on the given arguments. */
	@OpMethod(op = Ops.Convert.class)
	Object convert(Object... args);

	/** Executes the "convolve" operation on the given arguments. */
	@OpMethod(op = Ops.Convolve.class)
	Object convolve(Object... args);

	/** Executes the "correlate" operation on the given arguments. */
	@OpMethod(op = Ops.Correlate.class)
	Object correlate(Object... args);

	/** Executes the "create" operation on the given arguments. */
	@OpMethod(op = Ops.Create.class)
	Object create(Object... args);

	/** Executes the "crop" operation on the given arguments. */
	@OpMethod(op = Ops.Crop.class)
	Object crop(Object... args);

	/** Executes the "deconvolve" operation on the given arguments. */
	@OpMethod(op = Ops.Deconvolve.class)
	Object deconvolve(Object... args);

	/** Executes the "equation" operation on the given arguments. */
	@OpMethod(op = Ops.Equation.class)
	Object equation(Object... args);

	/** Executes the "eval" operation on the given arguments. */
	@OpMethod(op = Ops.Eval.class)
	Object eval(Object... args);

	/** Executes the "fft" operation on the given arguments. */
	@OpMethod(op = Ops.FFT.class)
	Object fft(Object... args);

	/** Executes the "fftsize" operation on the given arguments. */
	@OpMethod(op = Ops.FFTSize.class)
	Object fftsize(Object... args);

	/** Executes the "gauss" operation on the given arguments. */
	@OpMethod(op = Ops.Gauss.class)
	Object gauss(Object... args);

	/** Executes the "gausskernel" operation on the given arguments. */
	@OpMethod(op = Ops.GaussKernel.class)
	Object gaussKernel(Object... args);

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = Ops.Help.class)
	Object help(Object... args);

	/** Executes the "histogram" operation on the given arguments. */
	@OpMethod(op = Ops.Histogram.class)
	Object histogram(Object... args);

	/** Executes the "identity" operation on the given arguments. */
	@OpMethod(op = Ops.Identity.class)
	Object identity(Object... args);

	/** Executes the "ifft" operation on the given arguments. */
	@OpMethod(op = Ops.IFFT.class)
	Object ifft(Object... args);

	/** Executes the "invert" operation on the given arguments. */
	@OpMethod(op = Ops.Invert.class)
	Object invert(Object... args);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = Ops.Join.class)
	Object join(Object... args);

	/** Executes the "log" operation on the given arguments. */
	@OpMethod(op = Ops.Log.class)
	Object log(Object... args);

	/** Executes the "logkernel" operation on the given arguments. */
	@OpMethod(op = Ops.LogKernel.class)
	Object logKernel(Object... args);

	/** Executes the "lookup" operation on the given arguments. */
	@OpMethod(op = Ops.Lookup.class)
	Object lookup(Object... args);

	/** Executes the "loop" operation on the given arguments. */
	@OpMethod(op = Ops.Loop.class)
	Object loop(Object... args);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = Ops.Map.class)
	Object map(Object... args);

	/** Executes the "max" operation on the given arguments. */
	@OpMethod(op = Ops.Max.class)
	Object max(Object... args);

	/** Executes the "mean" operation on the given arguments. */
	@OpMethod(op = Ops.Mean.class)
	Object mean(Object... args);

	/** Executes the "median" operation on the given arguments. */
	@OpMethod(op = Ops.Median.class)
	Object median(Object... args);

	/** Executes the "min" operation on the given arguments. */
	@OpMethod(op = Ops.Min.class)
	Object min(Object... args);

	/** Executes the "minmax" operation on the given arguments. */
	@OpMethod(op = Ops.MinMax.class)
	Object minmax(Object... args);

	/** Executes the "normalize" operation on the given arguments. */
	@OpMethod(op = Ops.Normalize.class)
	Object normalize(Object... args);

	/** Executes the "project" operation on the given arguments. */
	@OpMethod(op = Ops.Project.class)
	Object project(Object... args);

	/** Executes the "quantile" operation on the given arguments. */
	@OpMethod(op = Ops.Quantile.class)
	Object quantile(Object... args);

	/** Executes the "scale" operation on the given arguments. */
	@OpMethod(op = Ops.Scale.class)
	Object scale(Object... args);

	/** Executes the "size" operation on the given arguments. */
	@OpMethod(op = Ops.Size.class)
	Object size(Object... args);

	/** Executes the "slicewise" operation on the given arguments. */
	@OpMethod(op = Ops.Slicewise.class)
	Object slicewise(Object... args);

	/** Executes the "stddev" operation on the given arguments. */
	@OpMethod(op = Ops.StdDeviation.class)
	Object stddev(Object... args);

	/** Executes the "subtract" operation on the given arguments. */
	@OpMethod(op = MathOps.Subtract.class)
	Object subtract(Object... args);

	/** Executes the "sum" operation on the given arguments. */
	@OpMethod(op = Ops.Sum.class)
	Object sum(Object... args);

	/** Executes the "threshold" operation on the given arguments. */
	@OpMethod(op = Ops.Threshold.class)
	Object threshold(Object... args);

	/** Executes the "variance" operation on the given arguments. */
	@OpMethod(op = Ops.Variance.class)
	Object variance(Object... args);

	// -- CreateOps short-cuts --

	/** Executes the "createimg" operation on the given arguments. */
	@OpMethod(op = CreateOps.CreateImg.class)
	Object createimg(Object... args);

	/** Executes the "createimglabeling" operation on the given arguments. */
	@OpMethod(op = CreateOps.CreateImgLabeling.class)
	Object createimglabeling(Object... args);

	/** Executes the "createimgfactory" operation on the given arguments. */
	@OpMethod(op = CreateOps.CreateImgFactory.class)
	Object createimgfactory(Object... args);

	/** Executes the "createtype" operation. */
	@OpMethod(op = CreateOps.CreateType.class)
	Object createtype();

	// -- Operation shortcuts - other namespaces --

	/** Gateway into ops of the "logic" namespace. */
	LogicNamespace logic();

	/** Gateway into ops of the "math" namespace. */
	MathNamespace math();

	/** Gateway into ops of the "threshold" namespace. */
	ThresholdNamespace threshold();

}
