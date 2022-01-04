package javamods;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class Patch {

	private HashSet<String> imports;
	private String targetClass;
	private String targetMethod;
	private String methodBody;
	private boolean after;

	public Patch(String targetClass, String targetMethod, String methodBody) {
		this(targetClass, targetMethod, methodBody, false);
	}

	public Patch(String targetClass, String targetMethod, String methodBody, boolean after) {
		this.imports = new HashSet<>();
		this.targetClass = targetClass;
		this.targetMethod = targetMethod;
		this.methodBody = methodBody;
		this.after = after;
	}

	public void addImport(String pkg) {
		this.imports.add(pkg);
	}

	public void addImports(String[] packages) {
		this.imports.addAll(Arrays.asList(packages));
	}

	public void addImports(List<String> packages) {
		this.imports.addAll(packages);
	}

	public String[] getImports() {
		return this.imports.toArray(new String[this.imports.size()]);
	}

	public String getTargetClass() {
		return this.targetClass;
	}

	public String getTargetMethod() {
		return this.targetMethod;
	}

	public String getMethodBody() {
		return this.methodBody;
	}

	public boolean insertAfter() {
		return this.after;
	}
}
