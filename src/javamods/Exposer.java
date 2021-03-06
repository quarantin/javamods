package javamods;

import java.util.HashSet;
import java.util.List;

import se.krka.kahlua.converter.KahluaConverterManager;
import se.krka.kahlua.integration.expose.LuaJavaClassExposer;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.Platform;

import javamods.JavaMod;


public class Exposer extends LuaJavaClassExposer {

	private KahluaTable env;
	private HashSet<Class<?>> exposed;

	public Exposer(KahluaConverterManager manager, Platform platform, KahluaTable env) {
		super(manager, platform, env);
		this.env = env;
		this.exposed = new HashSet<>();
	}

	public void exposeJavaMods(List<JavaMod> javaMods) {

		Log.info("Exposing java mods...");
		for (JavaMod javaMod : javaMods) {

			Log.info("Exposing java mod " + javaMod.getClass().getName() + " [" + javaMod.getJarPath() + "]");

			List<Class<?>> classesToExpose = javaMod.getClassesToExpose();
			if (classesToExpose != null)
				for (Class<?> classs : classesToExpose) {
					Log.info(" - exposing " + classs);
					exposed.add(classs);
					exposeLikeJavaRecursively(classs, env);
				}

			List<Object> objectsWithGlobalFunctions = javaMod.getObjectsWithGlobalFunctions();
			if (objectsWithGlobalFunctions != null)
				for (Object objectWithGlobalFunctions : objectsWithGlobalFunctions) {
					Log.info(" - adding global functions from " + objectWithGlobalFunctions.getClass().getName());
					exposeGlobalFunctions(objectWithGlobalFunctions);
				}

			javaMod.startup();
		}
	}

	public boolean shouldExpose(Class<?> classs) {
		return classs != null && exposed.contains(classs);
	}
}
