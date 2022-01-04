package javamods.mods;

import java.util.Arrays;
import java.util.List;

import se.krka.kahlua.integration.annotations.LuaMethod;

import javamods.mod.JavaModGlobalFunction;
import zombie.core.Core;


public class Debug extends JavaModGlobalFunction {

	@Override
	public List<Object> getObjectsWithGlobalFunctions() {
		return Arrays.asList(this);
	}

	@LuaMethod(name = "setDebug", global = true)
	public static void setDebug(boolean debug) {
		Core.bDebug = debug;
	}
}
