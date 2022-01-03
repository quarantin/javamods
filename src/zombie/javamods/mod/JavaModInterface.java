package zombie.javamods.mod;

import java.util.List;


public interface JavaModInterface {

	/*
	 * Returns a list of class to expose through Lua API.
	 */
	public List<Class<?>> getClassesToExpose();

	/*
	 * Returns a list of object declaring global functions to expose to Lua API.
	 * The global functions have to be declared with the @LuaMethod annotation.
	 */
	public List<Object> getObjectsWithGlobalFunctions();

	/*
	 * Returns a list of BaseCommand classes.
	 */
	public List<Class<?>> getServerCommands();

	/*
	 * Called at startup. This is where you can initialize your Java mod if needed.
	 */
	public void startup();
}
