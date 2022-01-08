package javamods;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;

import org.json.JSONObject;

import javamods.Log;


public class Config {

	private final static String CONFIG = "config.json";
	private final static String JAVAMODS = ".javamods";

	private static void createConfig() throws IOException {
		InputStream inputStream = Config.class.getResourceAsStream("/" + CONFIG);
		int configSize = inputStream.available();
		byte[] buffer = new byte[configSize];
		inputStream.read(buffer);
		inputStream.close();
		writeFile(getConfigFile(), new String(buffer));
	}

	private static String readFile(File file) throws IOException {
		int filesize = (int)file.length();
		char[] buffer = new char[filesize];
		new FileReader(file).read(buffer, 0, buffer.length);
		return new String(buffer);
	}

	private static void writeFile(File file, String content) throws IOException {
		FileWriter writer = new FileWriter(file);
		writer.write(content, 0, content.length());
		writer.close();
	}

	public static JSONObject parseConfig(File configFile) throws IOException {
		return new JSONObject(readFile(configFile));
	}

	public static File getConfigFile() {
		File homeDir = new File(System.getProperty("user.home"));
		File javamodsDir = new File(homeDir, JAVAMODS);
		javamodsDir.mkdirs();
		return new File(javamodsDir, CONFIG);
	}

	public static JSONObject getConfig() {

		File configFile = getConfigFile();

		if (!configFile.exists()) {
			Log.warn("File not found: " + configFile);
			return new JSONObject();
		}

		try {
			return parseConfig(configFile);
		}
		catch (IOException error) {
			Log.error(error);
			return new JSONObject();
		}
	}

	public static void main(String[] args) throws IOException {
		createConfig();
	}
}
