package zombie.javamods;

import java.util.HashSet;
import java.util.List;

import se.krka.kahlua.converter.KahluaConverterManager;
import se.krka.kahlua.integration.expose.LuaJavaClassExposer;
import se.krka.kahlua.j2se.J2SEPlatform;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.Platform;

import zombie.debug.DebugLog;


public class JavaModExposer extends LuaJavaClassExposer {

	//private static J2SEPlatform platform;
	//private static KahluaConverterManager manager;
	//private static KahluaTable env;

	private static JavaModExposer instance;

	public static JavaModExposer getInstance(KahluaConverterManager manager, Platform platform, KahluaTable env) {

		if (instance == null) {

			DebugLog.Lua.println("JavaMod: Initializing JavaModExposer...");

			//manager = new KahluaConverterManager();
			//platform = new J2SEPlatform();
			//env = platform.newEnvironment();

			instance = new JavaModExposer(manager, platform, env);
		}

		return instance;
	}

	private KahluaTable env;
	private HashSet<Class<?>> exposed;

	public JavaModExposer(KahluaConverterManager manager, Platform platform, KahluaTable env) {
		super(manager, platform, env);
		this.env = env;
		this.exposed = new HashSet<>();
	}

	public void exposeJavaMods(List<JavaMod> javaMods) {

		for (JavaMod javaMod : javaMods) {

			DebugLog.Lua.println("JavaMods: Exposing JavaMod: " + javaMod);

			List<Class<?>> exposedClasses = javaMod.getExposedClasses();
			if (exposedClasses != null) {
				for (Class<?> classs : exposedClasses) {
					DebugLog.Lua.println("JavaMods: Exposing class " + classs.getName());
					this.exposed.add(classs);
					exposeLikeJavaRecursively(classs, this.env);
				}
			}

			List<Object> globalObjects = javaMod.getGlobalObjects();
			if (globalObjects != null) {
				for (Object globalObject : globalObjects) {
					DebugLog.Lua.println("JavaMods: Exposing global object " + globalObject);
					//exposeLikeJavaRecursively(globalObject.getClass());
					exposeGlobalFunctions(globalObject);
				}
			}
		}

		DebugLog.Lua.println("JavaMod: OK");
	}

	public boolean shouldExpose(Class<?> javaClass) {
		return javaClass == null ? false : this.exposed.contains(javaClass);
	}
}
