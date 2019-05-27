package cz.it4i.parallel.fst;

import io.scif.services.DatasetIOService;
import io.scif.services.LocationService;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.nustaq.serialization.FSTConfiguration;
import org.scijava.Context;
import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.PluginService;

import cz.it4i.parallel.MultipleHostParadigm;
import cz.it4i.parallel.ParallelWorker;
import cz.it4i.parallel.AbstractBaseParadigm;
import cz.it4i.parallel.Host;

@Plugin(type = ParallelizationParadigm.class)
public class FSTRPCParadigm extends AbstractBaseParadigm implements
	MultipleHostParadigm
{

	@Parameter
	private DatasetIOService ioService;

	@Parameter
	private LocationService locationService;

	@Parameter
	private PluginService pluginService;

	@Parameter
	private Context context;

	private List<String> hosts = new LinkedList<>();

	@Override
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
		FSTConfiguration config = createConfigWithRegisteredSerializers(
			pluginService);
		
		return new FSTRPCWorker(host, port, config);
	}

	public static FSTConfiguration createConfigWithRegisteredSerializers(
		PluginService pluginService)
	{
		FSTConfiguration config = FSTConfiguration.createDefaultConfiguration();
		pluginService.createInstancesOfType(ParallelizationParadigmSerializer.class)
			.stream().forEach(s -> register(s, config));
		return config;
	}

	private static void register(ParallelizationParadigmSerializer s,
		FSTConfiguration config)
	{
		config.registerSerializer(s.getSerializedClass(),
			new FSTObjectSerializerAdapter(s), s
			.alsoForAllSubclasses());
	}

}
