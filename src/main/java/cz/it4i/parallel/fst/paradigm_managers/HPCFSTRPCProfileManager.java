package cz.it4i.parallel.fst.paradigm_managers;

import org.scijava.Context;
import org.scijava.parallel.ParadigmManager;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import cz.it4i.parallel.fst.FSTRPCParadigm;
import cz.it4i.parallel.fst.runners.HPCFSTRPCServerRunnerUI;
import cz.it4i.parallel.runners.HPCSettings;
import cz.it4i.parallel.runners.MultipleHostsParadigmManagerUsingRunner;
import cz.it4i.parallel.runners.ServerRunner;
import cz.it4i.parallel.ui.HPCImageJServerRunnerWithUI;
import cz.it4i.parallel.ui.HavingOwnerWindow;
import javafx.stage.Window;

@Plugin(type = ParadigmManager.class)
public class HPCFSTRPCProfileManager extends
	MultipleHostsParadigmManagerUsingRunner<FSTRPCParadigm, HPCSettings>
	implements HavingOwnerWindow<Window>
{

	@Parameter
	private Context ctx;

	private Window ownerWindow;

	@Override
	public Class<Window> getType() {
		return Window.class;
	}

	@Override
	public void setOwner(Window parent) {
		ownerWindow = parent;
	}

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
	protected void initRunner(ServerRunner<?> runner) {
		HPCImageJServerRunnerWithUI typedRunner =
			(HPCImageJServerRunnerWithUI) runner;
		typedRunner.initOwnerWindow(ownerWindow);
	}
}
