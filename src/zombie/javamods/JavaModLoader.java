package zombie.javamods;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.jar.JarFile;

import zombie.debug.DebugLog;


public class JavaModLoader {

	private final static String javaModManifest = "javamods.txt";

	private static HashSet<String> loadedMods = new HashSet<>();

	private static List<JavaMod> loadJavaMod(JarFile jarFile) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, IOException, NoSuchMethodException {
		String line;
		List<JavaMod> javaMods = new ArrayList<>();
		List<String> classList = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(jarFile.getEntry(javaModManifest))));

		DebugLog.Lua.warn("JavaMods: Checking JAR file " + jarFile.getName());
		while ((line = reader.readLine()) != null) {
			line = line.strip();
			if (!line.equals(""))
				classList.addAll(Arrays.asList(line.split("[,;:]")));
		}

		reader.close();

		if (classList.isEmpty()) {
			DebugLog.Lua.warn("JavaMods: No class found in " + javaModManifest);
			return javaMods;
		}

		for (String className : classList) {

			Class<? extends JavaMod> classs = Class.forName(className).asSubclass(JavaMod.class);

			if (!JavaMod.class.isAssignableFrom(classs)) {
				DebugLog.Lua.warn("JavaMods: Class " + className + " is not a JavaMod, skipping.");
				continue;
			}

			if (loadedMods.contains(className)) {
				DebugLog.Lua.warn("JavaMods: Found duplicate class " + className + ", skipping.");
				continue;
			}

			DebugLog.Lua.warn("JavaMods: Loading JavaMod " + jarFile.getName());
			javaMods.add(classs.getDeclaredConstructor().newInstance());
			loadedMods.add(className);
		}

		return javaMods;
	}

	public static List<JavaMod> loadJavaMods(File directory) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, IOException, NoSuchMethodException {

		ArrayList<JavaMod> javaMods = new ArrayList<>();

		DebugLog.Lua.warn("JavaMod: Loading JavaMods from " + directory.getAbsolutePath());
		for (String fileName : directory.list())
			if (fileName.toLowerCase().endsWith(".jar"))
				javaMods.addAll(loadJavaMod(new JarFile(new File(directory, fileName))));

		//JavaMod mod = javaMods.get(0);
		return javaMods;
	}
}
