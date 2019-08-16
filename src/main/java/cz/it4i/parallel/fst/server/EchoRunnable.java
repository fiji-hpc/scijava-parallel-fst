package cz.it4i.parallel.fst.server;

import java.io.Serializable;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EchoRunnable implements Runnable, Serializable
{


	private static final long serialVersionUID = -6276734570857266755L;

	private boolean ranRemotely;
	private String in;

	@Getter
	private String out;

	public EchoRunnable(String message) {
		super();
		this.in = message;
	}

	@Override
	public synchronized void run() {

		if (!ranRemotely) {
			log.info("Message: " + in);
			out = in;
			ranRemotely = true;
			in = null;
		}
	}
}
