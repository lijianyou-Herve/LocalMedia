package com.example.herve.localmedia.utils.log;

import android.util.Log;

import com.example.herve.localmedia.BuildConfig;


public class AppLog {

	public static void i(String msg, String args) {
		if (BuildConfig.DEBUG) {
			Log.i(msg, args);
		}
	}

	public static void d(String msg, String args) {
		if (BuildConfig.DEBUG) {
			Log.d(msg, args);
		}
	}

	public static void e(String msg, String args) {
		if (BuildConfig.DEBUG) {
			Log.e(msg, args);
		}
	}
	
	public static void w(String msg, String args) {
		if (BuildConfig.DEBUG) {
			Log.w(msg, args);
		}
	}
}
