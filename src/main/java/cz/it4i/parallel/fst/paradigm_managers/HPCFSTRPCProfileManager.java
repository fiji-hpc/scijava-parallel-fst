package cz.it4i.parallel.fst.paradigm_managers;

import java.util.Map;

import org.scijava.Context;
import org.scijava.parallel.ParadigmManager;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import cz.it4i.parallel.fst.FSTRPCParadigm;
import cz.it4i.parallel.fst.runners.HPCFSTRPCServerRunnerUI;
import cz.it4i.parallel.runners.HPCSettings;
import cz.it4i.parallel.runners.MultipleHostsParadigmManagerUsingRunner;
import cz.it4i.parallel.ui.HPCSettingsGui;

@Plugin(type = ParadigmManager.class)
public class HPCFSTRPCProfileManager extends
	MultipleHostsParadigmManagerUsingRunner<FSTRPCParadigm, HPCSettings>
{

	@Parameter
	private Context ctx;

	@Override
	public Class<FSTRPCParadigm> getSupportedParadigmType() {
		return FSTRPCParadigm.class;
	}

	@Override
	protected Class<HPCFSTRPCServerRunnerUI> getTypeOfRunner() {
		return HPCFSTRPCServerRunnerUI.class;
	}

	@Override
	public String toString() {
		return "FSTRPC on HPC";
	}

	@Override
	protected HPCSettings doEdit(Map<String, Object> inputs) {
		return HPCSettingsGui.showDialog(ctx, inputs);
	}

	@Override
	protected void fillInputs(HPCSettings settings,
		Map<String, Object> inputs)
	{
		HPCSettingsGui.fillInputs(settings, inputs);
	}
}
