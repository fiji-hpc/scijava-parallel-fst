package cz.it4i.parallel.fst.paradigm_managers;

import java.util.Map;

import org.scijava.Context;
import org.scijava.parallel.ParadigmManager;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import cz.it4i.parallel.fst.FSTRPCParadigm;
import cz.it4i.parallel.fst.runners.FSTRPCServerRunner;
import cz.it4i.parallel.runners.ImageJServerRunnerSettings;
import cz.it4i.parallel.runners.MultipleHostsParadigmManagerUsingRunner;
import cz.it4i.parallel.ui.ImageJSettingsGui;

@Plugin(type = ParadigmManager.class)
public class LocalFSTRPCProfileManager extends
	MultipleHostsParadigmManagerUsingRunner<FSTRPCParadigm, ImageJServerRunnerSettings>
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

	@Override
	protected ImageJServerRunnerSettings doEdit(Map<String, Object> inputs) {
		return ImageJSettingsGui.showDialog(ctx, inputs);
	}

	@Override
	protected void fillInputs(ImageJServerRunnerSettings settings,
		Map<String, Object> inputs)
	{
		ImageJSettingsGui.fillInputs(settings, inputs);
	}
}
