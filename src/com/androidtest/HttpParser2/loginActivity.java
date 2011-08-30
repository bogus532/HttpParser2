package com.androidtest.HttpParser2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.androidtest.HttpParser2.util.NetworkBase;
import com.androidtest.HttpParser2.util.UserInfo;
import com.androidtest.HttpParser2.util.Util;

public class loginActivity extends Activity {
	private static final String TAG = "loginActivity";
	
	private static UserInfo loginUser = null;
	
	ProgressDialog mDialog;
	loginItem loginitem;
	
	Display display;
		

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        Context context = getApplicationContext();
        display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        
        setTitle("로그인");
        findViewById(R.id.login_fail_textview).setVisibility(View.INVISIBLE);
        
        Button logoutButton = (Button)findViewById(R.id.login_logout_button);
        if (NetworkBase.isLogin()) {
        	String id = Util.getSharedData(this, Util.LOGIN_ID, "");
        	String pass = Util.getSharedData(this, Util.LOGIN_PWD, "");
        	((EditText)findViewById(R.id.login_id_edittext)).setText(id);
        	((EditText)findViewById(R.id.login_passwod_edittext)).setText(pass);
        	logoutButton.setText("로그아웃");
        } else {
        	String id = Util.getSharedData(this, Util.LOGIN_ID, "");
        	String pass = Util.getSharedData(this, Util.LOGIN_PWD, "");
        	((EditText)findViewById(R.id.login_id_edittext)).setText(id);
        	((EditText)findViewById(R.id.login_passwod_edittext)).setText(pass);
        	logoutButton.setText("로그인");
        }
        
        String autoLogin = Util.getSharedData(this, Util.LOGIN_AUTO_LOGIN, "false");
        if (autoLogin.equals("true")) {
        	CheckBox check = (CheckBox)findViewById(R.id.login_autologin_checkbox);
        	check.setChecked(true);
        }
	}
	
	public void onClickAutoLogin(View v) {
    }
	
	public void onClickButtonLogin(View v) {
    	if (NetworkBase.isLogin()) { 
    		NetworkBase.Logout();
    		finish();
    		return;
    	}
    	EditText idEditText = (EditText)findViewById(R.id.login_id_edittext);
    	EditText pwEditText = (EditText)findViewById(R.id.login_passwod_edittext);
    	
    	String id = idEditText.getText().toString();
    	String pass = pwEditText.getText().toString();
    	
    	if (id.length()<1 || pass.length()<1) {
    		Toast.makeText(this, "입력값이 없습니다.", 1000).show();
    	}
    	
    	boolean isSuccess = NetworkBase.getLogin(id, pass);
    	if (isSuccess) {
    		Intent intent = getIntent();
    		loginActivity.loginUser = new UserInfo();
    		loginActivity.loginUser.setId(id);
    		setResult(RESULT_OK,intent);
    		finish();
    		return;
    	} else {
    		findViewById(R.id.login_fail_textview).setVisibility(View.VISIBLE);
    	}
    }
    
    public void finish() {
    	CheckBox check = (CheckBox)findViewById(R.id.login_autologin_checkbox);
    	EditText idEditText = (EditText)findViewById(R.id.login_id_edittext);
    	EditText pwEditText = (EditText)findViewById(R.id.login_passwod_edittext);
    	
    	String id = idEditText.getText().toString();
    	String pass = pwEditText.getText().toString();
    	
		if (check.isChecked()) {
			Util.setSharedData(this, Util.LOGIN_AUTO_LOGIN, "true");
    		Util.setSharedData(this, Util.LOGIN_ID, id);
    		Util.setSharedData(this, Util.LOGIN_PWD, pass);
		} else {
			Util.setSharedData(this, Util.LOGIN_AUTO_LOGIN, "false");
			Util.setSharedData(this, Util.LOGIN_ID, "");
			Util.setSharedData(this, Util.LOGIN_PWD, "");
		}
		super.finish();
    }
	


}
