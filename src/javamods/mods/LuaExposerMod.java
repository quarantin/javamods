package javamods.mods;

import java.util.Arrays;
import java.util.List;

import javamods.JavaMod;
import javamods.Patch;


public class LuaExposerMod extends JavaMod {

	@Override
	public List<Patch> getPatches() {

		Patch initPatch = new Patch(
			"zombie.Lua.LuaManager",
			"init",
			"javamods.Core.init();",
			true);

		return Arrays.asList(initPatch);
	}
}
