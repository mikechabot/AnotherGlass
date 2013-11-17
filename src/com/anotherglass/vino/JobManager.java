package com.anotherglass.vino;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.anotherglass.vino.reflections.Reflection;

public class JobManager {

	private static JobManager manager;
	public ConcurrentHashMap<String, Job> jobs = new ConcurrentHashMap<String, Job>();
		
	public static JobManager getInstance() {
		if(manager == null) {
			manager = new JobManager();
		}
		return manager;
	}
	
	public Job getJob(String jobName) {
		return jobs.get(jobName);
	}
	
	public ConcurrentHashMap<String, Job> getJobs() {
		return jobs;
	}
	
	public int getJobsCount() {
		return jobs.size();
	}
	
	public int getRunningJobCount() {
		int count = 0;
		for (Job temp : jobs.values()) {
			if (temp.isRunning()) {
				count++;
			}
		}
		return count;
	}

	public boolean isJobRunning(String jobName) {
		for (Entry<String, Job> temp : jobs.entrySet()) {
			if (temp.getKey().equals(jobName)) {
				return temp.getValue().isRunning();
			}
		}
		return false;
	}
	
	public void addJob(String jobName, Job job) {
		jobs.put(jobName, job);
	}
	
	public void populateJobsMap() {
		for(Job each : Reflection.findSubTypesOf("com.anotherglass.vino.job", Job.class)) {
			jobs.put(each.getJobName(), each);						
		}
	}
	
}
