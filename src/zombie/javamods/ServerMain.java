package zombie.javamods;

import java.io.IOException;


public class ServerMain implements Runnable {

	private final static String debugArg     = "-debug";
	private final static String bootstrapArg = "-bootstrap";
	private final static String maxPlayersArg = "-maxplayers";

	private ServerMain(String[] args) throws IOException {

		Core.server = true;

		for (String arg : args) {

			if (arg.equals(debugArg))
				Core.debug =  true;
		}

		String maxPlayers = System.getenv("MAXPLAYERS");
		if (maxPlayers != null)
			CustomServerOptions.setMaxPlayers(Integer.parseInt(maxPlayers));

		Log.init();
		Log.info("Starting JavaMods Server");
		Log.info("Running with debug mode = " + Core.debug);

		new Thread(this).start();
	}

	public void run() {
		Core.waitLuaManagerReady();
		Core.init();
		Core.loop();
	}

	private static void startZomboidServer(String[] args) {

		try {
			final Object[] argv = new Object[]{ args };
			Class<?> zomboidServerMainClass = Core.getZomboidServerMainClass();
			zomboidServerMainClass.getDeclaredMethod("main", String[].class).invoke(null, argv);
		}
		catch (Exception error) {
			Log.error(error);
		}
	}

	public static void main(String[] args) throws IOException {
		new ServerMain(args);
		startZomboidServer(args);
		Log.exit();
	}
}
