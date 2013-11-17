package vino.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static String getFormattedNow() {
		return new SimpleDateFormat("MM/dd/yyyy h:mm a").format(new Timestamp(new Date().getTime()));
	}
	
	public static String getFormattedDate(Timestamp timestamp) {
		return new SimpleDateFormat("MM/dd/yyyy h:mm a").format(timestamp);
	}

	public static Timestamp getCurrentTimeStamp() {
		return new Timestamp(new Date().getTime()); 
	}
	
}