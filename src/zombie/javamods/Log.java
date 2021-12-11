package zombie.javamods;

import java.util.Calendar;
import java.text.SimpleDateFormat;


public class Log {

	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private final static Calendar calendar = Calendar.getInstance();
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);


	private static void log(String type, String message) {
		String date = dateFormat.format(calendar.getTime());
		System.out.println(type + " " + date + ": " + message);
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
