package cz.it4i.parallel.fst.runners;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

import org.nustaq.serialization.FSTConfiguration;

import cz.it4i.parallel.Routines;
import cz.it4i.parallel.SciJavaParallelRuntimeException;
import cz.it4i.parallel.fst.server.EchoRunnable;

class Wait4FSTRPCServer {

	private static long waitTimeout = 200;

	private Wait4FSTRPCServer() {}

	public static void doIi(Integer port) {

		while (true) {
			try {
				if (checkPort(port)) {
					break;
				}
				Routines.runWithExceptionHandling(() -> Thread.sleep(waitTimeout));
			}
			catch (IOException exc) {
				// ignore
			}
			catch (Exception exc) {
				throw new SciJavaParallelRuntimeException(exc);
			}
		}
	}

	static private boolean checkPort(Integer port) throws Exception {
		final FSTConfiguration config = FSTConfiguration
			.createDefaultConfiguration();
		try (Socket soc = new Socket("localhost", port)) {
			try (OutputStream os = soc.getOutputStream()) {
				String str = UUID.randomUUID().toString();
				config.encodeToStream(os, new EchoRunnable(str));
				os.flush();

				try (InputStream is = soc.getInputStream()) {
					EchoRunnable result = (EchoRunnable) config.decodeFromStream(is);
					return result.getOut().equals(str);
				}

			}
		}
	}
}
