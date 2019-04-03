package com.mailertemplate.entity;

public class Upskilling {
	private String programName;
	private String status;

	public Upskilling(String programName, String status) {
		super();
		this.programName = programName;
		this.status = status;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	@Override
	public String toString() {
		return "Upskilling [programName=" + programName + ", status=" + status + "]";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}