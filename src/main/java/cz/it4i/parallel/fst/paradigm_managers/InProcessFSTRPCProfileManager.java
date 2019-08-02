package cz.it4i.parallel.fst.paradigm_managers;

import org.scijava.Context;
import org.scijava.parallel.ParadigmManager;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import cz.it4i.parallel.fst.FSTRPCParadigm;
import cz.it4i.parallel.fst.runners.InProcessFSTRPCServerRunner;
import cz.it4i.parallel.runners.MultipleHostsParadigmManagerUsingRunner;
import cz.it4i.parallel.runners.RunnerSettings;
import cz.it4i.parallel.runners.ServerRunner;

@Plugin(type = ParadigmManager.class)
public class InProcessFSTRPCProfileManager extends
	MultipleHostsParadigmManagerUsingRunner<FSTRPCParadigm, RunnerSettings>
{

	@Parameter
	private Context context;

	@Override
	public Class<FSTRPCParadigm> getSupportedParadigmType() {
		return FSTRPCParadigm.class;
	}


	@Override
	protected Class<InProcessFSTRPCServerRunner> getTypeOfRunner() {
		return InProcessFSTRPCServerRunner.class;
	}

	@Override
	protected void initRunner(ServerRunner<?> runner) {
		context.inject(runner);
	}

	@Override
	public String toString() {
		return "Inprocess FSTRPC";
	}
}
