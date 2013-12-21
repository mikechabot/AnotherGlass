package vino.job;

public interface Job extends Runnable {
	
	public boolean isRunning();
	
	public String getJobServletUrl();
	
	public String getJobName();
	
	public String getJobDescription();
	
	public String getLastRunDate();
	
	public String getLastRunDuration();
	
}
