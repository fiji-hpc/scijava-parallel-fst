package cz.it4i.parallel.fst;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.scijava.plugin.SciJavaPlugin;


public interface ParallelizationParadigmSerializer extends SciJavaPlugin {

	void writeObject(Object obj, ObjectOutput output) throws IOException;

	Object readObject(Class<?> objectClass, ObjectInput input) throws Exception;

	Class<?> getSerializedClass();

	boolean alsoForAllSubclasses();

	boolean willHandleClass(Class<?> clazz);
}
