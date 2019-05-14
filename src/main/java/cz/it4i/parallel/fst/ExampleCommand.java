package cz.it4i.parallel.fst;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Command.class)
public class ExampleCommand implements Command{

	@Parameter
	private String interval;

	@Parameter(type = ItemIO.OUTPUT)
	private String out;

	@Override
	public void run() {
		System.err.println("##############################################");
		System.err.println(interval);
		System.err.println("##############################################");
		out = "result from server";
	}
}
