package zombie.javamods;

import java.util.List;


public interface JavaModInterface {

	/*
	 * Returns a list of class to expose through Lua API.
	 */
	public List<Class<?>> getExposedClasses();

	/*
	 * Returns a list of object declaring global Lua methods to expose through Lua API.
	 * The global Lua methods have to be declared with the annotation @LuaMethod.
	 */
	public List<Object> getGlobalObjects();

	/*
	 * Called at startup. This is where you can initialize your Java mod if needed.
	 */
	default void startup() {
	}
}
