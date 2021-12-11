package zombie.javamods;

import java.util.ArrayList;
import java.util.List;

import se.krka.kahlua.converter.KahluaConverterManager;
import se.krka.kahlua.integration.expose.LuaJavaClassExposer;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.Platform;


public class JavaModExposer extends LuaJavaClassExposer {

	KahluaTable env;
	List<Class<?>> exposed;

	public JavaModExposer(KahluaConverterManager manager, Platform platform, KahluaTable env) {
		super(manager, platform, env);
		this.env = env;
		this.exposed = new ArrayList<>();
	}

	public void exposeJavaMods(List<JavaMod> javaMods) {

		for (JavaMod javaMod : javaMods) {

			Log.info("JavaMods: Exposing JavaMod: " + javaMod);

			List<Class<?>> exposedClasses = javaMod.getExposedClasses();
			if (exposedClasses != null)
				for (Class<?> classs : exposedClasses) {
					Log.info("JavaMods: Exposing class " + classs);
					exposed.add(classs);
					exposeLikeJavaRecursively(classs, env);
				}

			List<Object> globalObjects = javaMod.getGlobalObjects();
			if (globalObjects != null)
				for (Object globalObject : globalObjects)
					exposeGlobalFunctions(globalObject);

			javaMod.startup();
		}
	}

	public boolean shouldExpose(Class<?> classs) {
		return classs != null && exposed.contains(classs);
	}
}
