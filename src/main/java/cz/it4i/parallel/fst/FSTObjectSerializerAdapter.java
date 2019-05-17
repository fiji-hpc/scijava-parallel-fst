package cz.it4i.parallel.fst;

import java.io.IOException;

import org.nustaq.serialization.FSTClazzInfo;
import org.nustaq.serialization.FSTClazzInfo.FSTFieldInfo;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.nustaq.serialization.FSTObjectSerializer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FSTObjectSerializerAdapter implements FSTObjectSerializer {

	private final ParallelizationParadigmSerializer serializer;

	@Override
	public void writeObject(FSTObjectOutput out, Object toWrite,
		FSTClazzInfo clzInfo, FSTFieldInfo referencedBy, int streamPosition)
		throws IOException
	{
		serializer.writeObject(toWrite, out);
	}

	@Override
	public void readObject(FSTObjectInput in, Object toRead, FSTClazzInfo clzInfo,
		FSTFieldInfo referencedBy) throws Exception
	{
		// do nothing readed during instantiation
	}

	@Override
	public boolean willHandleClass(@SuppressWarnings("rawtypes") Class cl) {
		return serializer.willHandleClass(cl);
	}

	@Override
	public boolean alwaysCopy() {
		return false;
	}

	@Override
	public Object instantiate(@SuppressWarnings("rawtypes") Class objectClass,
		FSTObjectInput fstObjectInput,
		FSTClazzInfo serializationInfo, FSTFieldInfo referencee, int streamPosition)
		throws Exception
	{
		return serializer.readObject(objectClass, fstObjectInput);
	}
}
