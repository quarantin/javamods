package zombie.javamods;


public class Core {

	public static boolean debug = true;

	protected static void initDebug() {
		debug = zombie.core.Core.bDebug;
	}

	public static Class<?> getZomboidLuaManagerClass() {
		return zombie.Lua.LuaManager.class;
	}

	public static Class<?> getZomboidMainClass() {
		return zombie.gameStates.MainScreenState.class;
	}
}
