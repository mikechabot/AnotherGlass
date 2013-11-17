package com.anotherglass.vino;

public abstract class Job {
	
	public abstract String getJobServletUrl();
	
	public abstract String getJobName();
	
	public abstract String getJobDescription();
	
	public abstract boolean isRunning();
	
}
