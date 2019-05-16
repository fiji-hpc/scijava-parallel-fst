package cz.it4i.parallel.fst;

import java.util.Collections;
import java.util.List;

import org.scijava.Context;

import cz.it4i.parallel.ServerRunner;


public class InProcessFSTRPCServerRunner implements ServerRunner {

	private FSTRPCServer server;

	public InProcessFSTRPCServerRunner(Context ctx) {
		server = new FSTRPCServer();
		ctx.inject(server);
	}

	@Override
	public void start() {
		server.run();
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
