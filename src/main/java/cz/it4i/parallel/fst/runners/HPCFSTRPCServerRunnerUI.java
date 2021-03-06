
package cz.it4i.parallel.fst.runners;

import cz.it4i.parallel.paradigm_managers.ui.HPCImageJRunnerWithUI;

public class HPCFSTRPCServerRunnerUI extends HPCImageJRunnerWithUI {

	public HPCFSTRPCServerRunnerUI()
	{
		super(FSTRPCServerRunner.FSTPRPC_SERVER_PARAMETERS, Wait4FSTRPCServer::doIt,
			FSTRPCServerRunner.PORT_NUMBER);
	}

	@Override
	protected String getServerName() {
		return "RPC-FST server";
	}

}
