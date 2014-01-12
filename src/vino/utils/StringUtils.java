package vino.utils;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collections;

public class StringUtils {

    public static String generatePseudoRandomToken() {
        char chars[] = new char[]{'.','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','-','0','1','2','3','4','5','6','7','8','9','~','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','_'};
        Collections.shuffle(Arrays.asList(chars));           
        StringBuilder sb = new StringBuilder();
        for (int i=0; i < 20; i++) {
                int c = MathUtils.generateRandomNumber(0, chars.length-1);
                sb.append(chars[c]);
        }
        return sb.toString();
    }

	public static int parseInt(String param) {
		int result = 0;
		try {
			result = Integer.parseInt(param);
		}
		catch (Exception e) {
			// do nothing
		}
		return result;
	}
	
	public static long parseLong(String param) {
		long result = 0;
		try {
			result = Long.parseLong(param);
		}
		catch (Exception e) {
			// do nothing
		}
		return result;
	}	
	
	public static double parseDouble(String param) {
		double result = 0;
		try {
			result = Double.parseDouble(param);
		}
		catch (Exception e) {
			// do nothing
		}
		return result;
	}

	 public static String toHex(byte[] array) {
	      StringBuilder sb = new StringBuilder();
	      for(byte each : array) {
	    	  sb.append(Integer.toHexString((each & 0xFF) | 0x100).substring(1,3));
	      }
	      return sb.toString();
	  }
	  
	  public static String toMD5(String message) {
	      try {
	    	  MessageDigest md5 = MessageDigest.getInstance("MD5");
	    	  return toHex(md5.digest(message.getBytes("CP1252")));
	      }
	      catch (Exception e) {
	    	  // do nothing
	      }
	      
	      return null;
	  }
}
