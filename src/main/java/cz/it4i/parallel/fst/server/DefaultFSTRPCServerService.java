package cz.it4i.parallel.fst.server;

import java.util.HashSet;
import java.util.Set;

import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;
import org.scijava.service.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Default implementation of {@link FSTRPCServerService}.
 *
 */
@Slf4j
@Plugin(type = Service.class)
public class DefaultFSTRPCServerService extends AbstractService implements
	FSTRPCServerService
{

	private Set<FSTRPCServer> servers = new HashSet<>();


	@Override
	public FSTRPCServer start() {
		final FSTRPCServer app = new FSTRPCServer(context()) {

			@Override
			public void stop() {
				servers.remove(this);
				super.stop();
			}
		};
		app.run();
		servers.add(app);
		return app;
	}

	@Override
	public void dispose() {
		for (final FSTRPCServer server : servers) {
			try {
				server.stop();
			}
			catch (final RuntimeException exc) {
				log.error(exc.getMessage(), exc);
			}
		}
	}

}
