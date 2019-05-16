
package cz.it4i.parallel.fst.runners;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.server.UID;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import cz.it4i.parallel.Routines;
import cz.it4i.parallel.SciJavaParallelRuntimeException;
import cz.it4i.parallel.ServerRunner;
import cz.it4i.parallel.fst.server.EchoRunnable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public abstract class AbstractFSTRPCServerRunner implements AutoCloseable, ServerRunner {

	private long waitTimeout = 200;

	public final static List<String> IMAGEJ_SERVER_PARAMETERS = Arrays.asList(
		"-Dimagej.legacy.modernOnlyCommands=true", "--", "--ij2", "--headless",
		"--fstrpcserver");

	private final boolean shutdownOnClose;

	@Override
	public void start() {

		try {
			doStartImageJServer();
			getPorts().parallelStream().forEach( this::waitForImageJServer );
		}
		catch (IOException exc) {
			log.error("start imageJServer", exc);
			throw new RuntimeException(exc);
		}
	}

	@Override
	public abstract List<Integer> getPorts();

	@Override
	public abstract int getNCores();

	@Override
	public void close() {
		if (shutdownOnClose) {
			shutdown();
		}
	}

	protected abstract void doStartImageJServer()
		throws IOException;

	private void waitForImageJServer( Integer port )
	{

		while (true) {
			try {
				if (checkPort(port)) {
					break;
				}
				Routines.runWithExceptionHandling(() -> Thread.sleep(waitTimeout));
			}
			catch (IOException e) {
				// ignore waiting for start
			}
			catch (ClassNotFoundException exc) {
				throw new SciJavaParallelRuntimeException(exc);
			}
		}
	}

	private boolean checkPort(Integer port) throws IOException,
		ClassNotFoundException
	{
		try (Socket soc = new Socket("localhost", port)) {
			try (ObjectOutputStream os = new ObjectOutputStream(soc
				.getOutputStream());
					ObjectInputStream is = new ObjectInputStream(soc.getInputStream()))
			{
				String str = UUID.randomUUID().toString();
				os.writeObject(new EchoRunnable(str));
				os.flush();
				EchoRunnable result = (EchoRunnable) is.readObject();
				return result.getOut().equals(str);
			}
		}
	}


}
