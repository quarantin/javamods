package javamods.mods;

import java.util.Arrays;
import java.util.List;

import javamods.JavaMod;
import javamods.Patch;


public class MaxPlayersMod extends JavaMod {

	@Override
	public List<Patch> getPatches() {

		Patch constructorPatch = new Patch(
			"zombie.network.ServerOptions",
			"ServerOptions",
			"System.out.println(\"inside ServerOptions.ServerOptions!\"); this.MaxPlayers = new ServerOptions.IntegerServerOption(this, \"MaxPlayers\", 1, 64, 16);");

		Patch methodPatch = new Patch(
			"zombie.network.ServerOptions",
			"getMaxPlayers",
			"System.out.println(\"inside ServerOptions.getMaxPlayers!\"); return Math.min(64, getInstance().MaxPlayers.getValue());");

		return Arrays.asList(constructorPatch, methodPatch);
	}
}
