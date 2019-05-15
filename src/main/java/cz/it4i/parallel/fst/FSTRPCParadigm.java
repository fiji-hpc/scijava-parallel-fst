package cz.it4i.parallel.fst;

import io.scif.services.DatasetIOService;
import io.scif.services.LocationService;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.scijava.command.CommandService;
import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import cz.it4i.parallel.ImageJServerParadigm.Host;
import cz.it4i.parallel.ParallelWorker;
import cz.it4i.parallel.SimpleOstravaParadigm;

@Plugin(type = ParallelizationParadigm.class)
public class FSTRPCParadigm extends SimpleOstravaParadigm {

	@Parameter
	private DatasetIOService ioService;

	@Parameter
	private LocationService locationService;

	private List<String> hosts = new LinkedList<>();

	public void setHosts(final Collection<Host> hosts) {
		this.hosts.clear();
		this.hosts.addAll(hosts.stream().map(Host::getName).collect(
			Collectors.toList()));
		int ncores = hosts.iterator().next().getNCores();
		if (!hosts.stream().allMatch(host -> host.getNCores() == ncores)) {
			throw new UnsupportedOperationException(
				"Only hosts with same number of cores are supported");
		}
	}

	// -- SimpleOstravaParadigm methods --

	@Override
	protected void initWorkerPool() {
		hosts.forEach(host -> workerPool.addWorker(createWorker(host)));
	}

	private ParallelWorker createWorker(String host) {
		final String[] tokensOfHost = host.split(":");
		int port = Integer.parseInt(tokensOfHost[1]);
		host = tokensOfHost[0];
		return new FSTRPCWorker(host, port, ioService, locationService);
	}

}
