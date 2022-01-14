package javamods;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;

import java.security.ProtectionDomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;

import javamods.Patch;


public class Patcher implements ClassFileTransformer {

	HashMap<String, List<Patch>> patches;

	public Patcher(List<Patch> patches) {

		List<Patch> patchList;

		this.patches = new HashMap<>();
		for (Patch patch : patches) {

			String className = patch.getTargetClass().replace(".", "/");

			if (!this.patches.containsKey(className))
				this.patches.put(className, new ArrayList<>());

			patchList = this.patches.get(className);
			patchList.add(patch);
		}
	}

	private void patchConstructor(CtClass ctClass, Patch patch) throws Exception {

		String methodBody = patch.getMethodBody();
		CtConstructor constructor = ctClass.getDeclaredConstructor(null);

		if (patch.insertAfter())
			constructor.insertAfter(methodBody);

		else
			constructor.insertBefore(methodBody);
	}

	private void patchMethod(CtClass ctClass, Patch patch) throws Exception {

		String methodBody = patch.getMethodBody();
		CtMethod method = ctClass.getDeclaredMethod(patch.getTargetMethod());

		if (patch.insertAfter())
			method.insertAfter(methodBody);

		else
			method.insertBefore(methodBody);
	}

	private byte[] apply(String className, List<Patch> patches) throws Exception {

		ClassPool classPool = ClassPool.getDefault();
		CtClass ctClass = classPool.get(className.replace("/", "."));

		for (Patch patch : patches) {

			Log.info("Applying patch " + patch);
			String targetClass = patch.getTargetClass();
			String targetMethod = patch.getTargetMethod();

			if (targetClass.equals(targetMethod) || targetClass.endsWith("." + targetMethod))
				patchConstructor(ctClass, patch);

			else
				patchMethod(ctClass, patch);
		}

		byte[] bytecode = ctClass.toBytecode();
		ctClass.detach();

		return bytecode;

	}

	public byte[] transform(ClassLoader loader, String className, Class<?> classs, ProtectionDomain proectionDomain, byte[] classBytes) {

		byte[] bytecode = classBytes;

		List<Patch> patches = this.patches.get(className);
		if (patches != null) {

			Log.info("Patching class " + className);

			try {

				bytecode = apply(className, patches);
			}
			catch (Exception error) {
				Log.error(error);
			}
		}

		return bytecode;
	}
}
