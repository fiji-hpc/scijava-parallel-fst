
package cz.it4i.parallel.fst.runners;

import java.util.Arrays;
import java.util.List;

import cz.it4i.parallel.runners.HPCSettings;
import cz.it4i.parallel.ui.HPCImageJServerRunnerWithUI;

public class HPCFSTRPCServerRunnerUI extends HPCImageJServerRunnerWithUI {

	public HPCFSTRPCServerRunnerUI(HPCSettings settings) {
		super(settings);
	}

	public HPCFSTRPCServerRunnerUI(HPCSettings settings, boolean shutdownOnClose) {
		super(settings, shutdownOnClose);
	}

	static final List<String> FSTPRPC_SERVER_PARAMETERS = Arrays.asList(
		"-Dimagej.legacy.modernOnlyCommands=true", "--", "--ij2", "--headless",
		"--fstrpcserver");


	@Override
	protected List<String> getParameters() {
		return FSTPRPC_SERVER_PARAMETERS;
	}

	@Override
	protected void waitForImageJServer(Integer port) {
		Wait4FSTRPCServer.doIi(port);
	}

	@Override
	protected int getStartPort() {
		return 9090;
	}

}
