package com.example.herve.localmedia.bean;

public class FileModel {

	private int type;
	private String fileName;

	private String absolutePath;
	public boolean selected = false;

	public FileModel(int type, String fileName, String absolutePath) {
		this.type = type;
		this.fileName = fileName;
		this.absolutePath = absolutePath;
	}

	public int getType() {
		return type;
	}

	public String getFileName() {
		return fileName;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}
}
