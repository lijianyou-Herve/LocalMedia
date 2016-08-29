package com.example.herve.localmedia.bean;

import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

public class FilesGroup {

	private String searchPath;

	private List<FileModel> data;

	public FilesGroup() {
		searchPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		data = new ArrayList<FileModel>();
	}

	public String getSearchPath() {
		return searchPath;
	}

	public void setSearchPath(String searchPath) {
		this.searchPath = searchPath;
	}

	public List<FileModel> getData() {
		return data;
	}
}
