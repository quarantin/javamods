package zombie.javamods;

import java.util.ArrayList;
import java.util.List;


public class JavaModImpl extends JavaMod {

	private List<Class<?>> exposedClasses;
	private List<Object> globalObjects;

	public JavaModImpl() {
		this.exposedClasses = new ArrayList<>();
		this.globalObjects = new ArrayList<>();
	}

	public List<Class<?>> getExposedClasses() {
		return this.exposedClasses;
	}

	public List<Object> getGlobalObjects() {
		return this.globalObjects;
	}

	public void addExposedClass(Class<?> classs) {
		this.exposedClasses.add(classs);
	}

	public void addGlobalObject(Object object) {
		this.globalObjects.add(object);
	}
}
