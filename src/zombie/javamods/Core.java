package zombie.javamods;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import se.krka.kahlua.converter.KahluaConverterManager;
import se.krka.kahlua.j2se.J2SEPlatform;
import se.krka.kahlua.vm.KahluaTable;

import zombie.Lua.LuaManager;
import zombie.gameStates.MainScreenState;


public class Core {

	public static boolean debug;
	private static KahluaTable env;

	public static Class<?> getZomboidMainClass() {
		return MainScreenState.class;
	}

	private static boolean isLuaManagerReady() {

		try {
			Field envField = LuaManager.class.getDeclaredField("env");
			KahluaTable env = (KahluaTable)envField.get(null);
			return env != null && env.rawget("Calendar") != null;
		}
		catch (Exception error) {
			throw new RuntimeException(error);
		}
	}

	protected static void waitLuaManagerReady() {

		while (!isLuaManagerReady()) {

			try {
				Thread.sleep(1000);
			}
			catch (Exception interrupted) {
			}
		}
	}

	protected static void init() {

		try {
			Field envField = LuaManager.class.getDeclaredField("env");
			env = (KahluaTable)envField.get(null);

			new Exposer(new KahluaConverterManager(), new J2SEPlatform(), env).exposeJavaMods();
		}
		catch (Exception error) {
			Log.error(error);
		}
	}

	protected static void loop() {

		do {

			try {
				Field envField = LuaManager.class.getDeclaredField("env");
				KahluaTable env = (KahluaTable)envField.get(null);
				Log.debug("LuaManager.env: " + env + " == " + Core.env);

				if (env != Core.env)
					init();
			}
			catch (Exception error) {
				Log.error(error);
			}

			try {
				Thread.sleep(1000);
			}
			catch (Exception interrupted) {}
		}
		while (true);
	}
}
