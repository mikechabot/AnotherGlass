package vino.job;

public abstract class Job {
	
	public abstract boolean isRunning();
	
	public abstract String getJobServletUrl();
	
	public abstract String getJobName();
	
	public abstract String getJobDescription();
	
	public abstract String getLastRunDate();
	
	public abstract String getLastRunDuration();
	
}
