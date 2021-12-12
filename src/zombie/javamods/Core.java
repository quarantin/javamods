package zombie.javamods;


public class Core {

	public static boolean debug;

	public static Class<?> getZomboidLuaManagerClass() {
		return zombie.Lua.LuaManager.class;
	}

	public static Class<?> getZomboidMainClass() {
		return zombie.gameStates.MainScreenState.class;
	}
}
