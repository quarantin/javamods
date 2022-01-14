package javamods.mods;

import java.util.Arrays;
import java.util.List;

import javamods.JavaMod;
import javamods.Log;
import javamods.Patch;

import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.network.ServerOptions;


public class AntiCheatMod extends JavaMod {

	public static boolean isStaff(UdpConnection connection) {
		String accessLevel = connection.accessLevel;
		return accessLevel != null && !accessLevel.isEmpty();
	}

	public static void kickPlayer(UdpConnection connection, String reason) {
		String logMessage = connection.username + " was kicked from server. Reason: " + reason;
		String playerMessage = "You have been kicked from this server. Reason: " + reason;
		Log.info(logMessage);
		ByteBufferWriter buffer = connection.startPacket();
		PacketTypes.PacketType.Kicked.doPacket(buffer);
		buffer.putUTF(playerMessage);
		PacketTypes.PacketType.Kicked.send(connection);
		connection.forceDisconnect();
		GameServer.addDisconnect(connection);
		if (ServerOptions.instance.BanKickGlobalSound.getValue())
			GameServer.PlaySoundAtEveryPlayer("RumbleThunder");
	}

	@Override
	public List<Patch> getPatches() {

		Patch teleportPatch = new Patch(
			"zombie.network.GameServer",
			"receiveTeleport",
			"if (!javamods.mods.AntiCheatMod.isStaff($2)) {" +
			"	javamods.mods.AntiCheatMod.kickPlayer($2, \"Teleport hack detected!\");" +
			"	return;" +
			"}"
		);

		Patch adminPatch = new Patch(
			"zombie.network.GameServer",
			"receiveChangePlayerStats",
			"if (!javamods.mods.AntiCheatMod.isStaff($2)) {" +
			"	javamods.mods.AntiCheatMod.kickPlayer($2, \"Admin hack detected!\");" +
			"	return;" +
			"}"
		);

		Patch removeItemPatch = new Patch(
			"zombie.network.GameServer",
			"receiveInvMngRemoveItem",
			"if (!javamods.mods.AntiCheatMod.isStaff($2)) {" +
			"	javamods.mods.AntiCheatMod.kickPlayer($2, \"RemoveItem hack detected!\");" +
			"	return;" +
			"}"
		);

		Patch addItemPatch = new Patch(
			"zombie.network.GameServer",
			"receiveInvMngGetItem",
			"if (!javamods.mods.AntiCheatMod.isStaff($2)) {" +
			"	javamods.mods.AntiCheatMod.kickPlayer($2, \"GetItem hack detected!\");" +
			"	return;" +
			"}"
		);

		Patch reqItemPatch = new Patch(
			"zombie.network.GameServer",
			"receiveInvMngReqItem",
			"if (!javamods.mods.AntiCheatMod.isStaff($2)) {" +
			"	javamods.mods.AntiCheatMod.kickPlayer($2, \"ReqItem hack detected!\");" +
			"	return;" +
			"}"
		);

		Patch requestInventoryPatch = new Patch(
			"zombie.network.GameServer",
			"receiveRequestInventory",
			"if (!javamods.mods.AntiCheatMod.isStaff($2)) {" +
			"	javamods.mods.AntiCheatMod.kickPlayer($2, \"RequestInventory hack detected!\");" +
			"	return;" +
			"}"
		);

		Patch sandboxOptionsPatch = new Patch(
			"zombie.network.GameServer",
			"receiveSandboxOptions",
			"if (!javamods.mods.AntiCheatMod.isStaff($2)) {" +
			"	javamods.mods.AntiCheatMod.kickPlayer($2, \"SandboxOptions hack detected!\");" +
			"	return;" +
			"}"
		);

		Patch executeQueryPatch = new Patch(
			"zombie.network.GameServer",
			"receiveExecuteQuery",
			"return;"
		);

		Patch hitPlayerPatch = new Patch(
			"zombie.network.GameServer",
			"receiveHitCharacter",
			"if (!zombie.network.ServerOptions.instance.PVP.getValue()) {" +
			"	byte packetType = $1.get();" +
			"	$1.rewind();" +
			"	if (packetType == zombie.network.packets.hit.HitCharacterPacket.HitType.PlayerHitPlayer.ordinal()) {" +
			"		javamods.mods.AntiCheatMod.kickPlayer($2, \"PvP hack detected!\");" +
			"		return;" +
			"	}" +
			"}"
		);

		Patch killPlayerPatch = new Patch(
			"zombie.network.GameServer",
			"receivePlayerDeath",
			"if (!javamods.mods.AntiCheatMod.isStaff($2)) {" +
			"	Short playerID = new Short($1.getShort());" +
			"	$1.rewind();" +
			"	zombie.characters.IsoPlayer player = zombie.network.GameServer.IDToPlayerMap.get(playerID);" +
			"	if (player != null && !$2.username.equals(player.username)) {" +
			"		javamods.mods.AntiCheatMod.kickPlayer($2, \"Kill hack detected!\");" +
			"		return;" +
			"	}" +
			"}"
		);

		Patch damagePlayerPatch = new Patch(
			"zombie.network.GameServer",
			"receivePlayerDamage",
			"if (!javamods.mods.AntiCheatMod.isStaff($2)) {" +
			"	Short playerID = new Short($1.getShort());" +
			"	$1.rewind();" +
			"	zombie.characters.IsoPlayer player = zombie.network.GameServer.IDToPlayerMap.get(playerID);" +
			"	if (player != null && !$2.username.equals(player.username)) {" +
			"		javamods.mods.AntiCheatMod.kickPlayer($2, \"Damage hack detected!\");" +
			"		return;" +
			"	}" +
			"}"
		);

		return Arrays.asList(
				teleportPatch,
				adminPatch,
				removeItemPatch,
				addItemPatch,
				reqItemPatch,
				requestInventoryPatch,
				//executeQueryPatch,
				hitPlayerPatch,
				killPlayerPatch,
				damagePlayerPatch
		);
	}
}
