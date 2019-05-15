
package test;

import static cz.it4i.parallel.Routines.runWithExceptionHandling;

import io.scif.services.DatasetIOService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.imagej.Dataset;
import net.imagej.DefaultDataset;
import net.imagej.ImgPlus;
import net.imagej.plugins.commands.imglib.RotateImageXY;
import net.imglib2.type.numeric.RealType;

import org.scijava.Context;
import org.scijava.parallel.ParallelizationParadigm;

import cz.it4i.parallel.Routines;
import cz.it4i.parallel.ServerRunner;
import cz.it4i.parallel.fst.ExampleImage;
import cz.it4i.parallel.fst.InProcessFSTRPCServerRunner;
import cz.it4i.parallel.fst.TestFSTRPCParadigm;
import cz.it4i.parallel.utils.TestParadigm;
import de.mpicbg.ulman.imgstreamer.ImgStreamer;

public class Proof {

	private static final String OUTPUT_DIRECTORY = "output";

	private final static int step = 170;

	private static DatasetIOService ioService;

	public static void main(String[] args) throws IOException {
		final Context context = new Context();
		ioService = context.service(DatasetIOService.class);
		Dataset dataset = ioService.open(ExampleImage.lenaAsTempFile()
			.toString());

		ImgStreamer is = new ImgStreamer(null);
		is.setImageForStreaming((ImgPlus) dataset.getImgPlus());
		Path path = Paths.get("/tmp/out.dat");
		try (OutputStream os = Files.newOutputStream(path)) {
			is.write(os);
		}

		is = new ImgStreamer(null);
		Dataset out;
		try (InputStream ins = Files.newInputStream(path)) {
			ImgPlus<? extends RealType<?>> img = is.readAsRealTypedImg(ins);
			out = new DefaultDataset(context, img);
		}
		Files.deleteIfExists(path);
		ioService.save(out, "/tmp/lena.png");
	}

	static void callRemotePlugin(final ParallelizationParadigm paradigm)
		throws IOException
	{
		if (ioService == null) {
			ioService = new Context().getService(DatasetIOService.class);
		}
		final List<Map<String, Object>> parametersList = initParameters(ioService);
		final List<Map<String, Object>> results = paradigm.runAll(
				RotateImageXY.class, parametersList);
		saveOutputs( parametersList, results );
	}

	static List<Map<String, Object>> initParameters(
		DatasetIOService ioServiceLocal)
		throws IOException
	{
		final List<Map<String, Object>> parametersList = new LinkedList<>();
		Dataset dataset = ioServiceLocal.open(ExampleImage.lenaAsTempFile()
			.toString());
		for (double angle = step; angle < 360; angle += step) {
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("dataset", dataset);
			parameters.put("angle", angle);
			parametersList.add(parameters);
		}
		return parametersList;
	}

	private static void saveOutputs( List< Map< String, Object > > parametersList, List< Map< String, Object > > results )
	{
		final Path outputDirectory = prepareOutputDirectory();
		final Iterator<Map<String, Object>> inputIterator = parametersList
				.iterator();
		for (Map<String, ?> result : results) {
			final Double angle = ( Double ) inputIterator.next().get( "angle" );
			final Path outputFile = outputDirectory.resolve("result_" + angle +
				".png");
			runWithExceptionHandling(() -> ioService.save((Dataset) result.get(
				"dataset"), outputFile.toString()));
		}
	}

	static Path prepareOutputDirectory() {
		Path outputDirectory = Paths.get(OUTPUT_DIRECTORY);
		if (!Files.exists(outputDirectory)) {
			Routines.runWithExceptionHandling(() -> Files.createDirectories(
				outputDirectory));
		}
		return outputDirectory;
	}
}
