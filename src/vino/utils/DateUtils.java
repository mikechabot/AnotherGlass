package vino.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static String getFormattedNow() {
		return new SimpleDateFormat("MM/dd/yyyy h:mm a").format(new Timestamp(new Date().getTime()));
	}
	
	public static String getFormattedDateFromLong(long date) {
		return new SimpleDateFormat("MM/dd/yyyy h:mm a").format(new Timestamp(date));
	}
	
	public static String getFormattedDateFromTimestamp(Timestamp timestamp) {
		return new SimpleDateFormat("MM/dd/yyyy h:mm a").format(timestamp);
	}

	public static String getFormattedMiutesFromLong(long date) {
		return new SimpleDateFormat("m:ss").format(new Timestamp(date));
	}
	
	public static Timestamp getCurrentTimeStamp() {
		return new Timestamp(new Date().getTime()); 
	}
	
	public static String getFormattedSpecial(long date) {
		String formatted = getFormattedMiutesFromLong(date);
		String minutes = formatted.substring(0, formatted.indexOf(":"));
		String seconds = formatted.substring(formatted.indexOf(":")+1, formatted.length());
		return minutes + "m " + seconds + "s";
	}
	
}