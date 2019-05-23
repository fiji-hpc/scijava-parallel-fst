package cz.it4i.parallel.fst.utils;

import com.google.common.collect.Streams;

import java.util.List;
import java.util.stream.Collectors;

import org.scijava.Context;
import org.scijava.parallel.ParallelService;
import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.parallel.ParallelizationParadigmProfile;

import cz.it4i.parallel.ImageJServerParadigm;
import cz.it4i.parallel.ImageJServerParadigm.Host;
import cz.it4i.parallel.ServerRunner;
import cz.it4i.parallel.fst.FSTRPCParadigm;
import cz.it4i.parallel.fst.runners.FSTRPCServerRunner;
import cz.it4i.parallel.fst.runners.HPCFSTRPCServerRunnerUI;
import cz.it4i.parallel.fst.runners.InProcessFSTRPCServerRunner;
import cz.it4i.parallel.ui.HPCSettingsGui;
import cz.it4i.parallel.utils.TestParadigm;

public class TestFSTRPCParadigm {

	private TestFSTRPCParadigm() {}

	public static ParallelizationParadigm runner(ServerRunner runner,
		Context context)
	{
		return new RemoteTestParadigm(runner, initParadigm(runner, context));
	}

	public static ParallelizationParadigm hpcFSTRPCServer(
		Context context)
	{
		ServerRunner runner = new HPCFSTRPCServerRunnerUI(HPCSettingsGui.showDialog(
			context), true);
		return new RemoteTestParadigm(runner, initParadigm(runner, context));
	}

	public static ParallelizationParadigm inProcessFSTRPCServer(Context context) {
		ServerRunner runner = new InProcessFSTRPCServerRunner(context);
		return new TestParadigm(runner, initParadigm(runner, context));
	}

	public static ParallelizationParadigm localFSTRPCServer(String fijiPath,
		Context context)
	{
		ServerRunner runner = new FSTRPCServerRunner(fijiPath, true);
		return new TestParadigm(runner, initParadigm(runner, context));
	}

	private static ParallelizationParadigm initParadigm(ServerRunner runner,
		Context context)
	{
		runner.start();
		List<Host> hosts = Streams.zip(runner.getPorts().stream(), runner
			.getNCores().stream(), (port, nCores) -> new ImageJServerParadigm.Host(
				"localhost:" + port, nCores)).collect(Collectors.toList());
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
