package vino;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

import vino.utils.StringUtils;

public class Params {

	private static final Logger log = Logger.getLogger(Params.class);
	
	private HttpServletRequest request;
	private Map<String, String> map;

	public Params(HttpServletRequest request) {
		this.request = request;
		init();
	}

	public boolean hasQueryString() {
		return (request.getQueryString() != null);
	}

	public boolean isGet() {
		return "GET".equalsIgnoreCase(request.getMethod());
	}

	public boolean isPost() {
		return "POST".equalsIgnoreCase(request.getMethod());
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean isDebug() {
		return getBoolean("debug");
	}
	
	public String getIpAddress() {
		return request.getRemoteAddr();
	}
	
	public String getUserAgent() {
		return request.getHeader("User-Agent");
	}
	
	public Cookie[] getCookies() {
		return request.getCookies();
	}
	
	public String getCookieValue(String name) {
		for (Cookie temp : getCookies()) {
			if(temp.getName().equals(name)) {
				return temp.getValue();
			}
		}
		return null;
	}
	
	public Cookie getCookie(String name) {
		for (Cookie cookie : getCookies()) {
			if(cookie.getName().equals(name)) {
				return cookie;
			}
		}
		return null;
	}

	public byte[] getFileUploadBytes(String name) {
		Part part = getFileUploadPart(name);
		
		if (part == null) {
			return null;
		}
		
		byte[] bytes = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		
		try {
			is = part.getInputStream();
			baos = new ByteArrayOutputStream();			

			int read = 0;
	        final byte[] buffer = new byte[1024];

	        while ((read = is.read(buffer)) != -1) {
	        	baos.write(buffer, 0, read);
	        }
	        
	        bytes = baos.toByteArray();
		}
		catch (IOException e) {
			// do nothing
		}
		finally {
			if (baos != null) {
				try {
					baos.close();
					baos = null;
				}
				catch(IOException e) {
					// do nothing
				}
			}				
			if (is != null) {
				try {
					is.close();
					is = null;
				}
				catch(IOException e) {
					// do nothing
				}
			}
		}
		
		return bytes;
	}
	
	public String getFileUploadName(String name) {
		Part part = getFileUploadPart(name);
		
		if (part != null) {
		    for (String content : part.getHeader("content-disposition").split(";")) {
		        if (content.trim().startsWith("filename")) {
		            return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
		        }
		    }
		}
		
	    return null;
	}
	
	public String getFileUploadContentType(String name) {
		Part part = getFileUploadPart(name);
		if (part != null) {
			return part.getContentType();
		}
		return null;
	}
	
	private Part getFileUploadPart(String name) {
		int n = 0;
		
		try {
			Collection<Part> parts = request.getParts();
			n = parts.size();
		}
		catch (Exception e) {
			log.debug("parsing request for parts failed", e);
		}
		
		if (n == 0) {
			log.debug("There were no parts in this request");
			return null;
		}
		
		Part part = null;
		
		try {
			part = request.getPart(name);
		}
		catch (Exception e) {
			log.debug("parsing request for part="+name+" failed", e);
		}
		
		return part;
	}
	
	public void setParam(String name, String value) {
		map.put(name, value);
	}

	public String[] getStrings(String param) {
		List<String> temp = new ArrayList<String>(0);
		String values = get(param);
		if (values != null) {
			for(String each : values.split(",")) {
				temp.add(each);
			}
		}
		return temp.toArray(new String[temp.size()]);
	}
	
	public String getString(String param) {
		return get(param);
	}

	public String getString(String param, String dft) {
		if (get(param) != null) {
			return get(param);
		}
		return dft;
	}

	public boolean getBoolean(String param) {
		String value = get(param);
		if (value != null) {
			if("y".equalsIgnoreCase(value)) return true;
			if("yes".equalsIgnoreCase(value)) return true;
			if("true".equalsIgnoreCase(value)) return true;
		}
		return false;
	}

	public long getLong(String param) {
		return StringUtils.parseLong(get(param));
	}

	public long getLong(String param, long dft) {
		if (get(param) != null) {
			return getLong(param);
		}
		return dft;
	}

	public int getInt(String param) {
		return StringUtils.parseInt(get(param));
	}

	public int getInt(String param, int dft) {
		if (get(param) != null) {
			return getInt(param);
		}
		return dft;
	}

	public double getDouble(String param) {
		return StringUtils.parseDouble(get(param));
	}

	public double getDouble(String param, double dft) {
		if (get(param) != null) {
			return getDouble(param);
		}
		return dft;
	}

	public String getMonth(String param) {
		return getMonth(param, false);
	}

	public String getMonth(String param, boolean required) {
		return getMatch(param, required, "^((0[1-9])|(1[0-2]))$", "must be a month eg. 01-12");
	}

	public String getYear(String param) {
		return getYear(param, false);
	}

	public String getYear(String param, boolean required) {
		return getMatch(param, required, "[0-9]{4}", "must be a 4 digit number year");
	}

	public String getMatch(String param, boolean required, String regex, String error) {
		String value = get(param);
		if (value != null) {
			if (value.matches(regex)) {
				return value;
			}
		}
		return "";
	}

	private String get(String param) {
		if (map.get(param) == null || map.get(param).equals("")) {
			return null;
		}
		return map.get(param);
	}

	public String toQueryString(String... excluding) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		for(Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (excluding != null && Arrays.asList(excluding).contains(key)) continue;
			if (sb.length() > 0) sb.append("&");
			sb.append(key).append("=").append(value != null ? URLEncoder.encode(value, "UTF-8") : "");
		}

		if (sb.length() > 0) return "?"+sb.toString();
		return "";
	}

	@Override
	public String toString() {
		return map.toString();
	}

	private void init() {
		Map<String, String[]> params = request.getParameterMap();
		map = new HashMap<String, String>(0);
		for (String key : params.keySet()) {
			String value = null;
			for (String temp : params.get(key)) {
				if (temp == null) continue;
				temp = temp.trim();
				if (temp.length() == 0) continue;
				if (value == null) {
					value = temp;
				}
				else {
					value += "," + temp;
				}
			}
			map.put(key, value);
		}
	}
	
}