package cz.it4i.parallel.fst;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.nustaq.serialization.FSTConfiguration;

public class Client {

	public static void main(String[] args) throws Exception
	{
		final FSTConfiguration config = FSTConfiguration
			.createDefaultConfiguration();

		for (int i = 0; i < 100; i++) {
			command(config);
		}

	}

	private static void command(final FSTConfiguration config) throws IOException,
		Exception
	{
		
		try (Socket soc = new Socket("localhost", 9090)) {

			OutputStream os = soc.getOutputStream();
			InputStream is = soc.getInputStream();

			long start = System.currentTimeMillis();
			Map<String, Object> params = new HashMap<>();
			params.put("interval", "some strange interval");
			CommandRunnable cr = new CommandRunnable(ExampleCommand.class
				.getCanonicalName(), params);
			config.encodeToStream(os, cr);

			os.flush();


			cr = (CommandRunnable) config.decodeFromStream(is);
			cr.run();
			params = cr.getInOut();
			out.println("duration " + (System.currentTimeMillis() - start) + "ms");
			start = currentTimeMillis();

			System.out.println("params: " + params);
		}
	}
}
