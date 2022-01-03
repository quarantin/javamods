package zombie.javamods;

import java.lang.reflect.Field;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import se.krka.kahlua.converter.KahluaConverterManager;
import se.krka.kahlua.integration.expose.LuaJavaClassExposer;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.Platform;

import zombie.commands.CommandBase;

import zombie.javamods.mod.JavaMod;


public class Exposer extends LuaJavaClassExposer {

	private KahluaTable env;
	private List<Class<?>> exposed;
	private List<Class<?>> serverCommands;

	public Exposer(KahluaConverterManager manager, Platform platform, KahluaTable env) {
		super(manager, platform, env);
		this.env = env;
		this.exposed = new ArrayList<>();
		this.serverCommands = new ArrayList<>();
	}

	public void exposeJavaMods() {

		Log.info("Exposing java mods...");
		for (JavaMod javaMod : Loader.loadJavaMods()) {

			Log.info("Loading java mod " + javaMod.getClass().getName() + " [" + javaMod.getJarPath() + "]");

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

			List<Class<?>> serverCommands = javaMod.getServerCommands();
			if (serverCommands != null) {
				this.serverCommands.addAll(serverCommands);
			}

			javaMod.startup();
		}

		addServerCommands(this.serverCommands);
	}

	public boolean shouldExpose(Class<?> classs) {
		return classs != null && exposed.contains(classs);
	}

	private void addServerCommands(List<Class<?>> serverCommands) {

		try {
			Field childrenClassesField = CommandBase.class.getDeclaredField("childrenClasses");
			childrenClassesField.setAccessible(true);
			Class<?>[] childrenClasses = (Class<?>[])childrenClassesField.get(null);
			serverCommands.addAll(Arrays.asList(childrenClasses));
			childrenClassesField.set(null, serverCommands.toArray());
		}
		catch (Exception error) {
			Log.error(error);
		}
	}

}
