package com.androidtest.HttpParser2.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class NetworkBase {
	public static final String CLIEN_URL = "http://clien.career.co.kr/";
	
	private static DefaultHttpClient httpclient = null;
	private static CookieStore cookies = null;
	private static boolean LOGINED = false;
	
	public static HttpClient getHttp(boolean cache) {
		httpclient = new DefaultHttpClient();
	    if (cookies != null) httpclient.setCookieStore(cookies);
		
		return httpclient;
	}
	
	public static boolean isLogin() {
		return LOGINED;
	}
	
	public static void Logout() {
		NetworkBase.LOGINED = false;
		cookies = null;
	}

	public static boolean getLogin(String id, String pwd) {
		if (LOGINED) {
			return false;
		}
		
		InputStream is = null;
		String url = NetworkBase.CLIEN_URL+"cs2/bbs/login_check.php";
		
		try {
			
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("mb_id", id));
			nameValuePairs.add(new BasicNameValuePair("mb_password", pwd));

			HttpPost httppost = new HttpPost(url);
			UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
			httppost.setEntity(entityRequest);
			
			HttpResponse response = NetworkBase.getHttp(true).execute(httppost);
			is = response.getEntity().getContent();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			
			if (sb.indexOf("nowlogin=1")>-1) { 
				LOGINED = true;
				cookies = httpclient.getCookieStore();
				return true;
			}
		} catch (Exception e) {
			Log.e("debug", e.toString(), e);
		} finally {
			try {is.close();} catch (Exception e){}
		}
		return false;
	}
	
	public static String getData(String url, Map<String, String> params) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		InputStream is = null;
		try {
			HttpPost httppost = new HttpPost(url);
			if (params != null) {
		        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		        for (Map.Entry<String, String> entry: params.entrySet()) {
		            paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		        }
		        httppost.setEntity(new UrlEncodedFormEntity(paramList,"utf-8"));
			}
						
			HttpResponse response = NetworkBase.getHttp(true).execute(httppost);
			is = response.getEntity().getContent();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
			
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			
		} catch (Exception e) {
			Log.e("debug", e.toString(), e);
			throw e;
		} finally {
			try {is.close();} catch (Exception e) {}
		}
		
		return sb.toString();
	}
	
	public static String getData(String url) throws Exception {
		return getData(url, null);
	}
	
	public static String getHtml(String str_url) {
				
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		String url = str_url;
		
		try {
			HttpPost httppost = new HttpPost(url);
						
			HttpResponse response = NetworkBase.getHttp(true).execute(httppost);
			is = response.getEntity().getContent();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
			
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			
		} catch (Exception e) {
			Log.e("debug", e.toString(), e);
		} finally {
			try {is.close();} catch (Exception e){}
		}
		return sb.toString();
	}
}

