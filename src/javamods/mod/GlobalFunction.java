package javamods.mod;

import java.util.List;


public abstract class GlobalFunction extends JavaMod {

	@Override
	public List<Class<?>> getClassesToExpose() {
		return null;
	}

	@Override
	public void startup() {
	}
}
