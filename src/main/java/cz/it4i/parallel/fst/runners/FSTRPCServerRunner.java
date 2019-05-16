
package cz.it4i.parallel.fst.runners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FSTRPCServerRunner extends AbstractFSTRPCServerRunner {

	private Process fstrpcServerProcess;

	private String fijiExecutable;

	public FSTRPCServerRunner(String fiji, boolean shutdownOnClose) {
		super(shutdownOnClose);
		fijiExecutable = fiji;
	}

	@Override
	public void shutdown() {
		if (fstrpcServerProcess != null) {
			fstrpcServerProcess.destroy();
		}
	}

	@Override
	public int getNCores() {
		return Runtime.getRuntime().availableProcessors();
	}

	@Override
	public List<Integer> getPorts() {
		return Collections.singletonList(9090);
	}

	@Override
	protected void doStartImageJServer() throws IOException {
		String fijiPath = fijiExecutable;
		if (fijiPath == null || !Files.exists(Paths.get(fijiPath))) {
			throw new IllegalArgumentException(
				"Cannot find the specified ImageJ or Fiji executable (" + fijiPath +
					"). The property 'Fiji.executable.path' may not be configured properly in the 'configuration.properties' file.");
		}

		List<String> command = Stream.concat(Stream.of(fijiPath),
			FSTPRPC_SERVER_PARAMETERS.stream()).collect(Collectors.toList());

		final ProcessBuilder pb = new ProcessBuilder(command).inheritIO();
		fstrpcServerProcess = pb.start();

	}

}
