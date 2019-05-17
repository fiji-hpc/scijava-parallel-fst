package cz.it4i.parallel.fst.serializers;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.NativeType;

import org.scijava.plugin.Plugin;

import cz.it4i.parallel.fst.InputStreamObjectInput;
import cz.it4i.parallel.fst.OutputStreamObjectOutput;
import cz.it4i.parallel.fst.ParallelizationParadigmSerializer;
import de.mpicbg.ulman.imgstreamer.RandomAccessibleIntervalStreamer;

@Plugin(type = ParallelizationParadigmSerializer.class)
public class RandomAccessibleIntervalSerializer implements
	ParallelizationParadigmSerializer
{



	@SuppressWarnings("unchecked")
	@Override
	public void writeObject(Object toWrite, ObjectOutput out)
		throws IOException
	{
		RandomAccessibleIntervalStreamer.write(
			(RandomAccessibleInterval<? extends NativeType<?>>) toWrite,
			new OutputStreamObjectOutput(out));
	}

	@Override
	public boolean willHandleClass(Class<?> cl) {
		return true;
	}

	@Override
	public Object readObject(Class<?> objectClass, ObjectInput objectInput)
		throws Exception
	{
		return RandomAccessibleIntervalStreamer.read(new InputStreamObjectInput(
			objectInput));
	}

	@Override
	public Class<?> getSerializedClass() {
		return RandomAccessibleInterval.class;
	}

	@Override
	public boolean alsoForAllSubclasses() {
		return true;
	}

}
