package cz.it4i.parallel.fst.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.scijava.Context;
import org.scijava.parallel.ParallelizationParadigm;

import cz.it4i.parallel.Host;
import cz.it4i.parallel.MultipleHostParadigm;
import cz.it4i.parallel.RunningRemoteServer;
import cz.it4i.parallel.runners.ServerRunner;
import cz.it4i.parallel.utils.TestParadigm;

public class RemoteTestParadigm extends TestParadigm implements
	RunningRemoteServer, MultipleHostParadigm
{

	private ServerRunner server;
	private ParallelizationParadigm paradigm;

	public RemoteTestParadigm(ServerRunner runner,
		ParallelizationParadigm paradigm)
	{
		super(runner, paradigm);
		this.server = runner;
		this.paradigm = paradigm;
	}

	public RemoteTestParadigm(ServerRunner runner, Context context) {
		super(runner, context);
		this.server = runner;
	}

	@Override
	public List<Integer> getRemotePorts() {
		if (server instanceof RunningRemoteServer) {
			return ((RunningRemoteServer) server).getRemotePorts();

		}
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> getRemoteHosts() {
		if (server instanceof RunningRemoteServer) {
			return ((RunningRemoteServer) server).getRemoteHosts();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Integer> getNCores() {
		return server.getNCores();
	}

	@Override
	public void setHosts(Collection<Host> hosts) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> getHosts() {

		if (paradigm instanceof MultipleHostParadigm) {
			MultipleHostParadigm multipleHostParadigm =
				(MultipleHostParadigm) paradigm;
			return multipleHostParadigm.getHosts();
		}
		throw new UnsupportedOperationException("paradigm: " + paradigm.getClass() +
			" is not " + MultipleHostParadigm.class.getSimpleName());
	}

	@Override
	public List<Map<String, Object>> runOnHosts(String commandName,
		Map<String, Object> parameters, List<String> hosts)
	{
		if (paradigm instanceof MultipleHostParadigm) {
			MultipleHostParadigm multipleHostParadigm =
				(MultipleHostParadigm) paradigm;
			return multipleHostParadigm.runOnHosts(commandName, parameters, hosts);
		}
		throw new UnsupportedOperationException("paradigm: " + paradigm.getClass() +
			" is not " + MultipleHostParadigm.class.getSimpleName());
	}

}
