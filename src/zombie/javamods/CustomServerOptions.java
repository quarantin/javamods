package zombie.javamods;

import zombie.network.ServerOptions;

public class CustomServerOptions {

	public static void setMaxPlayers(int maxPlayers) {
		ServerOptions.instance.MaxPlayers = new ServerOptions.IntegerServerOption(ServerOptions.instance, "MaxPlayers", 1, maxPlayers, 16);
	}
}
