package zombie.javamods;

import java.io.IOException;


public class Bootstrap {

	public static void main(String[] args) throws IOException {
		Loader.bootstrapJavaMods();
		System.out.println(Loader.getClassPath());
		System.exit(0);
	}
}
