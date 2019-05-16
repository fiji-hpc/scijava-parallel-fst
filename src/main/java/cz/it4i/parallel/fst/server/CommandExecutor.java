package cz.it4i.parallel.fst.server;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface CommandExecutor {

	Map<String, Object> run(String commandName, Map<String, Object> input)
		throws ExecutionException;
}
