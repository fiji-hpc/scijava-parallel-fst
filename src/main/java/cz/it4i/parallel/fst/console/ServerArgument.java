/*
 * #%L
 * ImageJ server for RESTful access to ImageJ.
 * %%
 * Copyright (C) 2013 - 2016 Board of Regents of the University of
 * Wisconsin-Madison.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package cz.it4i.parallel.fst.console;

import java.util.LinkedList;

import org.scijava.console.AbstractConsoleArgument;
import org.scijava.console.ConsoleArgument;
import org.scijava.object.ObjectService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.startup.StartupService;
import org.scijava.ui.UIService;

import cz.it4i.parallel.fst.server.FSTRPCServer;
import cz.it4i.parallel.fst.server.FSTRPCServerService;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles the {@code --fstrpcserver} argument to signal that an FSTRPC server
 * should be started immediately when ImageJ launches.
 *
 */

@Slf4j
@Plugin(type = ConsoleArgument.class)
public class ServerArgument extends AbstractConsoleArgument {

	@Parameter(required = false)
	private FSTRPCServerService fstrpcServerService;

	@Parameter(required = false)
	private ObjectService objectService;

	@Parameter(required = false)
	private UIService uiService;

	@Parameter(required = false)
	private StartupService startupService;

	// -- Constructor --

	public ServerArgument() {
		super(1, "--fstrpcserver");
	}

	// -- ConsoleArgument methods --

	@Override
	public void handle(final LinkedList<String> args) {
		if (!supports(args)) return;

		args.removeFirst(); // --server

		final FSTRPCServer server = fstrpcServerService.start();
		objectService.addObject(server);

		if (startupService != null) {
			startupService.addOperation(() -> {
				// In headless mode, block until server shuts down.
				if (uiService == null || !uiService.isHeadless()) return;
				try {
					server.join();
				}
				catch (final InterruptedException exc) {
					if (log != null) log.error(exc.getMessage(), exc);
					Thread.currentThread().interrupt();
				}
			});
		}
	}

	// -- Typed methods --
	@Override
	public boolean supports(final LinkedList<String> args) {
		return fstrpcServerService != null && objectService != null &&
			super.supports(args);
	}
}
