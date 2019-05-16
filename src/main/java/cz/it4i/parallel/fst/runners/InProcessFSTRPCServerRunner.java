
package cz.it4i.parallel.fst.runners;

import java.util.Collections;
import java.util.List;

import org.scijava.Context;

import cz.it4i.parallel.ServerRunner;
import cz.it4i.parallel.fst.server.FSTRPCServer;

public class InProcessFSTRPCServerRunner implements ServerRunner {

	private FSTRPCServer server;

	public InProcessFSTRPCServerRunner(Context ctx) {
		server = new FSTRPCServer(ctx);
	}

	@Override
	public void start() {
		server.run();
		getPorts().parallelStream().forEach(Wait4FSTRPCServer::doIi);
	}

	@Override
	public List<Integer> getPorts() {
		return Collections.singletonList(9090);
	}

	@Override
	public int getNCores() {
		return Runtime.getRuntime().availableProcessors();
	}

	@Override
	public void shutdown() {
		server.stop();

	}

	@Override
	public void close() {
		shutdown();
	}



}
