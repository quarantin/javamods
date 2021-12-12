package zombie.javamods;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import se.krka.kahlua.converter.KahluaConverterManager;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.Platform;


public class Main implements Runnable {

	private final static String debugArg     = "-debug";
	private final static String bootstrapArg = "-bootstrap";

	private static Class<?> zomboidMainClass;
	private static Class<?> zomboidLuaManagerClass;

	private Main(String[] args) throws IOException {

		for (String arg : args) {

			if (arg.equals(bootstrapArg)) {
				Loader.bootstrapJavaMods();
				System.out.println(Loader.getClassPath());
				System.exit(0);
			}

			else if (arg.equals(debugArg))
				Core.debug =  true;

		}

		Log.init();
		Log.info("Starting JavaMods");
		Log.info("Running with debug mode = " + Core.debug);

		zomboidMainClass = Core.getZomboidMainClass();
		zomboidLuaManagerClass = Core.getZomboidLuaManagerClass();

		new Thread(this).start();
	}

	private void init(String[] args) {
	}

	private boolean isLuaManagerReady() {

		try {
			Field envField = zomboidLuaManagerClass.getDeclaredField("env");
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

	public void run() {

		waitLuaManagerReady();

		try {
			Field managerField = zomboidLuaManagerClass.getDeclaredField("converterManager");
			KahluaConverterManager manager = (KahluaConverterManager)managerField.get(null);

			Field platformField = zomboidLuaManagerClass.getDeclaredField("platform");
			Platform platform = (Platform)platformField.get(null);

			Field envField = zomboidLuaManagerClass.getDeclaredField("env");
			KahluaTable env = (KahluaTable)envField.get(null);

			Exposer exposer = new Exposer(manager, platform, env);
			exposer.exposeJavaMods(Loader.loadJavaMods());
		}
		catch (Exception error) {
			throw new RuntimeException(error);
		}
	}

	private static void startZomboid() {
		try {
			final Object[] arg = new Object[]{ new String[] {} };
			zomboidMainClass.getDeclaredMethod("main", String[].class).invoke(null, arg);
		}
		catch (IllegalAccessException|InvocationTargetException|NoSuchMethodException error) {
			error.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		new Main(args);
		startZomboid();
	}
}
