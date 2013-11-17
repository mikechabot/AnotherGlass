package vino.database;

public class Utils {

	public static String sanitizeSingleQuote(Object object) {
		if(object == null || object.equals("")) {
			return null;
		}
		if(object.getClass().equals(String.class)) {
			object = "'" + object.toString().replace("'","''") + "'";
		}
		return object.toString();
	}
}
