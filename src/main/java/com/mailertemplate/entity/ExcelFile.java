package com.mailertemplate.entity;

public class ExcelFile {
	private String fullPath;
	private String fullPath2;

	public ExcelFile() {}
	
	public ExcelFile(String fullPath) {
		super();
		this.fullPath = fullPath;
	}
	
	public ExcelFile(String fullPath, String fullPath2) {
		super();
		this.fullPath = fullPath;
		this.fullPath2 = fullPath2;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public String getFullPath2() {
		return fullPath2;
	}

	public void setFullPath2(String fullPath2) {
		this.fullPath2 = fullPath2;
	}
	
	
}
