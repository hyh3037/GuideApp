package com.nickming.view;

public class Recorder {
	private float time;
	private String filePathString;

	public Recorder(float time, String filePathString) {
		super();
		this.time = time;
		this.filePathString = filePathString;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public String getFilePathString() {
		return filePathString;
	}

	public void setFilePathString(String filePathString) {
		this.filePathString = filePathString;
	}
}
