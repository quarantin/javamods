package javamods;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;

import java.security.ProtectionDomain;

import java.util.HashMap;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import javamods.Patch;


public class Patcher implements ClassFileTransformer {

	HashMap<String, Patch> patches;

	public Patcher(List<Patch> patches) {
		this.patches = new HashMap<>();
		for (Patch patch : patches) {
			String className = patch.getTargetClass().replace(".", "/");
			this.patches.put(className, patch);
		}
	}

	public byte[] transform(ClassLoader loader, String className, Class<?> targetClass, ProtectionDomain proectionDomain, byte[] classBytes) {

		byte[] bytecode = classBytes;

		Patch patch = patches.get(className);
		if (patch != null) {

			Log.info("Patching class " + className);

			try {
				ClassPool classPool = ClassPool.getDefault();

				for (String pkg : patch.getImports())
					classPool.importPackage(pkg);

				CtClass cc = classPool.get(patch.getTargetClass());
				CtMethod method = cc.getDeclaredMethod(patch.getTargetMethod());

				if (patch.insertAfter())
					method.insertAfter(patch.getMethodBody());
				else
					method.insertBefore(patch.getMethodBody());

				bytecode = cc.toBytecode();
				cc.detach();
			}
			catch (Exception error) {
				Log.error(error);
			}
		}

		return bytecode;
	}
}
