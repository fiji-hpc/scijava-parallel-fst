
package cz.it4i.parallel.fst;

import io.scif.services.DatasetIOService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.imagej.Dataset;

import org.nustaq.serialization.FSTConfiguration;
import org.scijava.Context;

public class Serializer {

	private static DatasetIOService ioService;

	public static void main(String[] args) throws IOException {
		final Context context = new Context();
		ioService = context.service(DatasetIOService.class);
		FSTConfiguration config = FSTConfiguration.createDefaultConfiguration();
		Dataset dataset = ioService.open(ExampleImage.lenaAsTempFile().toString());

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		config.encodeToStream(bos, dataset);
	}
}