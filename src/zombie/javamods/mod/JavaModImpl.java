package zombie.javamods.mod;

import java.util.ArrayList;
import java.util.List;


public class JavaModImpl extends JavaMod {

	private List<Class<?>> classesToExpose;
	private List<Object> objectsWithGlobalFunctions;

	public JavaModImpl() {
		this.classesToExpose = new ArrayList<>();
		this.objectsWithGlobalFunctions = new ArrayList<>();
	}

	@Override
	public List<Class<?>> getClassesToExpose() {
		return this.classesToExpose;
	}

	@Override
	public List<Object> getObjectsWithGlobalFunctions() {
		return this.objectsWithGlobalFunctions;
	}

	@Override
	public List<Class<?>> getServerCommands() {
		return null;
	}

	@Override
	public void startup() {
	}

	public void addClassToExpose(Class<?> classs) {
		this.classesToExpose.add(classs);
	}

	public void addGlobalFunction(Object object) {
		this.objectsWithGlobalFunctions.add(object);
	}
}
