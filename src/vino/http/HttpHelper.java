package vino.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HttpHelper {

	public static enum Method { GET, POST };

	private int timeout = 5*60; // in seconds, (5 minutes)

    public HttpHelper() { }

    public HttpHelper(int timeout) { this.timeout = timeout; }

    public String get(String httpUrl) throws HttpException {
    	return get(httpUrl, null);
    }

    public String get(String httpUrl, int wait) throws HttpException {
    	return get(httpUrl, null, wait);
    }

    public String get(String httpUrl, String queryString) throws HttpException {
    	return get(httpUrl, queryString, timeout);
    }

    public String get(String httpUrl, String queryString, int wait) throws HttpException {
    	HttpRunner runner = new HttpRunner(httpUrl, queryString, wait, Method.GET);
    	runner.collect();

        if(runner.isComplete()) {
        	return runner.getBlob();
        }
        else {
            return null;
        }
    }

    public String post(String httpUrl, String queryString) throws HttpException {
    	return post(httpUrl, queryString, timeout);
    }

    public String post(String httpUrl, String queryString, int wait) throws HttpException {
    	HttpRunner runner = new HttpRunner(httpUrl, queryString, wait, Method.POST);
    	runner.collect();

        if(runner.isComplete()) {
        	return runner.getBlob();
        }
        else {
        	return null;
        }
    }

	public String getQueryString(String... params) {
		StringBuilder sb = new StringBuilder();
		try {

			if(params != null) {
				int i=1;
				for(String param : params) {
					if (i % 2 == 0) {
						sb.append("=").append(param != null ? URLEncoder.encode(param, "UTF-8") : "");
					}
					else {
						if(i > 2) sb.append("&");
						sb.append(URLEncoder.encode(param, "UTF-8"));
					}
					i++;
				}
			}
		}
		catch (UnsupportedEncodingException e) {
			// do nothing
		}
		return sb.toString();
	}
}