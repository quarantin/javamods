package zombie.javamods.mod;

import java.util.List;


public abstract class ServerCommandJavaMod extends JavaMod {

	public List<Class<?>> getClassesToExpose() {
		return null;
	}

	public List<Class<?>> getServerCommands() {
		return null;
	}

	public List<Object> getObjectsWithGlobalFunctions() {
		return null;
	}

	public void startup() {
	}
}
