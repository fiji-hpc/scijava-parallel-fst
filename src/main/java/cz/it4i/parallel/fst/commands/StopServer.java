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

package cz.it4i.parallel.fst.commands;

import java.util.List;

import org.scijava.command.Command;
import org.scijava.log.LogService;
import org.scijava.object.ObjectService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

import cz.it4i.parallel.fst.server.FSTRPCServer;

/**
 * Stops the FSTRPC Server, if one is running.
 */
@Plugin(type = Command.class,
	menuPath = "Plugins > Utilities > Stop FST RPC Server")
public class StopServer implements Command {

	@Parameter
	private ObjectService objectService;

	@Parameter(required = false)
	private UIService ui;

	@Parameter
	private LogService log;

	@Override
	public void run() {
		final List<FSTRPCServer> servers = //
			objectService.getObjects(FSTRPCServer.class);
		if (!servers.isEmpty()) {
			try {
				servers.get(0).stop();
				if (ui != null) {
					ui.showDialog("ImageJ Server stopped successfully!", "ImageJ Server");
				}
			}
			catch (final Exception exc) {
				log.error(exc);
			}
		}
	}
}
