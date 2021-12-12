package zombie.javamods;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.jar.JarFile;

import zombie.javamods.mod.JavaMod;


public class Loader {

	private final static String javaModManifest = "javamods.txt";

	private static List<JavaMod> javaMods;
	private static HashSet<String> jarFiles = new HashSet<>();
	private static HashSet<String> loadedMods = new HashSet<>();

	protected static String getClassPath() {
		return String.join(System.getProperty("path.separator"), jarFiles);
	}

	private static List<String> getJavaModClassNames(JarFile jarFile) {

		String line;
		BufferedReader reader;
		List<String> classNames = new ArrayList<>();

		try {
			reader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(jarFile.getEntry(javaModManifest))));

			while ((line = reader.readLine()) != null) {
				line = line.strip();
				if (!line.equals(""))
					classNames.add(line);
			}

			reader.close();
		}
		catch (IOException error) {
			Log.error("Error while reading " + javaModManifest + " from " + jarFile.getName());
			Log.error(error);
			return null;
		}

		return classNames;
	}

	private static JavaMod instantiateJavaMod(String jarPath, String className) {

		Class<? extends JavaMod> classs;

		try {
			classs = Class.forName(className).asSubclass(JavaMod.class);
		}
		catch (ClassNotFoundException error) {
			Log.error("Class definition not found for class " + className + " in " + jarPath);
			Log.error(error);
			return null;
		}
		catch (NoClassDefFoundError error) {
			Log.error("Class definition not found for class imported from " + className + ", skipping.");
			Log.error(error);
			return null;
		}

		if (!JavaMod.class.isAssignableFrom(classs)) {
			Log.warn("Class " + className + " is not a JavaMod, skipping.");
			return null;
		}

		if (loadedMods.contains(className)) {
			Log.warn("Found duplicate class " + className + ", skipping.");
			return null;
		}

		try {
			JavaMod javaMod = classs.getDeclaredConstructor().newInstance();
			javaMod.setJarPath(jarPath);
			return javaMod;
		}
		catch (InstantiationException error) {
			Log.error("Error while instantiating object from class " + className + " in " + jarPath);
			Log.error(error);
		}
		catch (IllegalAccessException error) {
			Log.error("Illegal access while instantiating object from class " + className + " in " + jarPath);
			Log.error(error);
		}
		catch (InvocationTargetException error) {
			Log.error("Exception in no-argument constructor from class " + className + " in " + jarPath);
			Log.error(error);
		}
		catch (NoSuchMethodException error) {
			Log.error("Missing no-argument constructor from class " + className + " in " + jarPath);
			Log.error(error);
		}

		return null;
	}

	private static List<JavaMod> loadJavaMod(JarFile jarFile, boolean bootstrap) {

		String jarPath = jarFile.getName();
		List<JavaMod> javaMods = new ArrayList<>();

		List<String> classNames = getJavaModClassNames(jarFile);
		if (classNames == null)
			return null;

		if (classNames.isEmpty()) {
			Log.warn("No JavaMod class found in " + jarPath);
			return null;
		}

		for (String className : classNames) {

			jarFiles.add(jarPath);

			if (bootstrap)
				break;

			JavaMod javaMod = instantiateJavaMod(jarPath, className);
			if (javaMod == null)
				return null;

			javaMods.add(javaMod);
			loadedMods.add(className);
		}

		return javaMods;
	}

	private static List<JavaMod> loadJavaMods(boolean bootstrap) {

		if (Loader.javaMods != null)
			return Loader.javaMods;

		List<JavaMod> newJavaMods;
		List<JavaMod> javaMods = new ArrayList<>();

		for (File modFolder : Filesystem.getMods())
			for (String fileName : modFolder.list())
				if (fileName.toLowerCase().endsWith(".jar")) {

					File file = new File(modFolder, fileName);

					try {
						newJavaMods = loadJavaMod(new JarFile(file), bootstrap);
						if (newJavaMods != null)
							javaMods.addAll(newJavaMods);
					}
					catch (IOException error) {
						Log.warn("I/O error while opening JAR File " + file.getAbsolutePath() + ", skipping.");
						continue;
					}
				}

		Loader.javaMods = javaMods;
		return javaMods;
	}

	public static List<JavaMod> loadJavaMods() {
		return loadJavaMods(false);
	}

	public static List<JavaMod> bootstrapJavaMods() {
		return loadJavaMods(true);
	}
}
