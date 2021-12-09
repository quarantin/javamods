package zombie.javamods;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import se.krka.kahlua.converter.KahluaConverterManager;
import se.krka.kahlua.j2se.J2SEPlatform;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.Platform;

import zombie.gameStates.MainScreenState;
import zombie.Lua.LuaManager;


public class Main implements Runnable {

	private Main() {
		new Thread(this).start();
	}

	private boolean isLuaManagerReady() {

		try {
			Field envField = LuaManager.class.getDeclaredField("env");
			KahluaTable env = (KahluaTable)envField.get(null);
			return env != null && env.rawget("Calendar") != null;
		}
		catch (Exception error) {
			throw new RuntimeException(error);
		}
	}

	private void waitLuaManagerReady() {

		while (!isLuaManagerReady()) {

			try {
				Thread.sleep(1000);
			}
			catch (Exception interrupted) {
			}
		}
	}

	private void exposeJavaMods(File javaModsDir) {

		waitLuaManagerReady();

		try {
			Field managerField = LuaManager.class.getDeclaredField("converterManager");
			KahluaConverterManager manager = (KahluaConverterManager)managerField.get(null);

			Field platformField = LuaManager.class.getDeclaredField("platform");
			Platform platform = (Platform)platformField.get(null);

			Field envField = LuaManager.class.getDeclaredField("env");
			KahluaTable env = (KahluaTable)envField.get(null);

			JavaModExposer exposer = new JavaModExposer(manager, platform, env);
			exposer.exposeJavaMods(JavaModLoader.loadJavaMods(javaModsDir));
		}
		catch (Exception error) {
			throw new RuntimeException(error);
		}
	}

	public void run() {

		String installPath = System.getProperty("user.dir");
		File javaModsDir = new File(installPath, "javamods");
		if (!javaModsDir.exists())
			javaModsDir.mkdirs();

		exposeJavaMods(javaModsDir);
	}

	private static void startZomboid(String[] args) {
		try {
			final Object[] arg = new Object[]{ args };
			MainScreenState.class.getDeclaredMethod("main", String[].class).invoke(null, arg);
		}
		catch (IllegalAccessException|InvocationTargetException|NoSuchMethodException error) {
			error.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Main();
		startZomboid(args);
	}
}
