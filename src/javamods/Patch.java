package javamods;


public class Patch {

	private String targetClass;
	private String targetMethod;
	private String methodBody;
	private boolean after;

	public Patch(String targetClass, String targetMethod, String methodBody) {
		this(targetClass, targetMethod, methodBody, false);
	}

	public Patch(String targetClass, String targetMethod, String methodBody, boolean after) {
		this.targetClass = targetClass;
		this.targetMethod = targetMethod;
		this.methodBody = methodBody;
		this.after = after;
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
