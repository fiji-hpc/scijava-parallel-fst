package cz.it4i.parallel.fst;

import static cz.it4i.parallel.Routines.rethrowAsUnchecked;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

import org.nustaq.serialization.FSTConfiguration;

import cz.it4i.parallel.ParallelWorker;


public class FSTRPCWorker implements ParallelWorker {

	private String host;
	private int port;
	private FSTConfiguration config;

	public FSTRPCWorker(String host, int port) {
		this.host = host;
		this.port = port;
		config = FSTConfiguration.createDefaultConfiguration();
	}

	@Override
	public Map<String, Object> executeCommand(String commandTypeName,
		Map<String, ?> inputs)
	{

		try (Socket soc = new Socket(host, port)) {

			OutputStream os = soc.getOutputStream();
			InputStream is = soc.getInputStream();

			@SuppressWarnings("unchecked")
			Map<String, Object> params = (Map<String, Object>) inputs;


			CommandRunnable cr = new CommandRunnable(ExampleCommand.class
				.getCanonicalName(), params);
			config.encodeToStream(os, cr);

			os.flush();

			cr = (CommandRunnable) config.decodeFromStream(is);
			cr.run();
			return cr.getInOut();
		}
		catch (Exception exc) {
			rethrowAsUnchecked(exc);
			return null;
		}
	}

}
