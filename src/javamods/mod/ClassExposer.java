package javamods.mod;

import java.util.List;


public abstract class ClassExposer extends JavaMod {

	@Override
	public List<Object> getObjectsWithGlobalFunctions() {
		return null;
	}

	@Override
	public void startup() {
	}
}
