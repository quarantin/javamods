package zombie.javamods.mod;


public abstract class JavaMod implements JavaModInterface {

	private String jarPath;

	public String getJarPath() {
		return this.jarPath;
	}

	public void setJarPath(String jarPath) {
		this.jarPath = jarPath;
	}
}
