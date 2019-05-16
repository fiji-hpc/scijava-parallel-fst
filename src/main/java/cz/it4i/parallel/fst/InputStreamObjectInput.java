package cz.it4i.parallel.fst;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;


public class InputStreamObjectInput extends InputStream {

	private ObjectInput dataInput;

	public InputStreamObjectInput(ObjectInput dataInput) {
		super();
		this.dataInput = dataInput;
	}

	@Override
	public int read() throws IOException {
		return dataInput.read();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return dataInput.read(b, off, len);
	}


}
