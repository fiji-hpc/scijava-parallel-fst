package cz.it4i.parallel.fst;

import java.io.IOException;

import net.imagej.Dataset;
import net.imagej.DefaultDataset;
import net.imagej.ImgPlus;
import net.imglib2.type.numeric.RealType;

import org.nustaq.serialization.FSTClazzInfo;
import org.nustaq.serialization.FSTClazzInfo.FSTFieldInfo;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.nustaq.serialization.FSTObjectSerializer;
import org.scijava.Context;

import de.mpicbg.ulman.imgstreamer.ImgStreamer;


public class DatasetSerializer implements FSTObjectSerializer {


	private Context ctx;

	public DatasetSerializer() {
		ctx = new Context();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void writeObject(FSTObjectOutput out, Object toWrite,
		FSTClazzInfo clzInfo, FSTFieldInfo referencedBy, int streamPosition)
		throws IOException
	{
		ImgStreamer imgStreamer = new ImgStreamer(null);
		Dataset ds = (Dataset) toWrite;
		@SuppressWarnings("rawtypes")
		ImgPlus ip = ds.getImgPlus();
		imgStreamer.setImageForStreaming(ip);
		imgStreamer.write(new OutputStreamObjectOutput(out));
	}

	@Override
	public void readObject(FSTObjectInput in, Object toRead, FSTClazzInfo clzInfo,
		FSTFieldInfo referencedBy) throws Exception
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean willHandleClass(Class cl) {
		return true;
	}

	@Override
	public boolean alwaysCopy() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object instantiate(Class objectClass, FSTObjectInput fstObjectInput,
		FSTClazzInfo serializationInfo, FSTFieldInfo referencee, int streamPosition)
		throws Exception
	{
		ImgStreamer imgStreamer = new ImgStreamer(null);
		ImgPlus<? extends RealType<?>> ip = imgStreamer.readAsRealTypedImg(
			new InputStreamObjectInput(fstObjectInput));
		return new DefaultDataset(ctx, ip);
	}

}
