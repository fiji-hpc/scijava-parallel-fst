package cz.it4i.parallel.fst.server;

import java.util.List;

import org.scijava.Priority;
import org.scijava.parallel.ParallelService;
import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.parallel.ParallelizationParadigmProfile;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.Service;

import cz.it4i.parallel.ImageJServerParadigm.Host;
import cz.it4i.parallel.fst.FSTRPCParadigm;
import cz.it4i.parallel.plugins.DefaultRequestBrokerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Plugin(type = Service.class, priority = Priority.NORMAL + 10)
public class FSTRPCRequestBrokerService extends DefaultRequestBrokerService {

	private static final String FSTRPC_SERVER_PARADIGM = "FSTRPCServerParadigm";

	@Parameter
	private ParallelService parallelService;

	private boolean paradigmInitialized;

	@Override
	public synchronized void initParallelizationParadigm(List<String> hostNames,
		List<Integer> ncores)
	{
		if (!paradigmInitialized) {
			parallelService.deleteProfiles();
			parallelService.addProfile(new ParallelizationParadigmProfile(
				FSTRPCParadigm.class, FSTRPC_SERVER_PARADIGM));
			parallelService.selectProfile(FSTRPC_SERVER_PARADIGM);
			ParallelizationParadigm paradigm = parallelService.getParadigm();
			((FSTRPCParadigm) paradigm).setHosts(Host
				.constructListFromNamesAndCores(hostNames, ncores));
			paradigm.init();
			paradigmInitialized = true;
		}
		else {
			log.info("Parallelization paradigm already initialized");
		}
	}
}
