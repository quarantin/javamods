package javamods;

import java.lang.instrument.Instrumentation;

import java.util.ArrayList;
import java.util.List;

import javamods.Patch;
import javamods.JavaMod;


public class Agent {

	public static void premain(String agentArgs, Instrumentation instrumentation) {

		try {
			Log.init();
			Log.info("Starting JavaMods");

			List<Patch> patches = new ArrayList<>();
			List<JavaMod> javaMods = Loader.loadJavaMods();
			for (JavaMod javaMod : javaMods) {

				List<Patch> modPatches = javaMod.getPatches();
				if (modPatches != null)
					patches.addAll(modPatches);
			}

			instrumentation.addTransformer(new Patcher(patches));
		}
		catch (Exception error) {
			error.printStackTrace();
		}
	}
}
