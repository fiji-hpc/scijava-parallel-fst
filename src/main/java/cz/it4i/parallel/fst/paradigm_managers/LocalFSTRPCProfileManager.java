package cz.it4i.parallel.fst.paradigm_managers;

import org.scijava.Context;
import org.scijava.parallel.ParadigmManager;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import cz.it4i.parallel.fst.FSTRPCParadigm;
import cz.it4i.parallel.fst.runners.FSTRPCServerRunner;
import cz.it4i.parallel.paradigm_managers.LocalImageJRunnerSettings;
import cz.it4i.parallel.paradigm_managers.MultipleHostsParadigmManagerUsingRunner;

@Plugin(type = ParadigmManager.class)
public class LocalFSTRPCProfileManager extends
	MultipleHostsParadigmManagerUsingRunner<FSTRPCParadigm, LocalImageJRunnerSettings>
{

	@Parameter
	private Context ctx;

	@Override
	public Class<FSTRPCParadigm> getSupportedParadigmType() {
		return FSTRPCParadigm.class;
	}

	@Override
	protected Class<FSTRPCServerRunner> getTypeOfRunner() {
		return FSTRPCServerRunner.class;
	}

	@Override
	public String toString() {
		return "Local FSTRPC";
	}

}
