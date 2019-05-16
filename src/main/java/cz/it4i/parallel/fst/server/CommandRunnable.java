package cz.it4i.parallel.fst.server;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import cz.it4i.parallel.SciJavaParallelRuntimeException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandRunnable implements Runnable, Serializable,
	ClientOfCommandExecutor
{

	private String command;

	@Getter
	private Map<String, Object> inOut;

	private boolean ranRemotely;

	private Throwable t;

	private transient CommandExecutor executor;

	public CommandRunnable(String command, Map<String, Object> inputs) {
		super();
		this.command = command;
		this.inOut = inputs;
	}

	@Override
	public synchronized void run() {

		if (!ranRemotely) {
			log.info("Running: " + command);
			try {
				inOut = executor.run(command, inOut);
			}
			catch (ExecutionException exc) {
				t = exc;
			}
			ranRemotely = true;
			command = null;
		}
		else {
			if (t != null) {
				if (t instanceof Error) {
					throw (Error) t;
				}
				if (t instanceof RuntimeException) {
					throw (RuntimeException) t;
				}
				throw new SciJavaParallelRuntimeException(t);
			}
		}
	}

	@Override
	public void setCommandExecutor(CommandExecutor executor) {
		this.executor = executor;
	}

}
