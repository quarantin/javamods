package javamods;

import java.io.IOException;
import java.io.PrintWriter;

import java.text.SimpleDateFormat;

import java.util.Calendar;


public class Log {

	private final static String format = "yyyy-MM-dd HH:mm:ss";
	private final static Calendar calendar = Calendar.getInstance();
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat(format);

	private static PrintWriter out;

	protected static void init() throws IOException {
		out = new PrintWriter(Filesystem.getLogFile());
	}

	protected static void exit() {
		if (out != null)
			out.close();
	}

	private static void log(String type, String message) {

		String date = dateFormat.format(calendar.getTime());
		String msg = type + "\t" + date + "\t" + message;

		System.out.println(msg);
		if (out != null) {
			out.println(msg);
			out.flush();
		}
	}

	public static void debug(String message) {
		if (Core.doDebug())
			log("DEBUG  ", message);
	}

	public static void info(String message) {
		log("INFO   ", message);
	}

	public static void warn(String message) {
		log("WARNING", message);
	}

	public static void error(String message) {
		log("ERROR  ", message);
	}

	public static void error(Throwable throwable) {
		throwable.printStackTrace();
		if (out != null) {
			out.println("\n=== STACK TRACE ===\n");
			throwable.printStackTrace(out);
			out.println();
		}
	}
}
