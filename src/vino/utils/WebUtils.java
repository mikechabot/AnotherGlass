package vino.utils;

import javax.servlet.http.HttpServletRequest;

public class WebUtils {

	public static String baseUrl(HttpServletRequest request) {
		String scheme = request.getScheme();
		int port = request.getServerPort();
		String host = sanitize(request.getServerName());
		String path = sanitize(request.getContextPath());
		return scheme + "://" + host + ((port == 80 || port == 443) ? ":" + port : "") + path;
	}

	public static String sanitize(String str) {
		if (str == null) return null;
	
		StringBuilder sb = new StringBuilder();
	
		for(char c : str.toCharArray()) {
			if (c == '&') sb.append("&amp;");
			else if (c == '<') sb.append("&lt;");
			else if (c == '>') sb.append("&gt;");
			else if (c == '"') sb.append("&#034;");
			else if (c == '\'') sb.append("&#039;");
			else if (c == '\n') sb.append("<br/>");
			else sb.append(c);
		}
	
		return sb.toString();
	}
	
}