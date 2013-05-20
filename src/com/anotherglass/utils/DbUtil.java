package com.anotherglass.utils;

public class DbUtil {

	public static String out(Object object) {
		if(object == null || object.equals("")) {
			return null;
		}
		if(object.getClass().equals(String.class)) {
			object = "'" + object.toString().replace("'","''") + "'";
		}
		return object.toString();
	}
}
