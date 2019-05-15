
package cz.it4i.parallel.fst;

import io.scif.services.DatasetIOService;
import io.scif.services.LocationService;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.imagej.Dataset;

import org.nustaq.serialization.FSTConfiguration;
import org.scijava.command.CommandService;
import org.scijava.plugin.Parameter;

import cz.it4i.parallel.Routines;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FSTRPCServer {

	@Parameter
	private CommandService commandService;

	@Parameter
	private DatasetIOService ioService;

	@Parameter
	private LocationService locationService;

	private ExecutorService es;
	private ServerSocket serverSocket;
	private Thread mainThread;

	public void run() {
		if (es != null) {
			return;
		}
		es = Executors.newCachedThreadPool();
		serverSocket = Routines.supplyWithExceptionHandling(() -> new ServerSocket(
			9090));
		mainThread = new Thread(() -> Routines.runWithExceptionHandling(
			this::handleServerSocket));
		mainThread.start();
	}

	public void stop() {
		mainThread.interrupt();
		es.shutdown();
		Routines.runWithExceptionHandling(() -> serverSocket.close());
	}

	private void handleServerSocket() throws IOException {
		final FSTConfiguration config = FSTConfiguration
			.createDefaultConfiguration();
		config.registerSerializer(Dataset.class, new DatasetSerializer(ioService,
			locationService), true);
		while (!Thread.interrupted()) {
			try {
				final Socket s = serverSocket.accept();
				es.submit(() -> handleConnection(config, s));
			}
			catch (InterruptedIOException | SocketException exc) {
				Thread.currentThread().interrupt();
			}
		}
	}

	private void handleConnection(FSTConfiguration config, Socket s) {
		try (Socket localS = s) {
			OutputStream os = localS.getOutputStream();
			InputStream is = localS.getInputStream();
			Object obj;
			while (null != (obj = readObject(config, is))) {
				if (obj instanceof ClientOfCommandExecutor) {
					ClientOfCommandExecutor ce = (ClientOfCommandExecutor) obj;
					ce.setCommandExecutor((commandName, input) -> {
						try {
							return commandService.run(commandName, false, input).get()
								.getOutputs();
						}
						catch (InterruptedException exc) {
							log.warn(exc.getMessage(), exc);
							Thread.currentThread().interrupt();
							return Collections.emptyMap();
						}
					});
				}

				Runnable run = (Runnable) obj;
				run.run();
				config.encodeToStream(os, run);
				os.flush();
			}
		}
		catch (Exception exc) {
			log.error(exc.getMessage(), exc);
		}
	}

	private Object readObject(final FSTConfiguration config, InputStream is)
		throws Exception
	{

		try {
			return config.decodeFromStream(is);
		}
		catch (EOFException e) {
			return null;
		}
	}
}
