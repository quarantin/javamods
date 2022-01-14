package javamods.mods;

import java.util.Arrays;
import java.util.List;

import javamods.JavaMod;
import javamods.Patch;


public class MaxPlayersMod extends JavaMod {

	@Override
	public List<Patch> getPatches() {

		/*
		Patch initOptionsPatch = new Patch(
			"zombie.network.ServerOptions",
			"init",
			"int index = this.options.indexOf(this.MaxPlayers);" +
			"this.options.remove(this.MaxPlayers);" +
			"this.optionByName.remove(this.MaxPlayers.asConfigOption().getName());" +
			"this.MaxPlayers = new zombie.network.ServerOptions.IntegerServerOption(this, \"MaxPlayers\", 1, 64, 16);" +
			"Object option = this.options.remove(this.options.size() - 1);" +
			"this.options.add(index, option);"
		);
		*/
		Patch getMaxPlayersPatch = new Patch(
			"zombie.network.ServerOptions",
			"getMaxPlayers",
			//"return Math.min(64, getInstance().MaxPlayers.getValue());");
			"return 64;"
		);

		//return Arrays.asList(initOptionsPatch, getMaxPlayersPatch);
		return Arrays.asList(getMaxPlayersPatch);
	}
}
