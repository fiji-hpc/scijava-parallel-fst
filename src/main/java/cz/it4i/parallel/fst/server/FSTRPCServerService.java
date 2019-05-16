package cz.it4i.parallel.fst.server;

import net.imagej.ImageJService;

public interface FSTRPCServerService extends ImageJService {

	FSTRPCServer start();
}
