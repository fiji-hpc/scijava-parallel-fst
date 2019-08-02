
package cz.it4i.parallel.fst.runners;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cz.it4i.parallel.runners.ImageJServerRunner;

public class FSTRPCServerRunner extends ImageJServerRunner {

	static final List<String> FSTPRPC_SERVER_PARAMETERS = Arrays.asList(
		"-Dimagej.legacy.modernOnlyCommands=true", "--", "--ij2", "--headless",
		"--fstrpcserver");

	@Override
	public List<Integer> getPorts() {
		return Collections.singletonList(9090);
	}

	@Override
	protected List<String> getParameters() {
		return FSTPRPC_SERVER_PARAMETERS;
	}

	@Override
	protected void waitForServer(Integer port) {
		Wait4FSTRPCServer.doIi(port);
	}

}
