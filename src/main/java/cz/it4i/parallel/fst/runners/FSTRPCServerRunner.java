
package cz.it4i.parallel.fst.runners;

import java.util.Arrays;
import java.util.List;

import cz.it4i.parallel.paradigm_managers.LocalImageJRunner;

public class FSTRPCServerRunner extends LocalImageJRunner {

	static final List<String> FSTPRPC_SERVER_PARAMETERS = Arrays.asList(
		"-Dimagej.legacy.modernOnlyCommands=true", "--", "--ij2", "--headless",
		"--fstrpcserver");

	static final int PORT_NUMBER = 9090;

	public FSTRPCServerRunner() {
		super(FSTPRPC_SERVER_PARAMETERS, Wait4FSTRPCServer::doIt, PORT_NUMBER);
	}

}
