package zombie.javamods.mod;

import java.util.List;


public abstract class ExposeClassJavaMod extends JavaMod {

	public List<Object> getObjectsWithGlobalFunctions() {
		return null;
	}

	public List<Class<?>> getServerCommands() {
		return null;
	}

	public void startup() {
	}
}
