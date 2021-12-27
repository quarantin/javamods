package zombie.javamods.mods;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

import java.util.Arrays;
import java.util.List;

import se.krka.kahlua.integration.annotations.LuaMethod;

import zombie.javamods.Log;
import zombie.javamods.Filesystem;
import zombie.javamods.mod.JavaMod;


public class CustomSaveFile extends JavaMod {

	@Override
	public List<Class<?>> getClassesToExpose() {
		return Arrays.asList(PrintWriter.class);
	}

	@Override
	public List<Object> getObjectsWithGlobalFunctions() {
		return Arrays.asList(this);
	}

	@Override
	public void startup() {}

	private static boolean hasSlash(String filename) {
		return filename.indexOf('/') > -1 || filename.indexOf('\\') > -1;
	}

	private static File getCurrentSavePath() {

		BufferedReader reader;
		File latestSave = new File(Filesystem.getUserProfileDir(), "latestSave.ini");

		try {
			reader = new BufferedReader(new FileReader(latestSave));
			String dir2 = reader.readLine();
			String dir1 = reader.readLine();

			reader.close();

			if (hasSlash(dir1) || hasSlash(dir2)) {
				Log.warn("No slash allowed");
				return null;
			}

			String currentSavePath = String.join(File.separator, dir1, dir2);
			return new File(Filesystem.getUserProfileSaveDir(), currentSavePath);
		}
		catch (FileNotFoundException error) {
			Log.error("File not found: " + latestSave);
		}
		catch (IOException error) {
			Log.error("I/O error: " + latestSave);
		}

		return null;
	}

	@LuaMethod(name = "getSaveFileWriter", global = true)
	public PrintWriter getSaveFileWriter(String filename, boolean append) {

		if (hasSlash(filename)) {
			Log.warn("No slash allowed");
			return null;
		}

		File currentSavePath = getCurrentSavePath();
		if (currentSavePath == null)
			return null;

		File file = new File(currentSavePath, filename);
		try {
			return new PrintWriter(new FileWriter(file, append));
		}
		catch (IOException error) {
			Log.error("I/O error: " + file);
			return null;
		}
	}
}
