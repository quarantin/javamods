package zombie.commands.serverCommands;

import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;

import zombie.core.raknet.UdpConnection;

import zombie.network.GameServer;
import zombie.ui.UIManager;


@CommandName(name = "togglepause")
@CommandHelp(helpText = "UI_ServerOptionDesc_TogglePause")
@RequiredRight(requiredRights = 32)
public class TogglePauseCommand extends CommandBase {

	private static boolean paused = false;

	public TogglePauseCommand(String string, String string2, String string3, UdpConnection udpConnection) {
		super(string, string2, string3, udpConnection);
	}

	protected String Command() {

		String status;

		if (paused) {
			GameServer.UnPauseAllClients();
			UIManager.getSpeedControls().SetCurrentGameSpeed(1);
			paused = false;
			status = "World Unpaused";
		}
		else {
			GameServer.PauseAllClients();
			UIManager.getSpeedControls().SetCurrentGameSpeed(0);
			paused = true;
			status = "World Paused";
		}

		return status;
	}
}
