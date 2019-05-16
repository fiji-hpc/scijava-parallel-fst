package cz.it4i.parallel.fst;

import java.util.List;
import java.util.stream.Collectors;

import org.scijava.Context;
import org.scijava.parallel.ParallelService;
import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.parallel.ParallelizationParadigmProfile;

import cz.it4i.parallel.ServerRunner;
import cz.it4i.parallel.ImageJServerParadigm;
import cz.it4i.parallel.ImageJServerParadigm.Host;

public class TestFSTRPCParadigm {

	public static ParallelizationParadigm initParadigm(ServerRunner runner,
		Context context)
	{
		runner.start();
		int nCores = runner.getNCores();
		List<Host> hosts = runner.getPorts().stream().map(
			port -> new ImageJServerParadigm.Host("localhost:" + port, nCores))
			.collect(Collectors.toList());
		return configureParadigm(context.service(ParallelService.class), hosts);
	}

	private static ParallelizationParadigm configureParadigm(
		ParallelService parallelService, List<Host> hosts)
	{
		parallelService.deleteProfiles();
		parallelService.addProfile(new ParallelizationParadigmProfile(
			FSTRPCParadigm.class, "lonelyBiologist01"));
		parallelService.selectProfile("lonelyBiologist01");

		ParallelizationParadigm paradigm = parallelService.getParadigm();
		((FSTRPCParadigm) paradigm).setHosts(hosts);
		paradigm.init();
		return paradigm;
	}
}
