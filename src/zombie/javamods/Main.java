package zombie.javamods;

import java.io.IOException;


public class Main implements Runnable {

	private final static String debugArg     = "-debug";
	private final static String serverArg    = "-server";
	private final static String bootstrapArg = "-bootstrap";
	private final static String maxPlayersArg = "-maxplayers";

	private Main(String[] args) throws IOException {

		int maxPlayers = 0;
		String previousArg = null;

		for (String arg : args) {

			if (arg.equals(bootstrapArg)) {
				Loader.bootstrapJavaMods();
				System.out.println(Loader.getClassPath());
				System.exit(0);
			}

			else if (arg.equals(debugArg))
				Core.debug =  true;

			else if (arg.equals(serverArg))
				Core.server = true;

			else if (maxPlayersArg.equals(previousArg))
				maxPlayers = Integer.parseInt(arg);

			previousArg = arg;
		}

		if (maxPlayers > 0)
			CustomServerOptions.setMaxPlayers(maxPlayers);

		Log.init();
		Log.info("Starting JavaMods");
		Log.info("Running with debug mode = " + Core.debug);

		new Thread(this).start();
	}

	public void run() {
		Core.waitLuaManagerReady();
		Core.init();
		Core.loop();
	}

	private static void startZomboid(String[] args) {

		try {
			final Object[] argv = new Object[]{ args };
			Class<?> zomboidMainClass = Core.getZomboidClientMainClass();
			zomboidMainClass.getDeclaredMethod("main", String[].class).invoke(null, argv);
		}
		catch (Exception error) {
			Log.error(error);
		}
	}

	private static void startZomboidServer(String[] args) {

		try {
			final Object[] argv = new Object[]{ args };
			Class<?> zomboidMainClass = Core.getZomboidServerMainClass();
			zomboidMainClass.getDeclaredMethod("main", String[].class).invoke(null, argv);
		}
		catch (Exception error) {
			Log.error(error);
		}
	}

	public static void main(String[] args) throws IOException {

		new Main(args);

		if (Core.server)
			startZomboidServer(args);
		else
			startZomboid(args);

		Log.exit();
	}
}
