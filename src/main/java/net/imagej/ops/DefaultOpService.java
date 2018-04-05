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

package net.imagej.ops;

import java.util.ArrayList;
import java.util.Collection;

import org.scijava.command.CommandInfo;
import org.scijava.command.CommandService;
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
	private CommandService commandService;

	@Parameter
	private OpMatchingService matcher;

	@Parameter
	private NamespaceService namespaceService;

	// -- OpEnvironment methods --

	@Override
	public OpMatchingService matcher() {
		return matcher;
	}

	@Override
	public OpInfo info(final Class<? extends Op> type) {
		// TODO: Consider maintaining a separate and efficient
		// OpInfo data structure in AbstractOpEnvironment.
		final CommandInfo cInfo = commandService.getCommand(type);
		return cInfo == null ? null : new OpInfo(cInfo);
	}

	@Override
	public Collection<OpInfo> infos() {
		// TODO: Consider maintaining a separate and efficient
		// OpInfo data structure in AbstractOpEnvironment.
		final ArrayList<OpInfo> infos = new ArrayList<>();
		for (final CommandInfo cInfo : commandService.getCommandsOfType(Op.class)) {
			infos.add(new OpInfo(cInfo));
		}
		return infos;
	}

	@Override
	public <NS extends Namespace> NS namespace(Class<NS> nsClass) {
		return namespaceService.create(nsClass, this);
	}

	// -- SingletonService methods --

	@Override
	public Class<Op> getPluginType() {
		return Op.class;
	}

}
