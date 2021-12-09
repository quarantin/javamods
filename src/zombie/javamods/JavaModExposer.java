package zombie.javamods;

import java.util.List;

import se.krka.kahlua.converter.KahluaConverterManager;
import se.krka.kahlua.integration.expose.LuaJavaClassExposer;
import se.krka.kahlua.j2se.J2SEPlatform;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.Platform;

import zombie.debug.DebugLog;


public class JavaModExposer extends LuaJavaClassExposer {

	private static J2SEPlatform platform;
	private static KahluaConverterManager manager;
	private static KahluaTable env;

	private static JavaModExposer instance;

	public static JavaModExposer getInstance() {

		if (instance == null) {

			DebugLog.Lua.println("JavaMod: Initializing JavaModExposer...");

			manager = new KahluaConverterManager();
			platform = new J2SEPlatform();
			env = platform.newEnvironment();

			instance = new JavaModExposer(manager, platform, env, env);

			DebugLog.Lua.println("JavaMod: OK");
		}

		return instance;
	}

	public JavaModExposer(KahluaConverterManager manager, Platform platform, KahluaTable env, KahluaTable autoExposeBase) {
		super(manager, platform, env, autoExposeBase);
	}

	public void exposeJavaMods(List<JavaMod> javaMods) {

		for (JavaMod javaMod : javaMods) {

			List<Class<?>> exposedClasses = javaMod.getExposedClasses();
			if (exposedClasses != null)
				for (Class<?> classs : exposedClasses)
					exposeLikeJavaRecursively(classs);

			List<Object> globalObjects = javaMod.getGlobalObjects();
			if (globalObjects != null)
				for (Object globalObject : globalObjects)
					exposeGlobalFunctions(globalObject);
		}
	}
}
