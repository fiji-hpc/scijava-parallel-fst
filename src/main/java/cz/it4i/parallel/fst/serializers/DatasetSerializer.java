package cz.it4i.parallel.fst.serializers;

import io.scif.io.ByteArrayHandle;
import io.scif.services.DatasetIOService;
import io.scif.services.LocationService;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.UUID;

import net.imagej.Dataset;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import cz.it4i.parallel.fst.ParallelizationParadigmSerializer;

@Plugin(type = ParallelizationParadigmSerializer.class)
public class DatasetSerializer implements ParallelizationParadigmSerializer {


	private static final int SIZE_OF_CHUNK = 1024 * 1024 * 1024;

	@Parameter
	private LocationService locationService;

	@Parameter
	private DatasetIOService ioService;

	@Override
	public void writeObject(Object toWrite, ObjectOutput out)
		throws IOException
	{
		Dataset dataset = (Dataset) toWrite;
		out.writeUTF(dataset.getName());
		String id = UUID.randomUUID().toString() + "_" + dataset.getName();
		ByteArrayHandle bh = new ByteArrayHandle();
		locationService.mapFile(id, bh);
		ioService.save(dataset, id);
		int pointer = 0;
		byte[] data = bh.getBytes();
		int count = (int) bh.length();
		out.writeInt(count);
		while (pointer < bh.length()) {
			int toRead = (int) Math.min(SIZE_OF_CHUNK, bh.length() - pointer);
			out.write(data, pointer, toRead);
			pointer += toRead;
		}
		bh.close();
	}

	@Override
	public boolean willHandleClass(Class<?> cl) {
		return true;
	}

	@Override
	public Object readObject(Class<?> objectClass, ObjectInput objectInput)
		throws IOException

	{
		String name = objectInput.readUTF();
		String id = UUID.randomUUID().toString() + "_" + name;
		int count = objectInput.readInt();
		ByteArrayHandle bh = new ByteArrayHandle(count);
		locationService.mapFile(id, bh);
		objectInput.read(bh.getBytes(), 0, count);
		Dataset result = ioService.open(id);
		bh.close();
		return result;
	}

	@Override
	public Class<?> getSerializedClass() {
		return Dataset.class;
	}

	@Override
	public boolean alsoForAllSubclasses() {
		return true;
	}

}
