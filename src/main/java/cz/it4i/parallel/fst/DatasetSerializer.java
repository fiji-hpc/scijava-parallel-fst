package cz.it4i.parallel.fst;

import io.scif.io.ByteArrayHandle;
import io.scif.services.DatasetIOService;
import io.scif.services.LocationService;

import java.io.IOException;
import java.util.UUID;

import net.imagej.Dataset;

import org.nustaq.serialization.FSTClazzInfo;
import org.nustaq.serialization.FSTClazzInfo.FSTFieldInfo;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.nustaq.serialization.FSTObjectSerializer;


public class DatasetSerializer implements FSTObjectSerializer {


	private static final int SIZE_OF_CHUNK = 1024 * 1024 * 1024;
	private LocationService locationService;
	private DatasetIOService ioService;

	public DatasetSerializer(DatasetIOService ioService,
		LocationService locationService)
	{
		this.ioService = ioService;
		this.locationService = locationService;
	}

	@Override
	public void writeObject(FSTObjectOutput out, Object toWrite,
		FSTClazzInfo clzInfo, FSTFieldInfo referencedBy, int streamPosition)
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
	public void readObject(FSTObjectInput in, Object toRead, FSTClazzInfo clzInfo,
		FSTFieldInfo referencedBy) throws Exception
	{

	}

	@Override
	public boolean willHandleClass(@SuppressWarnings("rawtypes") Class cl) {
		return true;
	}

	@Override
	public boolean alwaysCopy() {
		return false;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object instantiate(Class objectClass, FSTObjectInput fstObjectInput,
		FSTClazzInfo serializationInfo, FSTFieldInfo referencee, int streamPosition)
		throws Exception
	{
		String name = fstObjectInput.readStringUTF();
		String id = UUID.randomUUID().toString() + "_" + name;
		int count = fstObjectInput.readInt();
		ByteArrayHandle bh = new ByteArrayHandle(count);
		locationService.mapFile(id, bh);
		fstObjectInput.read(bh.getBytes(), 0, count);
		Dataset result = ioService.open(id);
		bh.close();
		return result;
	}

}
