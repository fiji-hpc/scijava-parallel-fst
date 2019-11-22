package cz.it4i.parallel.fst.paradigm_managers;

import org.scijava.Context;
import org.scijava.parallel.ParadigmManager;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import cz.it4i.parallel.fst.FSTRPCParadigm;
import cz.it4i.parallel.fst.runners.InProcessFSTRPCServerRunner;
import cz.it4i.parallel.paradigm_managers.MultipleHostsParadigmManagerUsingRunner;
import cz.it4i.parallel.paradigm_managers.ParadigmProfileWithSettings;
import cz.it4i.parallel.paradigm_managers.RunnerSettings;
import cz.it4i.parallel.paradigm_managers.ServerRunner;

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
	protected boolean editSettings(
		ParadigmProfileWithSettings<RunnerSettings> typedProfile)
	{
		// It does not have any setting so it should always return true:
		return true;
	}
	
	@Override
	protected Class<InProcessFSTRPCServerRunner> getTypeOfRunner() {
		return InProcessFSTRPCServerRunner.class;
	}

	@Override
	protected void initRunner(ServerRunner<?> runner) {
		if (runner instanceof InProcessFSTRPCServerRunner) {
			InProcessFSTRPCServerRunner typedRunner =
				(InProcessFSTRPCServerRunner) runner;
			if (!typedRunner.isInitialized()) {
				context.inject(typedRunner);
			}
		}

	}

	@Override
	public String toString() {
		return "Inprocess FSTRPC";
	}
}
