package cz.it4i.parallel.fst.utils;

import java.util.List;

import org.scijava.parallel.ParallelizationParadigm;

import cz.it4i.parallel.RunningRemoteServer;
import cz.it4i.parallel.ServerRunner;
import cz.it4i.parallel.utils.TestParadigm;

public class RemoteTestParadigm extends TestParadigm implements
	RunningRemoteServer
{

	private ServerRunner server;

	public RemoteTestParadigm(ServerRunner runner,
		ParallelizationParadigm paradigm)
	{
		super(runner, paradigm);
		this.server = runner;
	}

	@Override
	public List<Integer> getRemotePorts() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> getRemoteHosts() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Integer> getNCores() {
		return server.getNCores();
	}

}
