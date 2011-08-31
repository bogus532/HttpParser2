package com.androidtest.HttpParser2.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

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
	
	public static OnTouchListener getTouchChangeColor(final View layout) {
		return new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
					//layout.setBackgroundColor(Color.parseColor("#AAAAAA"));
					layout.setBackgroundColor(Color.parseColor("#FF0000"));
				} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
					layout.setBackgroundColor(Color.BLACK);
				} else if (arg1.getAction() == MotionEvent.ACTION_CANCEL) {
					layout.setBackgroundColor(Color.BLACK);
				}
				return false;
			}
		};
	}

}
