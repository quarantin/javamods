package zombie.commands.serverCommands;

import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;

import zombie.core.raknet.UdpConnection;

import zombie.network.GameServer;
import zombie.ui.UIManager;


@CommandName(name = "pause")
@CommandHelp(helpText = "UI_ServerOptionDesc_Pause")
@RequiredRight(requiredRights = 32)
public class PauseCommand extends CommandBase {

	public PauseCommand(String string, String string2, String string3, UdpConnection udpConnection) {
		super(string, string2, string3, udpConnection);
	}

	protected String Command() {
		GameServer.PauseAllClients();
		UIManager.getSpeedControls().SetCurrentGameSpeed(0);
		return "World Paused";
	}
}
