package javamods;

import java.net.URISyntaxException;

import java.nio.file.Paths;

import javamods.Log;


public abstract class JavaMod implements JavaModInterface {

	public String getJarPath() {

		try {
			return Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).toFile().getAbsolutePath();
		}
		catch (URISyntaxException error) {
			Log.error(error);
		}

		return "Not found";
	}
}
