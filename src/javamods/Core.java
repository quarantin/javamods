package javamods;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import se.krka.kahlua.converter.KahluaConverterManager;
import se.krka.kahlua.j2se.J2SEPlatform;
import se.krka.kahlua.vm.KahluaTable;

import zombie.Lua.LuaManager;
import zombie.gameStates.MainScreenState;
import zombie.network.GameServer;


public class Core {

	public static boolean debug = false;
	public static boolean server = true;

	public static void init() {

		try {
			Log.info("javamods.Core.init()");

			Field envField = LuaManager.class.getDeclaredField("env");
			KahluaTable env = (KahluaTable)envField.get(null);;

			new Exposer(new KahluaConverterManager(), new J2SEPlatform(), env).exposeJavaMods(Loader.loadJavaMods());
		}
		catch (Exception error) {
			Log.error(error);
		}
	}
}
