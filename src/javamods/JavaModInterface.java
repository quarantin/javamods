package javamods;

import java.util.List;

import javamods.Patch;


public interface JavaModInterface {

	/*
	 * Returns a list of class to expose through Lua API.
	 */
	default public List<Class<?>> getClassesToExpose() {
		return null;
	}

	/*
	 * Returns a list of object declaring global functions to expose to Lua API.
	 * The global functions have to be declared with the @LuaMethod annotation.
	 */
	default public List<Object> getObjectsWithGlobalFunctions() {
		return null;
	}

	/*
	 * Returns a list of patch to apply.
	 */
	default public List<Patch> getPatches() {
		return null;
	}

	/*
	 * Called at startup. This is where you can initialize your Java mod if needed.
	 */
	default public void startup() {
	}
}
