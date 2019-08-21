
package cz.it4i.parallel.fst.runners;

import java.util.Collections;
import java.util.List;

import org.scijava.Context;
import org.scijava.parallel.Status;
import org.scijava.plugin.Parameter;

import cz.it4i.parallel.fst.server.FSTRPCServer;
import cz.it4i.parallel.runners.RunnerSettings;
import cz.it4i.parallel.runners.ServerRunner;

public class InProcessFSTRPCServerRunner implements
	ServerRunner<RunnerSettings>
{

	private FSTRPCServer server;

	private Status status = Status.NON_ACTIVE;

	@Parameter
	private Context ctx;

	public InProcessFSTRPCServerRunner() {
	}

	public InProcessFSTRPCServerRunner(Context ctx) {
		this.ctx = ctx;
	}

	public boolean isInitialized() {
		return ctx != null;
	}

	@Override
	public InProcessFSTRPCServerRunner init(RunnerSettings settings) {
		return this;
	}

	@Override
	public void start() {
		server = new FSTRPCServer(ctx);
		status = Status.ACTIVE;
		server.run();
		getPorts().parallelStream().forEach(Wait4FSTRPCServer::doIi);
	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public List<Integer> getPorts() {
		return Collections.singletonList(9090);
	}

	@Override
	public List<Integer> getNCores() {
		return Collections.singletonList(Runtime.getRuntime()
			.availableProcessors());
	}

	@Override
	public void letShutdownOnClose() {
		// it is always shutdowned during close
	}

	@Override
	public void close() {
		shutdown();
	}

	private void shutdown() {
		status = Status.NON_ACTIVE;
		server.stop();
		server = null;
	}

}
