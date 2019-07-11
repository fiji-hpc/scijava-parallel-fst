package cz.it4i.parallel.fst;

import io.scif.services.DatasetIOService;
import io.scif.services.LocationService;

import org.nustaq.serialization.FSTConfiguration;
import org.scijava.Context;
import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.PluginService;

import cz.it4i.parallel.AbstractMultipleHostParadigm;
import cz.it4i.parallel.ParallelWorker;

@Plugin(type = ParallelizationParadigm.class)
public class FSTRPCParadigm extends AbstractMultipleHostParadigm
{

	@Parameter
	private DatasetIOService ioService;

	@Parameter
	private LocationService locationService;

	@Parameter
	private PluginService pluginService;

	@Parameter
	private Context context;

	@Override
	protected ParallelWorker createWorker(String host) {
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
