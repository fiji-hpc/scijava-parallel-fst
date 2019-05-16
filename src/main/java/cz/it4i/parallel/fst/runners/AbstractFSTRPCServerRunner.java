
package cz.it4i.parallel.fst.runners;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import cz.it4i.parallel.ServerRunner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public abstract class AbstractFSTRPCServerRunner implements AutoCloseable, ServerRunner {

	protected static final List<String> FSTPRPC_SERVER_PARAMETERS = Arrays.asList(
		"-Dimagej.legacy.modernOnlyCommands=true", "--", "--ij2", "--headless",
		"--fstrpcserver");

	private final boolean shutdownOnClose;

	@Override
	public void start() {

		try {
			doStartImageJServer();
			getPorts().parallelStream().forEach(Wait4FSTRPCServer::doIi);
		}
		catch (IOException exc) {
			log.error("start imageJServer", exc);
			throw new RuntimeException(exc);
		}
	}

	@Override
	public abstract List<Integer> getPorts();

	@Override
	public abstract int getNCores();

	@Override
	public void close() {
		if (shutdownOnClose) {
			shutdown();
		}
	}

	protected abstract void doStartImageJServer()
		throws IOException;



}
