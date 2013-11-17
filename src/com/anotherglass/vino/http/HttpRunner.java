package com.anotherglass.vino.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import com.anotherglass.vino.http.HttpHelper.Method;

public class HttpRunner implements Runnable {

	private static Logger log = Logger.getLogger(HttpRunner.class);

	private String httpUrl;
	private String queryString;

	private int wait;
	private Method action;

    private boolean complete = true;
    private String blob;
    private Exception exception;

    public HttpRunner(String httpUrl, String queryString, int wait, Method action) {
    	this.httpUrl = httpUrl;
    	this.queryString = queryString;
    	this.wait = wait;
    	this.action = action;
    }

    public void collect() throws HttpException {
		Thread thread = new Thread(this, "HttpRunner."+this.action);
        thread.start();

        // wait for specified number of seconds and kill it if runs longer
		try {
	        thread.join(1000 * wait);
	        if (thread.isAlive()) {
	        	complete = false;
	            thread.interrupt();
	        }
		}
		catch (InterruptedException e) {
			log.debug("the request has timed out");
			complete = false;
		}

		if (!complete) throw new HttpException("The request has timed out");
		if (exception != null) throw new HttpException(exception);
    }

    public void run() {
    	try {
        	if (action == Method.GET) {
        		blob = get(httpUrl, queryString);
        	}
        	if (action == Method.POST) {
        		blob = post(httpUrl, queryString);
        	}
        }
        catch (Exception e) {
        	exception = e;
        }
    }

    public boolean isComplete() {
    	return complete;
    }

    public String getBlob() {
    	return blob;
    }

    public boolean isEmpty() {
    	return (blob == null);
    }

    public Exception getException() {
    	return exception;
    }

    private String get(String httpUrl, String queryString) throws HttpException, IOException {

    	if (queryString != null) {
    		httpUrl += "?" + queryString;
    	}

    	log.debug("Making a GET request to URL: "+httpUrl);

    	URL url = null;
    	try {
    		url = new URL(httpUrl);
    	}
    	catch(MalformedURLException e) {
    		throw new HttpException("The URL is not formed correctly");
    	}

    	StringBuilder sb = new StringBuilder();
    	BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

    	String line;

    	while ((line = br.readLine()) != null) {
        	sb.append(line).append("\n");
        }

        try {
        	if (br != null) {
        		br.close();
        	}
        }
        catch(IOException e) {
        	// we're just closing up anyway
        }

        log.debug("GET request complete");
        this.complete = true;

        return sb.toString();
    }

    private String post(String httpUrl, String queryString) throws HttpException, IOException {
        log.debug("Making a POST request to URL: "+httpUrl+ ", queryString: " +queryString);

        // send the data
    	URL url = null;
    	try {
    		url = new URL(httpUrl);
    	}
    	catch(MalformedURLException e) {
    		throw new HttpException("The URL is not formed correctly");
    	}

        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(queryString);
        wr.flush();

        // get the response
    	StringBuilder sb = new StringBuilder();
    	BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

    	String line;

    	while ((line = br.readLine()) != null) {
        	sb.append(line).append("\n");
        }

        try {
        	if (br != null) {
        		br.close();
        	}
        }
        catch(IOException e) {
        	// we're just closing up anyway
        }

        log.debug("POST request complete");
        this.complete = true;

        return sb.toString();
    }
}
