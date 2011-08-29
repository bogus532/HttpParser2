package com.androidtest.HttpParser2.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Util {
	public static final String LOGIN_ID = "login_id";
	public static final String LOGIN_PWD = "login_pass";
	public static final String LOGIN_AUTO_LOGIN = "login_auto_login";
	
	
	private static Context context;
	public static String getSharedData(Context context, String _key, String _defaultData) {
		if (Util.context == null) Util.context = context;
		SharedPreferences pref = null;
		try {
			pref=Util.context.getSharedPreferences("com.androidtest.HttpParser2", Activity.MODE_PRIVATE);
		} catch (Exception e) {
			Log.w("debug", e.toString(), e);
			return _defaultData;
		}
		if (pref==null) return _defaultData;
		return pref.getString(_key, _defaultData);
	}

	public static void setSharedData(Context context, String _key, String _data) {
		if (Util.context == null) Util.context = context;
		SharedPreferences p = Util.context.getSharedPreferences("com.androidtest.HttpParser2", Activity.MODE_PRIVATE);
		SharedPreferences.Editor e = p.edit();
		e.putString(_key, _data);
		e.commit();
	}

}
