package vino.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpUtils {

	private static final HttpClient client = HttpClientBuilder.create().build();
	
	public static String get(String url) throws HttpException {
		HttpGet request = new HttpGet(url);
		HttpResponse response = null;
		
		try {
			response = client.execute(request);
		}
		catch (IOException e) {
			throw new HttpException("Error executing request", e);
		}
				
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		 
			StringBuilder sb = new StringBuilder();
			
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line).append(System.lineSeparator());
			}
			
			return sb.toString();
		}
		catch (IOException e) {
			throw new HttpException("Error parsing response", e);
		}		
		finally {
			if (br != null) {
				try {
					br.close();
					br = null;
				}
				catch (Exception e) {
					// don't care
				}
			}
		}
	}
	
}
