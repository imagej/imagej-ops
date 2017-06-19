/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 Board of Regents of the University of
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

import org.scijava.Priority;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.module.process.AbstractPreprocessorPlugin;
import org.scijava.module.process.PreprocessorPlugin;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * {@link PreprocessorPlugin} implementation that looks up requested
 * {@link Namespace} implementations, obtaining their singleton instance handled
 * by the {@link NamespaceService}.
 *
 * @author Mark Hiner
 */
@Plugin(type = PreprocessorPlugin.class, priority = Priority.HIGH_PRIORITY)
public class NamespacePreprocessor extends AbstractPreprocessorPlugin {

	@Parameter(required = false)
	private NamespaceService nsService;

	@Parameter(required = false)
	private OpService ops;

	@Override
	public void process(final Module module) {
		if (nsService == null) return;

		for (final ModuleItem<?> input : module.getInfo().inputs()) {
			assignNamespace(module, input);
		}
	}

	// -- Helper methods --

	@SuppressWarnings("unchecked")
	private <T> void assignNamespace(final Module module,
		final ModuleItem<T> item)
	{
		if (module.isInputResolved(item.getName())) return;

		// if possible, extract the OpEnvironment from the delegate object
		final Object delegate = module.getDelegateObject();
		final OpEnvironment env = delegate instanceof Environmental ? //
			((Environmental) delegate).ops() : ops;
		if (env == null) return;

		T defaultValue = null;
		if (Namespace.class.isAssignableFrom(item.getType())) {
			defaultValue = (T) nsService.create(//
				(Class<? extends Namespace>) item.getType(), env);
		}
		if (defaultValue == null) return;

		item.setValue(module, defaultValue);
		module.resolveInput(item.getName());
	}
}
