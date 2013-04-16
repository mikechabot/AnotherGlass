package com.anotherglass.http;

public class HttpException extends Exception {
    
	private static final long serialVersionUID = 1L;
	
	Throwable cause;
    
    public HttpException() {
        super();
        cause = null;
    }
    
    public HttpException(Exception e) {
        super(e);
    }        
    
    public HttpException(String str) {
        super(str);
        cause = null;
    }
    
    public HttpException(String str, Throwable t) {
        super(str);            
        cause = t;
    }
}