package zombie.javamods;

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

	private static void log(String type, String message) {
		String date = dateFormat.format(calendar.getTime());
		out.println(type + " " + date + ": " + message);
	}

	public static void debug(String message) {
		if (Core.debug)
			log("DEBUG", message);
	}

	public static void info(String message) {
		log("INFO", message);
	}

	public static void warn(String message) {
		log("WARNING", message);
	}

	public static void error(String message) {
		log("ERROR", message);
	}
}
