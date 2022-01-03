package zombie.commands.serverCommands;

import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;

import zombie.core.raknet.UdpConnection;

import zombie.network.GameServer;
import zombie.ui.UIManager;


@CommandName(name = "unpause")
@CommandHelp(helpText = "UI_ServerOptionDesc_Unpause")
@RequiredRight(requiredRights = 32)
public class UnpauseCommand extends CommandBase {

	public UnpauseCommand(String string, String string2, String string3, UdpConnection udpConnection) {
		super(string, string2, string3, udpConnection);
	}

	protected String Command() {
		GameServer.UnPauseAllClients();
		UIManager.getSpeedControls().SetCurrentGameSpeed(1);
		return "World Unpaused";
	}
}
