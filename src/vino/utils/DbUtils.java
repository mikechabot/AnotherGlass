package vino.utils;

public class DbUtils {

	public static String scrub(Object object) {
		if(object == null || object.equals("")) {
			return null;
		}
		if(object.getClass().equals(String.class)) {
			object = "'" + object.toString().replace("'","''") + "'";
		}
		return object.toString();
	}
}
