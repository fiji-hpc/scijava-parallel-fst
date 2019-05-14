package cz.it4i.parallel.fst;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;


public class OutputStreamObjectOutput extends OutputStream {

	private ObjectOutput dataOutput;

	public OutputStreamObjectOutput(ObjectOutput dataOutput) {
		this.dataOutput = dataOutput;
	}

	@Override
	public void write(int b) throws IOException {
		dataOutput.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		dataOutput.write(b, off, len);
	}


}
