package com.example.herve.localmedia.utils.file_utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;

public class StorageList {
	private static Context mContext;
	private static StorageManager mStorageManager;
	private static Method mMethodGetPaths;

	public StorageList(Context context) {

		mContext = context.getApplicationContext();
		if (mContext != null) {
			mStorageManager = (StorageManager) mContext.getSystemService(Activity.STORAGE_SERVICE);
			try {
				mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String[] getVolumePaths() {
		String[] paths = null;
		String[] validPaths = null;
		try {
			paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
		} catch (Exception e) {
			e.printStackTrace();
		} 

		if (paths != null && paths.length >= 1) {
			if (isCanReadWrite(paths[0])) {
				validPaths = Arrays.copyOf(paths, 1);
			}
			if (paths.length >= 2) {
				if (isCanReadWrite(paths[0]) && isCanReadWrite(paths[1])) {
					validPaths = Arrays.copyOf(paths, 2);
				}
			}

		}

		if (validPaths == null || validPaths.length == 0) {
			validPaths = new String[1];
			validPaths[0] = Environment.getExternalStorageDirectory().getAbsolutePath();
		}

		return validPaths;
	}

	private boolean isCanReadWrite(String path) {

		File file = new File(path);

		if (file.exists()) {

			if (file.canRead() && file.canWrite()) {

				return true;
			}

		}

		return false;
	}
}